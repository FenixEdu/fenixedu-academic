package net.sourceforge.fenixedu.renderers.components;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlLink extends HtmlComponent {

    private String text;
    
    private String url;
    private String anchor;
    private String contentType;
    private String charSet;
    
    private HtmlComponent body;
    
    private Map<String, String> parameters;
    
    public HtmlLink() {
        super();
        
        parameters = new Hashtable<String, String>();
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public String getCharSet() {
        return charSet;
    }

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setParameter(String name, String value) {
        this.parameters.put(name, value);
    }
    
    public Map<String, String> getParameters() {
        return this.parameters;
    }

    public HtmlComponent getBody() {
        return body;
    }

    public void setBody(HtmlComponent body) {
        this.body = body;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public List<HtmlComponent> getChildren() {
        ArrayList<HtmlComponent> children = new ArrayList<HtmlComponent>(super.getChildren());
        
        if (getBody() != null) {
            children.add(getBody());
        }
        
        return children;
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag = super.getOwnTag(context);
        
        tag.setName("a");
        
        tag.setAttribute("href", calculateUrl());
        tag.setAttribute("charset", getCharSet());
        tag.setAttribute("type", getContentType());

        if (getText() != null) {
            tag.addChild(new HtmlTag(null, getText()));
        }
        
        if (getBody() != null) {
            tag.addChild(getBody().getOwnTag(context));
        }
        
        return tag;
    }

    private String calculateUrl() {
        StringBuilder buffer = new StringBuilder();
        
        if (getUrl() != null) {
            buffer.append(getUrl());
        }
        
        if (! getParameters().isEmpty()) {
            buffer.append("?");
            
            Set<String> keys = getParameters().keySet();
            
            int count = keys.size();
            for (String key : keys) {
                buffer.append(key);
                buffer.append("=");
                buffer.append(getParameters().get(key));
               
                count--;
                if (count > 0) {
                    buffer.append("&");
                }
            }
        }
        
        if (getAnchor() != null) {
            buffer.append("#");
            buffer.append(getAnchor());
        }
        
        // allways make a link
        if (buffer.length() == 0) {
            buffer.append("#");
        }
        
        return buffer.toString();
    }
}
