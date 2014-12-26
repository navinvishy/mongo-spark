mongo-spark
===========

A simple example using Apache Spark and MongoDB. This project uses Hadoop 2.6.0 and the Mongo Hadoop connector 1.3.1.

Steps to get this application running on a Mac:
- Install MongoDB
- Install homebrew 
- Use homebrew to install Hadoop. Use this command-line: $ brew install hadoop. This link: http://shayanmasood.com/blog/how-to-setup-hadoop-on-mac-os-x-10-9-mavericks/ has detailed instructions on setting up Hadoop on a Mac.
- Use homebrew to install Maven. Use this command-line: $ brew install maven
- Clone and build the MongoHadoop connector source code by following instructions here: https://github.com/mongodb/mongo-hadoop
- If you checked out version 1.3.1(using the command $ git checkout r1.3.1), the build should create a jar file in core/build/libs
- Download the mongo-java-driver jar from here: http://central.maven.org/maven2/org/mongodb/mongo-java-driver/2.13.0-rc1/
- With Hadoop 2.6.0, I'm not sure where exactly these 2 jars need to be placed. I placed them in the following locations:
  - $HADOOP_HOME/libexec/share/hadoop/hdfs
  - $HADOOP_HOME/libexec/share/hadoop/hdfs/lib
  - $HADOOP_HOME/libexec/share/hadoop/mapreduce
  - $HADOOP_HOME/libexec/share/hadoop/mapreduce/lib
- If you used homebrew to install Hadoop 2.6.0, $HADOOP_HOME would be /usr/local/Cellar/hadoop/2.6.0
- Get the zips data into MongoDB using the following steps:
  - If you dont have wget installed, use this command-line to install it. $ brew install wget
  - $ wget http://media.mongodb.org/zips.json?_ga=1.76268124.854354573.1417925689 > zipdata.json
  - $ mongoimport --db zipsdb --collection zips --file zipdata.json
  
This should set up your environment. Steps to run:
  - Start hdfs. cd into /usr/local/Cellar/hadoop/2.6.0/sbin and run $ ./start-dfs.sh. Browse to http://localhost:50070/ to make sure Hadoop is running.
  - Run the project. This should create a file in hdfs. You can view the contents of the file as follows:
````
$ hdfs dfs -cat hdfs://localhost:9000/state_pops.txt/part-00000
14/12/25 21:49:32 WARN util.NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
(NE,1578139)
(NY,17990402)
(IA,2776420)
(IL,11427576)
(PA,11881643)
(CT,3287116)...
````
