stop-master.sh

stop-slaves.sh

#start master node
start-master.sh

#start slave node
start-slave.sh spark://$(hostname):7077

#complile project and generate .jar
(cd Word2Vec-spark && sbt package)

#submit job passingo 2 parameters
spark-submit --driver-memory 5g --packages com.databricks:spark-xml_2.11:0.4.1 \
--class "main.scala.Main" \
--master spark://$(hostname):7077 Word2Vec-spark/target/scala-2.11/word2vec_2.11-0.0.1.jar \
hdfs://localhost:9000/usr/local \
ptwiktionary-latest-pages-articles.xml 
# hdfs://localhost:9000/usr/local \
# stopwords.txt