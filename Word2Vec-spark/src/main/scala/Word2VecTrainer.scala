package main.scala

import java.net.URI;
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import java.nio.file.{ Files, FileSystems }
import org.apache.hadoop.fs.{Path, FileSystem};
import org.apache.spark.mllib.feature.{Word2Vec, Word2VecModel}

class Word2VecTrainer(val sc: SparkContext, val workPath: String, val modelName: String) {
    private val word2vec = new Word2Vec();
    private val modelPath = workPath + modelName
    private val model = this.load("row", "Text");

    private def load(tag: String, attr: String): Word2VecModel = {
        var inputName = "Comments.xml"
        var isModelExists = false
        if (modelPath.contains("hdfs")) {
            val conf = sc.hadoopConfiguration
            val fs = FileSystem.get(URI.create(workPath), conf)
            isModelExists = fs.exists(new Path(modelPath))
        } else {
            inputName = "pt.stackoverflow/" + inputName
            val defaultFS = FileSystems.getDefault()
            val path = defaultFS.getPath(modelPath)
            isModelExists = Files.exists(path)
        }

        if (isModelExists) {
            return Word2VecModel.load(sc, modelPath)
        } else {
            val reader = new XMLReader(sc, workPath, inputName)
            var input = sc.parallelize(reader.read(tag, attr))
            var model = word2vec.fit(input)
            model.save(sc, modelPath)
            return model
        }
    }

    def getSynonymsByWord(word: String, n: Int = 10): String = {
        val words = model.findSynonyms(word, n).map(vec => "\""+vec._1+"\"")
        val synonyms = "{\"words\":[" + words.mkString(",") + "]}"
        return synonyms
    }
}