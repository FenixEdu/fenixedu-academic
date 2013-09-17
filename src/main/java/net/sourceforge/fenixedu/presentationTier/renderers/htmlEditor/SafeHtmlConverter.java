package net.sourceforge.fenixedu.presentationTier.renderers.htmlEditor;

import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.InvalidContentPathException;
import net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal;
import net.sourceforge.fenixedu.domain.contents.Portal;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.tidy.Tidy;

import pt.ist.fenixWebFramework.renderers.plugin.RenderersRequestProcessorImpl;

public class SafeHtmlConverter extends TidyConverter {

    private static final String TIDY_PROPERTIES = "HtmlEditor-Tidy-MathJax.properties";

    /**
     * Default serial id.
     */
    private static final long serialVersionUID = 1L;

    @Override
    public String getTidyProperties() {
        return TIDY_PROPERTIES;
    }

    @Override
    protected void parseDocument(OutputStream outStream, Tidy tidy, Document document) {
        filterDocument(document);
        tidy.pprint(document, outStream);
    }

    @Override
    protected String filterOutput(String output) {
        // tidy escapes the ampersand when used with numerical entities.
        return output.replaceAll("&amp;#([0-9]+);", "&#$1;");
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
        // case Node.TEXT_NODE:
        // break;
        // default:
        // Node parent = node.getParentNode();
        // parent.removeChild(node);
        // break;
        default:
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
        switch (child.getNodeType()) {
        case Node.PROCESSING_INSTRUCTION_NODE:
        case Node.TEXT_NODE:
        case Node.COMMENT_NODE:
        case Node.DOCUMENT_TYPE_NODE:
        case Node.ENTITY_NODE:
        case Node.ENTITY_REFERENCE_NODE:
        case Node.NOTATION_NODE:
            return true;
        case Node.ELEMENT_NODE:
            // processed bellow
            break;

        case -1:
            // HACK: when xHTML is requested JTidy inserts a tag equivalent to
            // <?xml encoding='iso-8859-1' version='1.0'?>, a node with the type
            // org.w3c.tidy.Node.XML_DECL. Nevertheless, this node, answers with
            // -1 to getNodeType(). This node needs to be accepted since nothing
            // of the document is printed if this node is removed.

            NamedNodeMap attributes = child.getAttributes();
            if (attributes.getNamedItem("encoding") != null && attributes.getNamedItem("version") != null) {
                return true;
            }
        default:
            return false;
        }

        List<String> forbiddenElements =
                Arrays.asList(new String[] { "script", "iframe", "element", "applet", "form", "frame", "frameset", "link",
                        "style", "embed" });

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
        HttpServletRequest currentRequest = RenderersRequestProcessorImpl.getCurrentRequest();
        String serverName = currentRequest.getServerName();

        return serverName.equals(url.getHost()) && url.getPath().startsWith(currentRequest.getContextPath());
    }

    private boolean isThrustedAttribute(Node parent, Attr attribute) {
        String name = attribute.getName().toLowerCase();
        String value = attribute.getValue();

        List<String> eventsAttributes =
                Arrays.asList(new String[] { "onabort", "onblur", "onchange", "onclick", "ondblclick", "onerror", "onfocus",
                        "onkeydown", "onkeypress", "onkeyup", "onload", "onmousedown", "onmousemove", "onmouseout",
                        "onmouseover", "onmouseup", "onreset", "onresize", "onselect", "onsubmit", "onunload" });

        if (eventsAttributes.contains(name)) { // instrinsic events
            return false;
        }

        if (value.toLowerCase().startsWith("javascript:")) { // javascript:
            // doSomething()
            return false;
        }

        if (name.equals("class")) { // don't allow to use application styles
            String allowedClasses = filterAllowedClasses(value);
            if (allowedClasses.length() > 0) {
                attribute.setValue(allowedClasses);
                return true;
            } else {
                return false;
            }
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
                } else {
                    return true;
                }
            } catch (MalformedURLException e) {
                return false;
            }
        }

        return true;
    }

    private String filterAllowedClasses(String value) {
        StringBuilder classes = new StringBuilder();
        for (String cssClass : value.split(" ")) {
            if (cssClass.startsWith("pub-")) {
                classes.append(cssClass);
                classes.append(" ");
            }
        }
        return classes.toString();
    }

    private boolean isPrivateURL(URL url) {
        if (!isRelative(url)) {
            return false;
        }

        String path = url.getPath();

        HttpServletRequest currentRequest = RenderersRequestProcessorImpl.getCurrentRequest();
        String contextPath = currentRequest.getContextPath();

        if (path.startsWith(contextPath)) {
            path = path.substring(contextPath.length());
        }

        final String trailingPath = getTrailingPath(path);
        List<Content> contents = new ArrayList<Content>();
        try {
            Portal.getRootPortal().addPathContentsForTrailingPath(contents, trailingPath);
        } catch (InvalidContentPathException e) {
            // Actually here we ignore, since this isn't a path in the system.
            contents.clear();
        }

        int size = contents.size();
        if (size > 0) {
            Content lastContent = contents.get(size - 1);
            if (contents.iterator().next() instanceof MetaDomainObjectPortal
                    && (lastContent instanceof Section || lastContent instanceof Item)) {
                // if it's a public section or item they can deal with their own
                // availability policy hence we'll let create the link.
                return false;

            }

        }

        for (Content content : contents) {
            if (content.getAvailabilityPolicy() != null
                    && content.getAvailabilityPolicy().getTargetGroup().getClass() != EveryoneGroup.class) {
                return true;
            }
        }

        String[] forbiddenPaths = new String[] { "^/dotIstPortal\\.do.*", "^/home\\.do.*" };

        for (String forbiddenPath : forbiddenPaths) {
            if (path.matches(forbiddenPath)) {
                return true;
            }
        }

        return false;
    }

    private String getTrailingPath(final String path) {
        return path.length() > 0 && path.charAt(0) == '/' ? path.substring(1) : path;
    }

}