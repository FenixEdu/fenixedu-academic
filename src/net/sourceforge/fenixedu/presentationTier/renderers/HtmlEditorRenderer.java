package net.sourceforge.fenixedu.presentationTier.renderers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.presentationTier.renderers.components.HtmlEditor;
import net.sourceforge.fenixedu.renderers.InputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlSubmitButton;
import net.sourceforge.fenixedu.renderers.components.HtmlTextArea;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;
import org.w3c.tidy.TidyMessage;
import org.w3c.tidy.TidyMessageListener;

import com.thoughtworks.xstream.converters.ConversionException;

/**
 * An javascript html editor for doing the input of html text.
 * This renderer abstracts the javascript html editor available in
 * Fenix and binds it to the slot beeing edited.
 * 
 * @author cfgi
 */
public class HtmlEditorRenderer extends InputRenderer {
    
    public static final String TIDY_PROPERTIES = "HtmlEditor-Tidy.properties";

    private int columns;
    private int rows;
    private int heigth;
    private int width;
    
    private boolean safe;
    
    public int getColumns() {
        return this.columns;
    }

    /**
     * The number of columns of the fall back textarea, that
     * is, the text area that is shown when the html editor is not
     * supported by the browser.
     * 
     * @property
     */
    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRows() {
        return this.rows;
    }

    /**
     * The number of rows of the fall back textarea.
     * 
     * @property
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getHeigth() {
        return this.heigth;
    }

    /**
     * The height of the html editor.
     * 
     * @property
     */
    public void setHeigth(int heigth) {
        this.heigth = heigth;
    }

    public int getWidth() {
        return this.width;
    }

    /**
     * The width of thehtml editor.
     * 
     * @property
     */
    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isSafe() {
        return this.safe;
    }

    /**
     * If this property is set to <tt>true</tt> then the input will be filtered and any
     * unsupported HTML will be removed or escaped to the corresponding entities. The idea
     * is that a piece of code like
     * <pre>
     * &lt;p onmouseover=&quot;doSomething();&quot;&gt;
     *     &lt;table style="..."/&gt;
     * &lt;/p&gt;
     * </pre>
     * 
     * would be saved as
     * 
     * <pre>
     * &lt;p&gt;
     *     &lt;table&gt;&lt;/table&gt;
     * &lt;/p&gt;
     * </pre>
     * 
     * @property
     */
    public void setSafe(boolean safe) {
        this.safe = safe;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new EditorLayout();
    }

    private class EditorLayout extends Layout {

        private boolean isBrowserSupported() {
            String[] notSupported = { "safari", "konqueror", "opera" }; 
            
            HttpServletRequest request = getInputContext().getViewState().getRequest();
            
            String userAgent = request.getHeader("User-Agent");
            if (userAgent == null) {
                return false;
            }
            
            userAgent = userAgent.toLowerCase();
            for (int i = 0; i < notSupported.length; i++) {
                String id = notSupported[i];
                
                if (userAgent.indexOf(id) != -1) {
                    return false;
                }
            }
            
            return true;
        }
        
        @Override
        public HtmlComponent createComponent(Object object, Class type) {
            // TODO: cfgi, rich text editor also verifies browser so is this needed?
            if (! isBrowserSupported()) {
                return createTextArea(object, type);
            }
            else {
                return createEditor(object, type);
            }
        }

        private HtmlComponent createEditor(Object object, Class type) {
            HtmlInlineContainer container = new HtmlInlineContainer();

            HtmlEditor editor = new HtmlEditor();
            
            editor.setWidth(getWidth());
            editor.setHeigth(getHeigth());
            
            editor.setValue((String) object);
            editor.setTargetSlot((MetaSlotKey) getInputContext().getMetaObject().getKey());
            
            if (isSafe()) {
                editor.setConverter(new SafeHtmlConverter());
            }
            
            HtmlSubmitButton submitButton = getInputContext().getForm().getSubmitButton();
            
            String currentScript = submitButton.getOnClick();
            submitButton.setOnClick("updateRTE('" + editor.getName() + "');" + (currentScript == null ? "" : currentScript));
            
            container.addChild(editor);
            return container;
        }

        private HtmlComponent createTextArea(Object object, Class type) {
            HtmlTextArea textArea = new HtmlTextArea();
            textArea.setTargetSlot((MetaSlotKey) getInputContext().getMetaObject().getKey());
            
            if (isSafe()) {
                textArea.setConverter(new SafeHtmlConverter());
            }

            textArea.setValue((String) object);
            
            textArea.setColumns(getColumns());
            textArea.setRows(getRows());
            
            return textArea;
        }
    }
    
    private static class SafeHtmlConverter extends Converter {

        @Override
        public Object convert(Class type, Object value) {
            String htmlText = (String) value;
            
            if (htmlText == null) {
                return null;
            }
            
            ByteArrayInputStream inStream   = new ByteArrayInputStream(htmlText.getBytes());
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
                properties.load(getClass().getResourceAsStream(HtmlEditorRenderer.TIDY_PROPERTIES));
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
                for (int i=0; i<attributes.getLength(); i++) {
                    Attr attribute = (Attr) attributes.item(i);
                    
                    if (! isThrustedAttribute(element, attribute)) {
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
            for (int i=0; i<children.getLength(); i++) {
                childrenList.add(children.item(i));
            }
            
            for (Node child : childrenList) {
                if (isThustedNode(child)) {
                    filterDocument(child);
                }
                else {
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
            
            List<String> forbiddenElements = Arrays.asList(new String[] { "script", "iframe", "img",
                    "element", "object", "applet", "form", "frame", "frameset", "link", "style" });
            
            Element element = (Element) child;
            if (forbiddenElements.contains(element.getNodeName().toLowerCase())) {
                return false;
            }
            
            return true;
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
            
            if (value.toLowerCase().startsWith("javascript")) { // javascript: doSomething()
                return false;
            }
            
            if (name.equals("href")) { // avoid relative urls
                if (! (value.startsWith("http://") || 
                       value.startsWith("ftp://")  ||
                       value.startsWith("mailto:"))) {
                    return false;
                }
                else { // make the url explicit and not clickable
                    Document document = parent.getOwnerDocument();
                    Node node = document.createTextNode(value);
                    
                    parent.getParentNode().replaceChild(node, parent);
                    return true;
                }
            }

            if (name.equals("class") || name.equals("style")) { // remove css
                return false;
            }
            
            if (name.equals("src")) { // avoid any sourcing of documents
                return false;
            }
            
            return true;
        }

        private class TidyErrorsListener implements TidyMessageListener {

            private boolean bogus;
            
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
    
//    public static void main(String[] args) {
//        SafeHtmlConverter converter = new SafeHtmlConverter();
//
//        String value = (String) converter.convert(null, 
//                "<p style=\"border: 1px dotted black;\">" +
//                "  <span class=\"testing\">ola</span>\n"    +
//                " <font size=\"+2\" family=\"monospace\">bla bla</font>" +
//                "  <span>olé</span>\n" +
//                "  <a href=\"www.google.pt\">testing</a> xxxxx" +
//                "</p>"
//                );
//        System.out.println(value);
//    }
}
