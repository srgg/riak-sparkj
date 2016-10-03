# Simple Riak Spark Java Connector Example

It's a small example application how to use [Riak Spark Connector](https://github.com/basho/spark-riak-connector) from a Java code, it calculates the number of values in the bucket.

## Run the example

You need to have Riak and Spark clusters up and running.

To spin-up Spark Cluster [docker-spark](https://github.com/gettyimages/docker-spark#docker-compose-example) might be used, it is only required to change spark image to 1.6.2-hadoop-2.6:

```
sed -i -e 's/gettyimages\/spark/gettyimages\/spark\:1.6.2-hadoop-2.6/g' ./docker-compose.yml
```

** TODO NEED TO ADD how to run riak from docker **

You could run example as follows:
```
mvn package exec:java -Dspark.master=<spark-master-url> -Dspark.riak.connection.host=<riak-host-and-port>
```
 
Example:
```
mvn package exec:java -Dkvbucket=test -Dspark.master=spark://172.17.0.2:7077 -Dspark.riak.connection.host=192.168.2.236:10017
```


As a result of the execution you should see in the output something like that:

```
16/10/03 15:37:55 INFO TaskSchedulerImpl: Removed TaskSet 0.0, whose tasks have all completed, from pool 
16/10/03 15:37:55 INFO DAGScheduler: Job 0 finished: count at App.java:28, took 6.450440 s



COUNT: 0


16/10/03 15:37:55 INFO SparkUI: Stopped Spark web UI at http://192.168.99.1:4040
16/10/03 15:37:55 INFO SparkDeploySchedulerBackend: Shutting down all executors
```
  
whereas COUNT: 0 means that there are no values in the bucket 'test'.

You could use following commands: add and check data to/in bucket:

- put value to the bucket:
    ```
    curl -X POST -H "Content-Type: plain/text" --data "Test1"  localhost:10018/buckets/test/keys/k1
    ```
- list all teh keys in the bucket:
    ```
    curl localhost:10018/buckets/test/keys?keys=true
    ```
 

 
