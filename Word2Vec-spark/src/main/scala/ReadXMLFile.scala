package main.scala

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

class XMLReader(val sc: SparkContext, val file: String) {

    val stopwordsFile = "hdfs://localhost:9000/usr/local/stopwords.txt"
    val textRegex = "[^A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ]+"    
    
    val fXmlFile = new File(file);
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