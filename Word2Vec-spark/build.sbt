name := "Word2Vec"

version := "0.0.1"

scalaVersion := "2.11.8"

dependencyOverrides ++= Seq(
    "io.netty" % "netty" % "3.9.9.Final",
    "commons-net" % "commons-net" % "2.2",
    "com.google.code.findbugs" % "jsr305" % "3.0.2",
    "com.google.guava" % "guava" % "11.0.2"
)

libraryDependencies ++= {
    val akkaV = "2.4.2"
    val sparkV = "2.3.1"

    Seq(
        "org.apache.spark" %% "spark-sql"  % sparkV,
        "org.apache.spark" %% "spark-mllib" % sparkV,
        "org.apache.spark" %% "spark-core" % sparkV,
        "com.typesafe.akka" %% "akka-http"   % "10.1.4",
        "com.typesafe.akka" %% "akka-stream" % "2.5.12"
    )
}