package net.sourceforge.fenixedu.renderers.layouts;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu._development.LogLevel;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

public abstract class Layout {
    private static Logger logger = Logger.getLogger(Layout.class);
    
    private String classes;

    private String style;

    private String title;

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setTitle(String title) {
        this.title = title;
    }
        
    public String getClasses() {
        return classes;
    }

    public String getStyle() {
        return style;
    }

    public String getTitle() {
        return title;
    }

    public String[] getAndVerifyPropertyNames() {
        String[] names = getPropertyNames();
        List<String> finalNames = new ArrayList<String>();
        
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            
            if (! PropertyUtils.isWriteable(this, name)) {
                if (LogLevel.WARN) {
                    logger.warn("Layout " + this + " specified a non-writeable property: " + name);
                }
            }
            else {
                finalNames.add(name);
            }
        }
        
        return finalNames.toArray(new String[0]);
    }
    
    public String[] getPropertyNames() {
        return new String[] { "classes", "style" , "title" };
    }

    protected String[] mergePropertyNames(String[] parentNames, String[] ownNames) {
        String[] allNames = new String[parentNames.length + ownNames.length];
        
        for (int i = 0; i < allNames.length; i++) {
            allNames[i] = i < parentNames.length ? parentNames[i] : ownNames[i - parentNames.length];
        }
        
        return allNames;
    }
    
    public HtmlComponent createLayout(Object object, Class type) {
        HtmlComponent component = createComponent(object, type);
        applyStyle(component);
        
        return component;
    }

    public abstract HtmlComponent createComponent(Object object, Class type);

    public void applyStyle(HtmlComponent component) {
        component.setClasses(getClasses());
        component.setStyle(getStyle());
        component.setTitle(getTitle());
    }
}
