package main.scala

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._

import java.nio.file.{ Files, FileSystems }

import org.apache.spark.mllib.feature.{Word2Vec, Word2VecModel}

class Word2VecTrainer(val sc: SparkContext, val modelPath: String) {
    private val inputName = "../datasets/pt.stackoverflow.com/Comments.xml";
    private val word2vec = new Word2Vec();
    private val model = this.load(inputName, "row", "Text");

    private def load(input: String, tag: String, attr: String): Word2VecModel = {
        val defaultFS = FileSystems.getDefault();
        val path = defaultFS.getPath(modelPath);
        val reader = new XMLReader(sc, inputName);

        if (Files.exists(path)) {
            return Word2VecModel.load(sc, "file://" + modelPath);
        } else {
            var input = sc.parallelize(reader.read(tag, attr));
            var model = word2vec.fit(input);
            model.save(sc, "file://" + modelPath);
            return model;
        }
    }

    def getSynonymsByWord(word: String, n: Int = 10): Array[String] = {
        model.findSynonyms(word, n).map(vec => (vec._1));
    }
}