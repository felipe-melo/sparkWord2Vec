stop-dfs.sh

#delete temp files
sudo rm -R /tmp/*

#HADOOP hdfs
hdfs namenode -format

start-dfs.sh

hdfs dfs -mkdir /usr

hdfs dfs -mkdir /usr/local

hdfs dfs -put ../datasets/ptwiktionary-latest-pages-articles.xml hdfs://localhost:9000/usr/local/ptwiktionary-latest-pages-articles.xml
#HADOOP hdfs

hdfs dfs -put ../datasets/stopwords.txt hdfs://localhost:9000/usr/local/stopwords.txt