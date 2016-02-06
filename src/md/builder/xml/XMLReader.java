package md.builder.xml;

import md.builder.Builder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class XMLReader {

    Document document;
    Builder builder;
    XPath xPath;

    public XMLReader(Builder builder, InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {
        this.builder = builder;
        this.document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
        this.xPath = XPathFactory.newInstance().newXPath();
    }

    public void execute() throws XPathExpressionException {

        NodeList objects = (NodeList) xPath.evaluate("/objects/object[@class]", document, XPathConstants.NODESET);
        for (int objectIndex = 0; objectIndex < objects.getLength(); objectIndex++) {
            Node item = objects.item(objectIndex);
            String classpath = (String) xPath.evaluate("@class", item, XPathConstants.STRING);
            Boolean isConstructable = (Boolean) xPath.evaluate("construct", item, XPathConstants.BOOLEAN);

            if (isConstructable) {
                handleObject(classpath, item);
            } else {
                handleStaticObject(classpath, item);
            }
        }
    }

    private void handleObject(String classpath, Node item) throws XPathExpressionException {
        Node constructor = (Node) xPath.evaluate("construct", item, XPathConstants.NODE);
        String objectName = (String) xPath.evaluate("@as", constructor, XPathConstants.STRING);

        builder.constructObject(
                classpath,
                objectName,
                getArguments(constructor)
        );

        NodeList toSet = (NodeList) xPath.evaluate("set[@value and @into]", item, XPathConstants.NODESET);
        for (int i = 0; i < toSet.getLength(); i++) {
            Node field = toSet.item(i);
            String fieldName = (String) xPath.evaluate("@into", field, XPathConstants.STRING);
            String value = (String) xPath.evaluate("@value", field, XPathConstants.STRING);
            builder.setField(objectName, fieldName, value);
        }

        NodeList methodCalls = (NodeList) xPath.evaluate("call[@method]", item, XPathConstants.NODESET);
        for (int i = 0; i < methodCalls.getLength(); i++) {
            Node methodCall = methodCalls.item(i);
            builder.callMethod(
                    objectName,
                    methodCall.getAttributes().getNamedItem("method").getTextContent(),
                    getArguments(methodCall)
            );
        }
    }

    private void handleStaticObject(String classpath, Node item) throws XPathExpressionException {
        NodeList fields = (NodeList) xPath.evaluate("set[@value and @into]", item, XPathConstants.NODESET);
        for (int i = 0; i < fields.getLength(); i++) {
            Node field = fields.item(i);
            String fieldName = (String) xPath.evaluate("@into", field, XPathConstants.STRING);
            String value = (String) xPath.evaluate("@value", field, XPathConstants.STRING);
            builder.setStaticField(classpath, fieldName, value);
        }


        NodeList methodCalls = (NodeList) xPath.evaluate("call[@method]", item, XPathConstants.NODESET);
        for (int i = 0; i < methodCalls.getLength(); i++) {
            Node methodCall = methodCalls.item(i);
            builder.callStaticMethod(
                    classpath,
                    methodCall.getAttributes().getNamedItem("method").getTextContent(),
                    getArguments(methodCall)
            );
        }
    }

    private String[] getArguments(Node item) throws XPathExpressionException {
        ArrayList<String> result = new ArrayList<>();
        NodeList args = (NodeList) xPath.evaluate("arg", item, XPathConstants.NODESET);
        for (int i = 0; i < args.getLength(); i++) {
            Node arg = args.item(i);
            result.add(arg.getTextContent());
        }
        return result.toArray(new String[result.size()]);
    }
}
