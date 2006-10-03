package net.sourceforge.fenixedu.renderers.components;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public abstract class HtmlContainer extends HtmlComponent {

    private List<HtmlComponent> children;
    
    public HtmlContainer() {
        super();
        
        children = new ArrayList<HtmlComponent>();
    }

    public void addChild(HtmlComponent component) {
        this.children.add(component);
    }
    
    public List<HtmlComponent> getChildren() {
        return this.children;
    }
    
    public void removeChild(HtmlComponent container) {
        this.children.remove(container);
    }
    
    public void clearChildren() {
        this.children = new ArrayList<HtmlComponent>(); 
    }
    
    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag = super.getOwnTag(context);
        
        for (HtmlComponent component : getChildren()) {
            tag.addChild(component.getOwnTag(context));
        }
        
        // force close tag to appear
        if (tag.getChildren().isEmpty()) {
            tag.addChild(new HtmlTag(null));
        }
        
        return tag;
    }    
    
}
