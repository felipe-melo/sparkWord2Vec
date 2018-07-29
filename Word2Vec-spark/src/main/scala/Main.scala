package main.scala

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

object Main {
    def main(args: Array[String]) {
        val conf = new SparkConf()
            .setAppName("Word2Vec")
        val sc = new SparkContext(conf)

        val data = sc.textFile("hdfs://localhost:9000/user/felipemelo/Sentiment_Analysis_Dataset.csv")
        val tweets = data.map(line => line.split(",")(3).trim())
        val words = tweets.flatMap(tweet => tweet.split(" "))
        val hashTags = words.filter(word => word.startsWith("#"))
        val hashTagsTuple = hashTags.map(hashTag => (hashTag, 1))
        val hashTagsCount = hashTagsTuple.reduceByKey((v1, v2) => v1 + v2)
        val hashTagsSorted = hashTagsCount.sortBy(tuple => tuple._2, false)
        hashTagsSorted.take(10).foreach(println)
    }
}