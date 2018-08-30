package main.scala

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd._
import org.apache.spark.SparkConf
import org.apache.spark.sql.{SQLContext, Row}
import org.apache.spark.sql.types.{StructType, StructField, StringType, DoubleType, IntegerType};
import org.apache.spark.sql.functions.{explode, col}

object Main {
    def main(args: Array[String]) {
        if (args.length < 2) {
            System.err.println("folder and model name are required")
            System.exit(1)
        }
        val modelPath = args(0)
        val modelName = args(1)
        val conf = new SparkConf()
            .setAppName("Word2Vec")
        val sc = new SparkContext(conf)
        
        val trainer = new Word2VecTrainer(sc, modelPath, modelName)
    }
}