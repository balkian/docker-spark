docker run --rm --net dockerspark_sparknet -v $PWD/conf/worker:/conf -v $PWD/data:/tmp/data gettyimages/spark bin/spark-class org.apache.spark.deploy.worker.Worker spark://master:7077
