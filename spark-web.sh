#complile project and generate .jar
(cd Word2Vec-spark && sbt package)
#run webserver class
(cd Word2Vec-spark && sbt "runMain main.scala.WebServer /home/cooper/Desenvolvimento/ufrj/Word2Vec/datasets/")
