package main.scala

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.{File, FileWriter, FileInputStream, InputStream};
import java.net.URI;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;

class XMLReader(val sc: SparkContext, val workPath: String, val file: String) {

    val stopwordsFile = workPath + "stopwords.txt"
    val textRegex = "[^A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ]+"
    val path = workPath + file

    var fXmlFile: InputStream = null

    if (workPath.contains("hdfs")) {
        val fs = FileSystem.get(URI.create(workPath + file), new Configuration())
        fXmlFile = fs.open(new Path(path))
    } else {
        println(path)
        fXmlFile = new FileInputStream(new File(path))
    }

    val dbFactory = DocumentBuilderFactory.newInstance();
    val dBuilder = dbFactory.newDocumentBuilder();
    val doc = dBuilder.parse(fXmlFile);

    doc.getDocumentElement().normalize();

    def read (tagName: String, attrName: String): Array[Seq[String]] = {
        val nList = doc.getElementsByTagName(tagName);
        var rows: Array[Seq[String]] = new Array[Seq[String]](nList.getLength());

        var stopwords = sc.textFile(stopwordsFile)
            .flatMap(line => line.split(" "))
            .collect
            .toSet

        for (i <- 0 to nList.getLength() - 1) {
            val nNode = nList.item(i);
            val eElement = nNode.asInstanceOf[Element];
            val text = eElement.getAttribute(attrName);
            val cleanedText = text.toLowerCase
                .replaceAll(textRegex, " ")
                .split(" ")
                .toSeq
                .filter(!stopwords.contains(_))
            rows(i) = cleanedText;
        }
        rows
    }
}

class XMLWriter(val sc: SparkContext, val workPath: String, val file: String) {
    val path = workPath + file;
    //val fXmlFile = new File(path);

    var fXmlFile: InputStream = null

    if (workPath.contains("hdfs")) {
        val fs = FileSystem.get(URI.create(workPath + file), new Configuration())
        fXmlFile = fs.open(new Path(path))
    } else {
        println(path)
        fXmlFile = new FileInputStream(new File(path))
    }

    val dbFactory = DocumentBuilderFactory.newInstance();
    val dBuilder = dbFactory.newDocumentBuilder();
    val doc = dBuilder.parse(fXmlFile);

    doc.getDocumentElement().normalize();    

    def write (tagName: String, value: String) = {
        val root = doc.getDocumentElement()
        val suggestions = root.asInstanceOf[Element]

        val newSuggestion = doc.createElement("suggestion")
        newSuggestion.setAttribute("Text", value)

        suggestions.appendChild(newSuggestion)

        val transformerFactory = TransformerFactory.newInstance()
        val transformer = transformerFactory.newTransformer()
        val source = new DOMSource(doc)

        val result = new StreamResult(path)
        transformer.transform(source, result)
    }
}