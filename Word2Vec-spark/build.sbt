name := "Word2Vec"

version := "0.0.1"

scalaVersion := "2.11.8"

dependencyOverrides ++= Seq(
    "io.netty" % "netty" % "3.9.9.Final",
    "commons-net" % "commons-net" % "2.2",
    "com.google.guava" % "guava" % "11.0.2"
)

libraryDependencies ++= Seq(
    "com.databricks" % "spark-xml_2.11" % "0.4.1",
    "org.apache.spark" %% "spark-core" % "2.3.1",
    "org.apache.spark" %% "spark-sql"  % "2.3.1",
    "org.apache.spark" %% "spark-mllib" % "2.0.1"
)
