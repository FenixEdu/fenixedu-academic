package net.sourceforge.fenixedu.renderers.components;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlListItem extends HtmlComponent {

    private List<HtmlComponent> body;
    
    public HtmlListItem() {
        super();
        
        this.body = new ArrayList<HtmlComponent>();
    }

    public void setBody(HtmlComponent body) {
        this.body = new ArrayList<HtmlComponent>();
        this.body.add(body);
    }
    
    public HtmlComponent getBody() {
        if (this.body.isEmpty()) {
            return null;
        }
        else {
            return this.body.get(0);
        }
    }
    
    public void addChild(HtmlComponent component) {
        this.body.add(component);
    }

    @Override
    public List<HtmlComponent> getChildren() {
        List<HtmlComponent> children = super.getChildren();
        
        if (this.body != null) {
            children.addAll(this.body);
        }
        
        return children;
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag = super.getOwnTag(context);
        
        tag.setName("li");
        
        for (HtmlComponent child : this.body) {
            tag.addChild(child.getOwnTag(context));
        }
        
        return tag;
    }
}
