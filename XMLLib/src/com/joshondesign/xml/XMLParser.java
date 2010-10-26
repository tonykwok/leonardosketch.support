package com.joshondesign.xml;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.*;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.InputSource;

/**
 * A utility class which does DOM parsing and xpath queries through a compact and easy to use API, utilizing
 * modern Java language facilities like Iterable, var args, and shortened names.
 */
public class XMLParser {

    /**
     * Parse the input stream into an DOM.
     * 
     * @param in
     * @return
     * @throws Exception
     */
    public static Doc parse(InputStream in) throws Exception {
        Document doc = loadDocument(in);
        return new Doc(doc);
    }

    public static Doc parse(File file) throws Exception {
        Document docx = loadDocument(new FileInputStream(file));
        Doc doc = new Doc(docx);
        doc.setBaseURI(file.toURI());
        return doc;
    }

    public static Doc parse(Document document) {
        return new Doc(document);
    }
    
    /**
     * Parses XML from a string.
     * @param xml - a String containing XML
     * @return a Doc loaded with the content from the string.
     * @throws Exception
     */
    public static Doc parse(String xml) throws Exception{
        Document doc = loadDocument(new ByteArrayInputStream(xml.getBytes()));
        return new Doc(doc);
    }
    
    public static void dump(Object obj) {
        if(obj instanceof Node) { dump((Node)obj); return; }
        if(obj instanceof NodeList) { dump((NodeList)obj); return; }
        if(obj instanceof Document) { dump((Document)obj); return; }
        p(obj);
    }

    private static void p(Object obj) {
        System.out.println(""+obj);
    }
    private static void pr(Object obj) {
        System.out.print(""+obj);
    }



    private static Document loadDocument(InputStream in) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        MyErrorHandler eh = new MyErrorHandler();
        builder.setErrorHandler(eh);
        //u.p("before"+Thread.currentThread());
        Document doc = builder.parse(in);
        //u.p("after" + Thread.currentThread());
        //u.p("ex = " + eh.err);
        if(eh.err != null) {
            //u.p("throwing");
            throw eh.err;
        }
        return doc;
    }
    static String xpathString(Node node, String xpath) throws XPathExpressionException {
        XPath xp = XPathFactory.newInstance().newXPath();
        return (String) xp.evaluate(xpath, node, XPathConstants.STRING);
    }
    
    static String getAtt(Element node, String name) {
        return node.getAttribute(name);
    }

    static List<Element> xpathElements(Node node, String xpath) throws XPathExpressionException {
        XPath xp = XPathFactory.newInstance().newXPath();

        NodeList nl = (NodeList) xp.evaluate(xpath, node, XPathConstants.NODESET);
        List<Element> list = new ArrayList<Element>();
        for(int i=0; i<nl.getLength(); i++) {
            list.add((Element) nl.item(i));
        }
        return list;
    }
    
    static Element xpathElement(Document document, String query) throws XPathExpressionException {
        XPath xp = XPathFactory.newInstance().newXPath();
        Element el = (Element) xp.evaluate(query, document, XPathConstants.NODE);
        return  el;
    }


    static void dump(Node node) {
        dump(node, "");
    }

    /**
     *  Description of the Method
     *
     *@param  doc  Description of the Parameter
     */
    static void dump(Document doc) {
        p("as document");
        p("<?xml version=\"1.0\"?>");
        DocumentType dt = doc.getDoctype();
        if (dt != null) {
            p("<!DOCTYPE " + dt.getName() + " PUBLIC \"" +
                dt.getPublicId() + "\" \"" +
                dt.getSystemId() + ">");
        }
        dump((Node) doc);
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of the Parameter
     */
    static void dump(Node node, String tab) {
        if (node == null) {
            return;
        }
        //u.p(tab + "node type = " + node.getNodeType());
        if (node.getNodeType() == node.ENTITY_NODE) {
            p(tab + "entity node");
        }

        if (node.getNodeType() == node.ENTITY_REFERENCE_NODE) {
            p(tab + "------ entity ref node");
            p(tab + "ent: " + node.getNodeName());
            p(tab + node.getNodeValue());
            NodeList c = node.getChildNodes();
            Node n = c.item(0);
            //u.print_as_bytes(n.getNodeValue());
            //n.setNodeValue("blahfoo");
        }

        if (node.getNodeType() == node.TEXT_NODE) {
            if (!node.getNodeValue().trim().equals("")) {
                p(tab + node.getNodeValue());
            }
        }

        if (node.getNodeType() == node.ELEMENT_NODE) {
            NamedNodeMap atts = node.getAttributes();
            NodeList c = node.getChildNodes();
            pr(tab + "<" + node.getNodeName());

            if (atts.getLength() == 0) {
                if (c.getLength() == 0) {
                    p("/>");
                } else {
                    p(">");
                }
            }

            if (atts.getLength() == 1) {
                pr(" " + atts.item(0));
                if (c.getLength() == 0) {
                    p("/>");
                } else {
                    p(tab + ">");
                }
            }

            if (atts.getLength() > 1) {
                // print the attributes
                p(" " + atts.item(0));
                for (int i = 1; i < atts.getLength(); i++) {
                    p(tab + "  " + atts.item(i));
                }
                if (c.getLength() == 0) {
                    p(tab + "/>");
                } else {
                    p(tab + ">");
                }
            }
        }

        NodeList c = node.getChildNodes();
        for (int i = 0; i < c.getLength(); i++) {
            Node n = c.item(i);
            dump(n, tab + "  ");
        }

        if ((node.getNodeType() == node.ELEMENT_NODE) && (node.getChildNodes().getLength() > 0)) {
            p(tab + "</" + node.getNodeName() + ">");
        }
    }


    /**
     *  Description of the Method
     *
     *@param  node  Description of the Parameter
     *@param  pw    Description of the Parameter
     */
    static void dump(Node node, PrintWriter pw) {
        pw.println("name=" + node.getNodeName() + "<br>");
        NodeList c = node.getChildNodes();
        for (int i = 0; i < c.getLength(); i++) {
            Node n = c.item(i);
            dump(n, pw);
        }
    }
    /**
     *  Description of the Method
     *
     *@param  list  Description of the Parameter
     */
    static void dump(NodeList list) {
        p("node list: " + list.toString());
        p("size = " + list.getLength());
        for (int i = 0; i < list.getLength(); i++) {
            dump(list.item(i));
        }
    }

    public static Doc translate(Doc doc, URI toURI) throws TransformerConfigurationException, MalformedURLException, IOException, TransformerException {
        Transformer xslt = TransformerFactory.newInstance().newTransformer(new StreamSource(toURI.toURL().openStream()));
        DOMResult result = new DOMResult();
        xslt.transform(new DOMSource(doc.document), result);
        return new Doc((Document) result.getNode());
    }
    
    public static void translateToFile(Doc doc, URI toURI, File out) throws TransformerConfigurationException, MalformedURLException, IOException, TransformerException {
        Transformer xslt = TransformerFactory.newInstance().newTransformer(new StreamSource(toURI.toURL().openStream()));
        StreamResult result = new StreamResult(new FileOutputStream(out));
        xslt.transform(new DOMSource(doc.document), result);        
        //return new Doc((Document) result.getNode());
    }

    private static class MyErrorHandler implements ErrorHandler {
        public Exception err;
        public void error(SAXParseException exception) {
            p("MyErrorHandler: error:");
            p(exception);
            err = exception;
        }

        public void fatalError(SAXParseException exception) {
            p("MyErrorHandler: fatal error:");
            p(exception);
            err = exception;
            p("set exception");
        }

        public void warning(SAXParseException exception) {
            p("MyErrorHandler: warning:");
            p(exception);
            //err = exception;
        }
    }
}
