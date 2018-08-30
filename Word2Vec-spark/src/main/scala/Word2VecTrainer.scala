package main.scala

import java.net.URI;
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import java.nio.file.{ Files, FileSystems }
import org.apache.hadoop.fs.{Path, FileSystem};
import org.apache.spark.mllib.feature.{Word2Vec, Word2VecModel}
import org.apache.spark.mllib.linalg.{Vector => Vec, Vectors}

class Word2VecTrainer(val sc: SparkContext, val workPath: String, val modelName: String) {
    private val word2vec = new Word2Vec();
    private val modelPath = workPath + modelName
    private val model = this.load("row", "Text");

    private def load(tag: String, attr: String): Word2VecModel = {
        var inputName = "Aux.xml"
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

            val reader_sugg = new XMLReader(sc, workPath, "suggestions.xml")
            var input_sugg = sc.parallelize(reader_sugg.read("suggestion", attr))

            var model = word2vec.fit(input ++ input_sugg)
            model.save(sc, modelPath)
            return model
        }
    }

    def getSynonymsByWord(searchQuery: String, n: Int = 15): String = {
        try {
            val qry = separateWords(searchQuery, model)
            val words = model.findSynonyms(qry, n).filter(aux => !searchQuery.contains(aux._1)).map(vec => 
                "{\"text\": \""+vec._1+"\", \"value\":"+vec._2+"}"
            )
            val synonyms = "{\"words\":[" + words.mkString(",") + "]}"
            return synonyms
        } catch {
            case ex: Exception => {
                println(ex)
                return "{}"
            }
        }
    }

    def separateWords(words: String, model: Word2VecModel): Vec = {
        if (!words.contains("-") && !words.contains("_")) {
            //return model.transform("u'"+words+"''");
            return model.transform(words)
        }
        if (words.contains("-")) {
            val aux = words.split("-")
            var sum = separateWords(aux(0), model)
            (1 to aux.size - 1).foreach(i => {
                sum = sub(sum, separateWords(aux(i), model))
            })
            return sum
        } else {
            val aux = words.split("_")
            var sum = separateWords(aux(0), model)
            (1 to aux.size-1).foreach(i => {
                sum = add(sum, separateWords(aux(i), model))
            })
            return sum
        }
    }

    def add(vec1: Vec, vec2: Vec): Vec = {
        var i = 0
        val aux = (0 to vec1.size-1).map(i => vec1(i) + vec2(i)).toArray
        return Vectors.dense(aux)
    }

    def sub(vec1: Vec, vec2: Vec): Vec = {
        var i = 0
        val aux = (0 to vec1.size-1).map(i => vec1(i) - vec2(i)).toArray
        return Vectors.dense(aux)
    }

    def mul(vec1: Vec, scalar: Int): Vec = {
        var i = 0
        val aux = (0 to vec1.size-1).map(i => vec1(i) * scalar).toArray
        return Vectors.dense(aux)
    }
}