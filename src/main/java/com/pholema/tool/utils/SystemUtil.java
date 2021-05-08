package com.pholema.tool.utils;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.pholema.tool.utils.dynamic.CustomizeProperties;
import com.pholema.tool.utils.dynamic.PropertiesUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

public class SystemUtil {

    private static Logger logger = Logger.getLogger(SystemUtil.class);

    public static String getAppName() {
        try {
            String location = PropertiesUtils.properties.getProperty("pom.home");
            logger.info("location " + PropertiesUtils.properties.getProperty("pom.home"));
            if (location != null && location.endsWith(".xml")) {
                InputStream is = new FileInputStream(location);
                DocumentBuilderFactory xmlFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = xmlFactory.newDocumentBuilder();
                Document xmlDoc = docBuilder.parse(is);
                XPathFactory xpathFact = XPathFactory.newInstance();
                XPath xpath = xpathFact.newXPath();
                return (String) xpath.evaluate("/project/artifactId", xmlDoc, XPathConstants.STRING);
            } else if (location != null && location.endsWith(".properties")) {
                CustomizeProperties properties = new CustomizeProperties();
                properties.load(new FileInputStream(location));
                return properties.getProperty("artifactId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "appName";
    }

    public static String getVersion() {
        try {
            String location = PropertiesUtils.properties.getProperty("pom.home");
            if (location.endsWith(".xml")) {
                InputStream is = new FileInputStream(location);
                DocumentBuilderFactory xmlFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = xmlFactory.newDocumentBuilder();
                Document xmlDoc = docBuilder.parse(is);
                XPathFactory xpathFact = XPathFactory.newInstance();
                XPath xpath = xpathFact.newXPath();
                return (String) xpath.evaluate("/project/version", xmlDoc, XPathConstants.STRING);
            } else if (location.endsWith(".properties")) {
                CustomizeProperties properties = new CustomizeProperties();
                properties.load(new FileInputStream(location));
                return properties.getProperty("version");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "appName";
    }

    public static void printWriterLog(Exception e, org.apache.log4j.Logger logger) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        logger.error(sw.toString());
    }
}
