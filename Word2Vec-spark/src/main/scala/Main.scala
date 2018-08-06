package main.scala

//import org.apache.spark.implicits._
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd._
import org.apache.spark.SparkConf
import org.apache.spark.sql.{SQLContext, Row}
import com.databricks.spark.xml
import org.apache.spark.sql.types.{StructType, StructField, StringType, DoubleType, IntegerType};
import org.apache.spark.sql.functions.{explode, col}

import org.apache.spark.mllib.feature.{Word2Vec, Word2VecModel}

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

        val revisionSchema = StructType(Array(
            StructField("revision", StringType, nullable = true),
            StructField("id", IntegerType, nullable = false),
            StructField("text", StringType, nullable = true))
        )

        val textRegex = "[^A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ]+"

        var stopwords = sc.textFile("hdfs://localhost:9000/usr/local/stopwords.txt")
            .flatMap(line => line.split(" "))
            .collect
            .toSet

        val df = sqlContext.read
            .format("com.databricks.spark.xml")            
            .option("rowTag", "revision")
            .schema(revisionSchema)
            .load(dir + "/" + inputFileName)

        val selectedData = df.select("text")

        var input = sc.parallelize(selectedData
            .collect
            .take(10000)
            .map(row => row.toString
                .replaceAll(textRegex, " ")
                .toLowerCase
                .split(" ")
                .toSeq
                .filter(!stopwords.contains(_))))

        input.foreach(println)

        val word2vec = new Word2Vec()

        val model = word2vec.fit(input)

        val synonyms = model.findSynonyms("verbo", 10)

        for((synonym, cosineSimilarity) <- synonyms) {
            println(s"$synonym $cosineSimilarity")
        }

        model.save(sc, "models/word2vecModel")
    }
}