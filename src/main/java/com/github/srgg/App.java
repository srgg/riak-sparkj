package com.github.srgg;

import com.basho.riak.client.core.query.Namespace;
import com.basho.riak.spark.japi.SparkJavaUtil;
import com.basho.riak.spark.japi.rdd.RiakJavaRDD;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.IOException;
import java.util.Properties;

public class App
{
    public static void main( String[] args )
    {
        String bucket = System.getProperty("kvbucket");
        if (bucket == null) {
            bucket = "test";
        }

        final SparkConf conf = new SparkConf().setAppName("Java Riak Spark Connector usage example");
        conf.setIfMissing("spark.master", "spark://172.17.0.2:7077");
        conf.setIfMissing("spark.riak.connection.host", "192.168.2.236:10017");

        final String depsPath = getDepsDir();

        conf.setJars(new String[]{depsPath + "/spark-riak-connector_2.10-1.6.0-uber.jar"});

        final JavaSparkContext jsc = new JavaSparkContext(conf);
        try {
            final RiakJavaRDD<String> rdd = SparkJavaUtil.javaFunctions(jsc).riakBucket(new Namespace(bucket), String.class).queryAll();
            final long r = rdd.count();

            System.out.println("\n\n\nCOUNT: " + r + "\n\n");
        } finally {
            jsc.stop();
        }
    }

    private static String getDepsDir() {
        final java.io.InputStream is = App.class.getResourceAsStream("/app.properties");

        if (is == null) {
            throw new IllegalStateException("Can't load 'app.properties'.");
        }

        final Properties props = new Properties();
        try {
            props.load(is);
            final String s = props.getProperty("dependencies-dir");

            if (s == null) {
                throw new IllegalStateException("The 'dependencies-dir' is not defined in app.properties.");
            }

            return s;
        } catch (IOException e) {
            throw new RuntimeException("Error occured during app.properties load.");
        }
    }
}
