stop-dfs.sh

#delete temp files
sudo rm -R /tmp/*

#HADOOP hdfs
hdfs namenode -format
start-dfs.sh

hdfs dfs -mkdir /usr

hdfs dfs -mkdir /usr/local

hdfs dfs -put datasets/pt.stackoverflow/Comments.xml hdfs://localhost:9000/usr/local/Comments.xml
#HADOOP hdfs

hdfs dfs -put datasets/stopwords.txt hdfs://localhost:9000/usr/local/stopwords.txt 