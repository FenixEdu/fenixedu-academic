package net.sourceforge.fenixedu.presentationTier.renderers.htmlEditor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.renderers.components.converters.ConversionException;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.plugin.RenderersRequestProcessor;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;
import org.w3c.tidy.TidyMessage;
import org.w3c.tidy.TidyMessageListener;

public class SafeHtmlConverter extends Converter {

    public static final String TIDY_PROPERTIES = "HtmlEditor-Tidy.properties";

    @Override
    public Object convert(Class type, Object value) {
        String htmlText = (String) value;

        if (htmlText == null) {
            return null;
        }

        ByteArrayInputStream inStream = new ByteArrayInputStream(htmlText.getBytes());
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        Tidy tidy = createTidyParser();

        TidyErrorsListener errorListener = new TidyErrorsListener();
        tidy.setMessageListener(errorListener);

        Document document = tidy.parseDOM(inStream, null);

        if (errorListener.isBogus()) {
            throw new ConversionException("renderers.converter.safe.invalid");
        }

        filterDocument(document);
        tidy.pprint(document, outStream);

        return new String(outStream.toByteArray());
    }

    private Tidy createTidyParser() {
        Tidy tidy = new Tidy();

        Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream(TIDY_PROPERTIES));
        } catch (IOException e) {
            e.printStackTrace();
        }

        tidy.setConfigurationFromProps(properties);

        return tidy;
    }

    private void filterDocument(Node node) {
        switch (node.getNodeType()) {
        case Node.DOCUMENT_NODE:
            filterChildren(node);
            break;
        case Node.ELEMENT_NODE:
            Element element = (Element) node;

            // remove all attributes
            NamedNodeMap attributes = element.getAttributes();
            for (int i = 0; i < attributes.getLength(); i++) {
                Attr attribute = (Attr) attributes.item(i);

                if (!isThrustedAttribute(element, attribute)) {
                    element.removeAttribute(attribute.getNodeName());
                }
            }

            // filter children
            filterChildren(element);
            break;
        case Node.TEXT_NODE:
            break;
        default:
            Node parent = node.getParentNode();
            parent.removeChild(node);
            break;
        }
    }

    private void filterChildren(Node node) {
        List<Node> childrenList = new ArrayList<Node>();

        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            childrenList.add(children.item(i));
        }

        for (Node child : childrenList) {
            if (isThustedNode(child)) {
                filterDocument(child);
            } else {
                node.removeChild(child);
            }
        }
    }

    private boolean isThustedNode(Node child) {
        if (child.getNodeType() != Node.ELEMENT_NODE && child.getNodeType() != Node.TEXT_NODE) {
            return false;
        }

        if (child.getNodeType() == Node.TEXT_NODE) {
            return true;
        }

        List<String> forbiddenElements = Arrays.asList(new String[] { "script", "iframe",
                "element", "object", "applet", "form", "frame", "frameset", "link", "style" });

        Element element = (Element) child;
        String name = element.getNodeName().toLowerCase();

        if (forbiddenElements.contains(name)) {
            return false;
        }

        if (name.equals("img")) {
            String source = element.getAttribute("src");
                    
            if (! isRelative(source)) {
                return false;
            }
            
            element.removeAttribute("longdesc");
            element.removeAttribute("usemap");
            element.removeAttribute("ismap");
        }
        
        return true;
    }

    private boolean isRelative(String uri) {
        return ! (uri.startsWith("http://") || uri.startsWith("ftp://") || uri.startsWith("mailto:"));
    }
    
    private boolean isThrustedAttribute(Node parent, Attr attribute) {
        String name = attribute.getName().toLowerCase();
        String value = attribute.getValue();

        List<String> eventsAttributes = Arrays.asList(new String[] { "onabort", "onblur", "onchange",
                "onclick", "ondblclick", "onerror", "onfocus", "onkeydown", "onkeypress", "onkeyup",
                "onload", "onmousedown", "onmousemove", "onmouseout", "onmouseover", "onmouseup",
                "onreset", "onresize", "onselect", "onsubmit", "onunload" });

        if (eventsAttributes.contains(name)) { // instrinsic events
            return false;
        }

        if (value.toLowerCase().startsWith("javascript:")) { // javascript: doSomething()
            return false;
        }

        if (name.equals("class")) { // don't allow to use application styles
            return false;
        }

        if (name.equals("href")) { 
            if (! isRelative(value)) { // protect links to private places of the application
                HttpServletRequest currentRequest = RenderersRequestProcessor.getCurrentRequest();
                String serverName = currentRequest.getServerName();
                
                try {
                    URL url = new URL(value);
                    if (serverName.equals(url.getHost())) {
                        if (url.getPath().contains("/publico/")) {
                            return true;
                        }
                        else {
                            NodeList list = parent.getChildNodes();
                            for (int i = 0; i<list.getLength(); i++) {
                                Node node = list.item(i);
                                
                                parent.removeChild(node);
                            }
                            
                            parent.appendChild(parent.getOwnerDocument().createTextNode(value));
                            return false;
                        }
                    }
                    
                    return true;
                } catch (MalformedURLException e) {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        
        return true;
    }

    class TidyErrorsListener implements TidyMessageListener {

        boolean bogus;

        public boolean isBogus() {
            return this.bogus;
        }

        public void setBogus(boolean bogus) {
            this.bogus = bogus;
        }

        public void messageReceived(TidyMessage message) {
            if (message.getLevel().equals(TidyMessage.Level.ERROR)) {
                setBogus(true);
            }
        }

    }
}