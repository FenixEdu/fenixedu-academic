package net.sourceforge.fenixedu.renderers.components;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

import org.apache.commons.collections.Predicate;

public abstract class HtmlComponent implements Serializable {

    private boolean visible;
    
    private String id;
    
    private String classes;
    
    private String style;
    
    private String title;
    
    // TODO: %i18n
    
    // TODO: %events
    
    public HtmlComponent() {
        this.visible = true;
    }

    public String getClasses() {
        return classes;
    }

    public void addClass(String newClass) {
        this.classes = classes == null ? newClass : classes + " " + newClass;
    }
    
    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
        
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public List<HtmlComponent> getChildren() {
        return new ArrayList<HtmlComponent>();
    }
    
    public void draw(PageContext context) throws IOException {
        HtmlTag tag = getOwnTag(context);
        tag.writeTag(context);
    }
    
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag = new HtmlTag("div"); // generic container
        
        tag.setAttribute("id", getId());
        tag.setAttribute("class", getClasses());
        tag.setAttribute("style", getStyle());
        tag.setAttribute("title", getTitle());
        
        tag.setVisible(isVisible());
        
        return tag;
    }
    
    public HtmlComponent getChild(Predicate predicate) {
        for (HtmlComponent child : getChildren()) {
            if (predicate.evaluate(child)) {
                return child;
            }
            else {
                HtmlComponent foundComponent = child.getChild(predicate);
                if (foundComponent != null) {
                    return foundComponent;
                }
            }
        }
        
        return null;
    }
    
    public List<HtmlComponent> getChildren(Predicate predicate) {
        List<HtmlComponent> results = new ArrayList<HtmlComponent>();
        
        for (HtmlComponent child : getChildren()) {
            if (predicate.evaluate(child)) {
                results.add(child);
            }

            results.addAll(child.getChildren(predicate));
        }
        
        return results;
    }
    
    public HtmlComponent getChildWithId(final String id) {
        return getChild(new Predicate() {

            public boolean evaluate(Object component) {
                return ((HtmlComponent) component).getId().equals(id);
            }
            
        });
    }
}
