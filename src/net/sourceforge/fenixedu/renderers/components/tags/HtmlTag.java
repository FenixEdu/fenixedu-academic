package net.sourceforge.fenixedu.renderers.components.tags;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.jsp.PageContext;

// TODO: cfgi, implement this properly ... too hackish

public class HtmlTag {

    protected static String DEFAULT_INDENT = "  ";
    
    private String name;
    
    private Map<String, String> attributes;
    
    private String text;
    
    private List<HtmlTag> children;
    
    private boolean visible;

    private HtmlTag() {
        this.attributes = new HashMap<String, String>();
        this.children = new ArrayList<HtmlTag>();
        this.visible = true;
        this.text = "";
    }
    
    public HtmlTag(String name) {
        this();
        
        this.name = name;
    }

    public HtmlTag(String name, String text) {
        this(name);

        this.text = text;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void copyAttributes(HtmlTag tag) {
        this.attributes.putAll(tag.attributes);
    }
    
    public void setAttribute(String name, String value) {
        if (name != null && value != null) {
            this.attributes.put(name, value);
        }
    }
    
    public void setAttribute(String name, Boolean value) {
        if (value != null) {
            setAttribute(name, value.toString().toLowerCase());
        }
    }
    
    public void setAttribute(String name, Number value) {
        if (value != null) {
            setAttribute(name, value.toString());
        }
    }
    
    public void removeAttribute(String name) {
        this.attributes.remove(name);
    }
    
    public boolean hasVisibleAttributes() {
        return this.attributes.keySet().size() > 0;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    public String getText() {
        return this.text;
    }
    
    public void addChild(HtmlTag tag) {
        this.children.add(tag);
    }

    public List<HtmlTag> getChildren() {
        return this.children;
    }
    
    public void writeTag(PageContext context) throws IOException {
        writeTag(context.getOut());
    }

    public void writeTag(Writer writer) throws IOException {
        writeTag(writer, "");
    }
    
    public void writeTag(Writer writer, String indent) throws IOException {
        if (isVisible()) {
            writeOpenTag(writer, indent);
            writeBody(writer, indent);
            writeCloseTag(writer, indent);
        }
    }
    
    protected void writeBody(Writer writer, String indent) throws IOException {
        if (getChildren().size() > 0) {
            writer.write('\n');
        }
        
        for (HtmlTag child : getChildren()) {
            child.writeTag(writer, indent + DEFAULT_INDENT);
            writer.write('\n');
        }
    }

    protected void writeOpenTag(Writer writer, String indent) throws IOException {
        writer.write(indent);
        
        if (name != null) {
            writer.write('<');
            writer.write(name);

            for (final Entry<String, String> attributeEntry : attributes.entrySet()) {
            	final String attributeName = attributeEntry.getKey();
            	final String attributeValue = attributeEntry.getValue();
                if (attributeValue != null) {
                    writer.write(' ');
                    writer.write(attributeName);
                    writer.write("=\"");
                    writer.write(attributeValue);
                    writer.write('"');
                }
            }
            
            writer.write('>');
        }
        
        if (this.text != null) {
            writer.write(this.text);
        }        
    }
        
    protected void writeCloseTag(Writer writer, String indent) throws IOException {
        if (this.children.size() > 0) {
            writer.write(indent);
        }
        
        if (name != null) {
            writer.write("</");
            writer.write(name);
            writer.write('>');
        }
        
    }    
}
