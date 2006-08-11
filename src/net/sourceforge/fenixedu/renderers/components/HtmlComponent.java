package net.sourceforge.fenixedu.renderers.components;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.collections.Predicate;

public abstract class HtmlComponent implements Serializable {

    public enum TextDirection {
        RIGTH_TO_LEFT,
        LEFT_TO_RIGHT;
        
        public String toString() {
            if (this == RIGTH_TO_LEFT) {
                return "rtl";
            }
            else {
                return "ltr";
            }
        }
    };
    
    private boolean visible;
    private boolean indented;
    
    // %coreattrs
    private String id;
    private String classes;
    private String style;
    private String title;
    
    // %i18n
    private String language;
    private TextDirection direction;
    
    // %event
    private String onClick;
    private String onDblClick;
    private String onMouseDown;
    private String onMouseUp;
    private String onMouseOver;
    private String onMouseMove;
    private String onMouseOut;
    private String onKeyPress;
    private String onKeyDown;
    private String onKeyUp;
    
    // custom
    private Map<String, String> custom;
    
    public HtmlComponent() {
        this.custom = new HashMap<String, String>();
        this.visible = true;
        this.indented = true;
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
        
    public TextDirection getDirection() {
        return this.direction;
    }

    public void setDirection(TextDirection direction) {
        this.direction = direction;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isIndented() {
        return this.indented;
    }

    public void setIndented(boolean idented) {
        this.indented = idented;
    }

    public String getOnClick() {
        return this.onClick;
    }

    public void setOnClick(String onclick) {
        this.onClick = onclick;
    }

    public String getOnDblClick() {
        return this.onDblClick;
    }

    public void setOnDblClick(String ondblclick) {
        this.onDblClick = ondblclick;
    }

    public String getOnKeyDown() {
        return this.onKeyDown;
    }

    public void setOnKeyDown(String onkeydown) {
        this.onKeyDown = onkeydown;
    }

    public String getOnKeyPress() {
        return this.onKeyPress;
    }

    public void setOnKeyPress(String onkeypress) {
        this.onKeyPress = onkeypress;
    }

    public String getOnKeyUp() {
        return this.onKeyUp;
    }

    public void setOnKeyUp(String onkeyup) {
        this.onKeyUp = onkeyup;
    }

    public String getOnMouseDown() {
        return this.onMouseDown;
    }

    public void setOnMouseDown(String onmousedown) {
        this.onMouseDown = onmousedown;
    }

    public String getOnMouseMove() {
        return this.onMouseMove;
    }

    public void setOnMouseMove(String onmousemove) {
        this.onMouseMove = onmousemove;
    }

    public String getOnMouseOut() {
        return this.onMouseOut;
    }

    public void setOnMouseOut(String onmouseout) {
        this.onMouseOut = onmouseout;
    }

    public String getOnMouseOver() {
        return this.onMouseOver;
    }

    public void setOnMouseOver(String onmouseover) {
        this.onMouseOver = onmouseover;
    }

    public String getOnMouseUp() {
        return this.onMouseUp;
    }

    public void setOnMouseUp(String onmouseup) {
        this.onMouseUp = onmouseup;
    }

    public String getAttribute(String name) {
        return this.custom.get(name);
    }
    
    public void setAttribute(String name, String value) {
        this.custom.put(name, value);
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
        
        tag.setAttribute("lang", getLanguage());
        
        if (getDirection() != null) {
            tag.setAttribute("dir", getDirection().toString());
        }
        
        tag.setAttribute("onclick", getOnClick());
        tag.setAttribute("ondblclick", getOnDblClick());
        tag.setAttribute("onmousedown", getOnMouseDown());
        tag.setAttribute("onmouseup", getOnMouseUp());
        tag.setAttribute("onmouseover", getOnMouseOver());
        tag.setAttribute("onmousemove", getOnMouseMove());
        tag.setAttribute("onmouseout", getOnMouseOut());
        tag.setAttribute("onkeypress", getOnKeyPress());
        tag.setAttribute("onkeydown", getOnKeyDown());
        tag.setAttribute("onkeyup", getOnKeyUp());
        
        tag.setVisible(isVisible());
        tag.setIndented(isIndented());
        
        for (Entry<String, String> entry : this.custom.entrySet()) {
            tag.setAttribute(entry.getKey(), entry.getValue());
        }
        
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
    
    public static HtmlComponent getComponent(HtmlComponent component, Predicate predicate) {
        if (component == null) {
            return null;
        }
        
        if (predicate.evaluate(component)) {
            return component;
        }
        
        return component.getChild(predicate);
    }
    
    public static List<HtmlComponent> getComponents(HtmlComponent component, Predicate predicate) {
        if (component == null) {
            return new ArrayList<HtmlComponent>();
        }
        
        if (predicate.evaluate(component)) {
            List<HtmlComponent> results = new ArrayList<HtmlComponent>();
            results.add(component);
            
            return results;
        }
        else {
            return component.getChildren(predicate);
        }
    }

    public void register() {
        RenderUtils.registerComponent(getId(), this);
    }
    
    public void register(String id) {
        RenderUtils.registerComponent(id, this);
    }
}
