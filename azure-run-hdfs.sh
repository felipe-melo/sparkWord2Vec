stop-dfs.sh

sudo rm -R ~/data
hdfs namenode -format

start-dfs.sh
start-yarn.sh

hdfs dfs -mkdir /usr
hdfs dfs -mkdir /usr/local

hdfs dfs -put datasets/pt.stackoverflow/Comments.xml hdfs://master:9000/usr/local/Comments.xml
hdfs dfs -put datasets/stopwords.txt hdfs://master:9000/usr/local/stopwords.txt
hdfs dfs -put datasets/stackoverflowModel hdfs://master:9000/usr/local/stackoverflowModel
hdfs dfs -put datasets/suggestions.xml hdfs://master:9000/usr/local/suggestions.xml