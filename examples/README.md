Hi, this is a demo of spark streaming, 

The components are:

* Spark master + worker through docker compose
* Spark streaming example code (a modified version of the official spark streaming.NetworkWorkCount example)
  * It will run in the master
  * It connects to a socket
* A custom server in scala, which the streaming task will connect to
* A set of scripts to conveniently run programs

See our github repo for more information:
http://github.com/balkian/docker-spark
