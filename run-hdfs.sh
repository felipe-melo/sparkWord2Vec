stop-dfs.sh

#delete temp files
sudo rm -R /tmp/*

#HADOOP hdfs
hdfs namenode -format

start-dfs.sh

hdfs dfs -mkdir /user

hdfs dfs -mkdir /user/felipemelo

hdfs dfs -put ../datasets/books.xml hdfs://localhost:9000/user/felipemelo/books.xml
#HADOOP hdfs