package com.joshondesign.xml;
import java.io.*;
import java.net.URI;
import java.util.ArrayDeque;
import java.util.Deque;
import org.w3c.dom.Element;

public class XMLWriter {
    private PrintWriter out;
    private Deque<String> stack = new ArrayDeque<String>();
    private static final String ATT_INDENT = "  ";
    private static final String INDENT = "    ";
    private int indent = 0;
    private boolean elementStartIsOpen;
    private URI baseURI;
    private boolean hasAtts;
    private boolean didText;

    public XMLWriter(PrintWriter writer, URI baseURI) {
        out = writer;
        this.baseURI = baseURI;
    }

    public XMLWriter(File file) throws FileNotFoundException, UnsupportedEncodingException {
        out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
        this.baseURI = file.toURI();
    }

    public XMLWriter(PrintStream out, URI baseURI) {
        this.out = new PrintWriter(out);
        this.baseURI = baseURI;
    }


    public XMLWriter header() {
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        return this;
    }

    public void close() {
        out.close();
    }
    
    public void flush() {
        out.flush();
    }

    public XMLWriter start(String elementName, String ... ats) {
        if(elementStartIsOpen) {
            closeElementStart();
        }
        stack.push(elementName);
        tab();
        indent();
        out.print("<"+elementName);
        if(ats.length > 0) {
            hasAtts = true;
        }
        for(int i=0; i<ats.length/2; i++) {
            tab();
            out.println("" + ats[i*2] + "='" + ats[i*2+1] + "'");
        }
        elementStartIsOpen = true;
        return this;
    }

    private void indent() {
        indent++;
    }

    private void tab() {
        for(int i=0; i<indent; i++) {
            out.print(INDENT);
        }
    }

    public XMLWriter attr(String name, String value) {
        if(hasAtts) tab();
        out.println("  "+name+"='"+value+"'");
        hasAtts = true;
        return this;
    }

    private void closeElementStart() {
        if(hasAtts) {
            tab();
        }
        out.print(">");
        hasAtts = false;
        elementStartIsOpen = false;
    }

    public XMLWriter end() {
        if(elementStartIsOpen) {
            closeElementStart();
        }
        outdent();
        if(!didText) {
            tab();
        }
        out.println("</"+stack.pop()+">");
        didText = false;
        return this;
    }

    private void outdent() {
        indent--;
    }

    public URI getBaseURI() {
        return baseURI;
    }

    public XMLWriter text(String text) {
        if(elementStartIsOpen) {
            closeElementStart();
        }
        out.print(text);
        didText = true;
        return this;
    }

    public void write(Doc doc) {
        header();
        write(doc.children());
    }
    private void write(Iterable<? extends Nod> elems) {
        for(Nod n : elems) {
            if(n instanceof Elem) {
                Elem e = (Elem) n;
                start(e.name());
                for(String attrName : e.attrs()) {
                    attr(attrName, e.attr(attrName));
                }
                write(e.allChildren());
                end();
            } else {
                if(n.item.getNodeType() == Element.TEXT_NODE) {
                    text(n.item.getNodeValue());
                }
//                out.println("node! " + n.item.getNodeType() + " " + Element.TEXT_NODE);

            }
        }
    }

    private void p(String string) {
        out.println(string);
    }
}
