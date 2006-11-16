package net.sourceforge.fenixedu.presentationTier.renderers.htmlEditor;

import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.renderers.plugin.RenderersRequestProcessor;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.tidy.Tidy;

public class SafeHtmlConverter extends TidyConverter {

    @Override
    protected void parseDocument(OutputStream outStream, Tidy tidy, Document document) {
        filterDocument(document);
        tidy.pprint(document, outStream);
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
                "element", "applet", "form", "frame", "frameset", "link", "style" });

        Element element = (Element) child;
        String name = element.getNodeName().toLowerCase();

        if (forbiddenElements.contains(name)) {
            return false;
        }

        if (name.equals("img")) {
            String source = element.getAttribute("src");
            
            try {
                URL url = new URL(source);
                if (isPrivateURL(url)) {
                    return false;
                }
            } catch (MalformedURLException e) {
                return false;
            }
                        
            element.removeAttribute("longdesc");
            element.removeAttribute("usemap");
            element.removeAttribute("ismap");
        }
        
        return true;
    }

    private boolean isRelative(URL url) {
        HttpServletRequest currentRequest = RenderersRequestProcessor.getCurrentRequest();
        String serverName = currentRequest.getServerName();

        return serverName.equals(url.getHost()) && url.getPath().startsWith(currentRequest.getContextPath());
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
            try {
                URL url = new URL(value);
                
                if (isPrivateURL(url)) {
                    NodeList list = parent.getChildNodes();
                    for (int i = 0; i < list.getLength(); i++) {
                        Node node = list.item(i);

                        parent.removeChild(node);
                    }

                    Text newNode = parent.getOwnerDocument().createTextNode(value);
                    Node linkParent = parent.getParentNode();
                    linkParent.replaceChild(newNode, parent);
                    
                    return false;
                }
                else {
                    return true;
                }
            } catch (MalformedURLException e) {
                return false;
            }
        }
        
        return true;
    }

    private boolean isPrivateURL(URL url) {
        if (! isRelative(url)) {
            return false;
        }
        
        String path = url.getPath();

        HttpServletRequest currentRequest = RenderersRequestProcessor.getCurrentRequest();
        String contextPath = currentRequest.getContextPath();
        
        if (path.startsWith(contextPath)) {
            path = path.substring(contextPath.length());
        }
        
        for (Role role : RootDomainObject.getInstance().getRoles()) {
            if (path.startsWith(role.getPortalSubApplication())) {
                return true;
            }
        }
        
        String[] forbiddenPaths = new String[] {
                "^/dotIstPortal\\.do.*",
                "^/home\\.do.*"
        };
        
        for (String forbiddenPath : forbiddenPaths) {
            if (path.matches(forbiddenPath)) {
                return true;
            }
        }

        return false;
    }
    
}