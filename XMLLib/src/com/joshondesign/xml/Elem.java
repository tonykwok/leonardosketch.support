package com.joshondesign.xml;
import org.w3c.dom.Element;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A wrapper for DOM elements.
*/
public class Elem extends Nod {
    private Element element;
//    private Doc doc;

    Elem(Doc doc, Element element) {
        super(doc,element);
        this.element = element;
//        this.doc = doc;
    }

    /**
     * Return the node name of this element
     * @return
     */
    public String name() {
        return this.element.getNodeName();
    }

    public String text() throws XPathExpressionException {
        return this.xpathString("text()");
    }

    public void setText(String text) {
        this.element.setTextContent(text);
    }

    /**
     * Execute an xpath query on this element, returning an iterable list of child elements.
     *
     * @param query
     * @return
     * @throws javax.xml.xpath.XPathExpressionException
     */
    public Iterable<? extends Elem> xpath(String query) throws XPathExpressionException {
        List<Element> elements = XMLParser.xpathElements(element,query);
        List<Elem> elems = new ArrayList<Elem>();
        for(Element e : elements) {
            elems.add(new Elem(this.doc,e));
        }
        return elems;
    }

    public String xpathString(String query) throws XPathExpressionException {
        return XMLParser.xpathString(element,query);
    }

    /**
     * Returns all the direct children of this element the specified tag name.
     * @param tagName - that tag name of the children you wish to return
     * @return
     */
    public Iterable<? extends Elem> withTag(String tagName){
        List<Elem> elems = new ArrayList<Elem>();
        NodeList nodeList = element.getElementsByTagName(tagName);
        for(int i = 0; i < nodeList.getLength(); i++){
            Elem elem = new Elem(doc, (Element)nodeList.item(i));
            elems.add(elem);
        }
        return elems;
    }

    /**
     * Return the value of the requested attribute.
     * @param name
     * @return
     */
    public String attr(String name) {
        return XMLParser.getAtt(element,name);
    }

    /**
     * Returns true if the requested attribute exists, else returns false.
     * @param name
     * @return
     */
    public boolean hasAttr(String name) {
        return element.hasAttribute(name);
    }

    /**
     * Returns true if the named attribute exists and has the same value as the value passed in. This is a short
     * hand way of safely comparing attribute values. It will never throw a NullPointerException. Usage:
     *
     * if(elem.attrEquals("valid","true")) {
     *  // do stuff
     * }
     * if(elem.attrEquals("valid","false")) {
     *  // do other stuff
     * }
     *
     * @param name
     * @param value
     * @return
     */
    public boolean attrEquals(String name, String value) {
        if(!element.hasAttribute(name)) return false;
        String v2 = element.getAttribute(name);
        if(v2 == null && value == null) return true;
        if(v2 == null || value == null) return false;
        if(value.equals(v2)) return true;
        return false;
    }

    public Doc getDoc() {
        return this.doc;
    }

    Iterable<String> attrs() {
        List<String> list = new ArrayList<String>();
        NamedNodeMap map = element.getAttributes();
        for(int i=0; i<map.getLength(); i++) {
            Node node = map.item(i);
            list.add(node.getNodeName());
        }
        return list;
    }

    Iterable<Nod> allChildren() {
        NodeList list = element.getChildNodes();
        List<Nod> elems = new ArrayList<Nod>();
        for(int i=0; i<list.getLength(); i++) {
            Node item = list.item(i);
            if(item.getNodeType() == Element.ELEMENT_NODE) {
                elems.add(new Elem(doc, (Element) item));
            } else {
                elems.add(new Nod(doc,item));
            }
        }
        return elems;
    }
    Iterable<Elem> children() {
        NodeList list = element.getChildNodes();
        List<Elem> elems = new ArrayList<Elem>();
        for(int i=0; i<list.getLength(); i++) {
            Node item = list.item(i);
            System.err.println("item = " + item);
            if(item.getNodeType() == Element.ELEMENT_NODE) {
                System.err.println("is element node");
                elems.add(new Elem(doc, (Element) item));
            }
        }
        return elems;
    }
}
