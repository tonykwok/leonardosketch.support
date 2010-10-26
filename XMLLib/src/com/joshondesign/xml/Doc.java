package com.joshondesign.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.xpath.XPathExpressionException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
* Created by IntelliJ IDEA.
* User: joshmarinacci
* Date: Jun 24, 2010
* Time: 10:40:18 AM
* To change this template use File | Settings | File Templates.
*/
public class Doc {
    Document document;
    private URI baseURI;

    Doc(Document doc) {
        this.document = doc;
    }

    /**
     * Execute an xpath query on this document, returning an iterable list of child elements.
     *
     * @param query
     * @return
     * @throws javax.xml.xpath.XPathExpressionException
     */
    public Iterable<? extends Elem> xpath(String query) throws XPathExpressionException {
        List<Element> elements = XMLParser.xpathElements(document,query);
        List<Elem> elems = new ArrayList<Elem>();
        for(Element e : elements) {
            elems.add(new Elem(this,e));
        }
        return elems;
    }

    public void setBaseURI(URI uri) {
        this.baseURI = uri;
    }

    public URI getBaseURI() {
        return baseURI;
    }

    public Elem root(){
        return new Elem(this, document.getDocumentElement());
    }

    public void dump() {
        XMLParser.dump(document);
    }

    public String xpathString(String query) throws XPathExpressionException {
        return XMLParser.xpathString(document,query);
    }

    public Elem xpathElement(String query) throws XPathExpressionException {
        return new Elem(this,XMLParser.xpathElement(document, query));
    }

    Iterable<Elem> children() {
        NodeList list = document.getChildNodes();
        List<Elem> elems = new ArrayList<Elem>();
        for(int i=0; i<list.getLength(); i++) {
            Node item = list.item(i);
            if(item.getNodeType() == Document.ELEMENT_NODE) {
                elems.add(new Elem(this, (Element) item));
            }
        }
        return elems;
    }

}
