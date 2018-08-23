package main.scala

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object WebServer {
    def main(args: Array[String]) {
        val trainer = loadModel()

        implicit val system = ActorSystem("my-system")
        implicit val materializer = ActorMaterializer()
        // needed for the future flatMap/onComplete in the end
        implicit val executionContext = system.dispatcher

        val syms = trainer.getSynonymsByWord("mac")
        syms.foreach(println)

        val route =
            path("hello") {
                get {
                    complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
                }
            }

        val bindingFuture = Http().bindAndHandle(route, "localhost", 8001)

        println(s"Server online at http://localhost:8001/hello\nPress RETURN to stop...")
        StdIn.readLine() // let it run until user presses return
        bindingFuture
            .flatMap(_.unbind()) // trigger unbinding from the port
            .onComplete(_ => system.terminate()) // and shutdown when done
    }

    def loadModel(): Word2VecTrainer = {
        val conf = new SparkConf()
            .setAppName("Word2Vec")
            .setMaster("local[1]")
            .set("spark.executor.memory", "1g");
        val sc = new SparkContext(conf)

        val modelPath = "/home/cooper/Desenvolvimento/ufrj/Word2Vec/datasets/"
        val modelName = "stackoverflowModel"

        return new Word2VecTrainer(sc, modelPath, modelName)
    }
}