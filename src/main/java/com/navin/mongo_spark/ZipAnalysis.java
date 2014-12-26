package com.navin.mongo_spark;

import org.apache.hadoop.conf.Configuration;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.bson.BSONObject;
import org.slf4j.Logger;

import com.mongodb.hadoop.MongoInputFormat;

import scala.Tuple2;

public class ZipAnalysis {
	private static final String HDFS_HOST = "hdfs://localhost:9000";
	private static final String MONGO_HOST = "mongodb://127.0.0.1:27017/";
	public static void main(String[] args){
		SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("Zips");
		JavaSparkContext sc = new JavaSparkContext(sparkConf);
		Configuration bsonConfig = new Configuration();
		bsonConfig.set("mongo.job.input.format", "com.mongodb.hadoop.MongoInputFormat.class");
		bsonConfig.set("mongo.input.uri", MONGO_HOST+"zipsdb.zips");
		JavaPairRDD<Object,BSONObject> zipData = sc.newAPIHadoopRDD(bsonConfig, MongoInputFormat.class, Object.class, BSONObject.class);
		JavaPairRDD<String,Integer> statePopsMap = zipData.mapToPair(new PairFunction<Tuple2<Object,BSONObject>,String,Integer>(){
				public Tuple2<String,Integer> call(Tuple2<Object,BSONObject> zip){
					Tuple2<String, Integer> tuple = new Tuple2<String,Integer>((String)zip._2.get("state"),(Integer)zip._2.get("pop"));	
					return tuple;
				}
		});
		JavaPairRDD<String,Integer> statesPops = statePopsMap.reduceByKey(new Function2<Integer,Integer, Integer>(){
			public Integer call(Integer i1, Integer i2){
				return i1 + i2;
			}
		});
		statesPops.saveAsTextFile(HDFS_HOST + "/state_pops.txt");
		sc.close();
	}
}
