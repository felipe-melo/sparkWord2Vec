#stop-master.sh

#stop-slaves.sh

#start-master.sh

#start slave node
#start-slave.sh spark://$(hostname):7077

#complile project and generate .jar
(cd Word2Vec-spark && sbt package)

#submit job passingo 2 parameters
#first is the folder where modelo and stopwords file are
#second is the namo of the model

spark-submit \
--class "main.scala.Main" \
--deploy-mode cluster Word2Vec-spark/target/scala-2.11/word2vec_2.11-0.0.1.jar \
hdfs://master:9000/usr/local/ stackoverflowModel
#~/sparkWord2Vec/datasets/ stackoverflowModel
#hdfs://localhost:9000/usr/local/ stackoverflowModel