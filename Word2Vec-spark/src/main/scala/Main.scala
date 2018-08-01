package main.scala

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.sql.SQLContext
import com.databricks.spark.xml
import org.apache.spark.sql.types.{StructType, StructField, StringType, DoubleType};

object Main {
    def main(args: Array[String]) {
        if (args.length < 2) {
            System.err.println("dir or file not pass")
            System.exit(1)
        }
        val dir = args(0)
        val inputFileName = args(1)
        val conf = new SparkConf()
            .setAppName("Word2Vec")
        val sc = new SparkContext(conf)

        val sqlContext = new SQLContext(sc)
        val customSchema = StructType(Array(
            StructField("_id", StringType, nullable = true),
            StructField("author", StringType, nullable = true),
            StructField("description", StringType, nullable = true),
            StructField("genre", StringType ,nullable = true),
            StructField("price", DoubleType, nullable = true),
            StructField("publish_date", StringType, nullable = true),
            StructField("title", StringType, nullable = true))
        )

        val df = sqlContext.read
            .format("com.databricks.spark.xml")
            .option("rowTag", "book")
            .schema(customSchema)
            .load(dir + "/" + inputFileName)

        val selectedData = df.select("author", "_id")
            selectedData.write
            .format("com.databricks.spark.xml")
            .option("rootTag", "books")
            .option("rowTag", "book")

        selectedData.printSchema
        selectedData.show
    }
}