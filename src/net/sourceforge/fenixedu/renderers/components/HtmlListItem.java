package net.sourceforge.fenixedu.renderers.components;

import java.util.List;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlListItem extends HtmlComponent {

    private HtmlComponent body;
    
    public HtmlListItem() {
        super();
    }

    public void setBody(HtmlComponent body) {
        this.body = body;
    }
    
    public HtmlComponent getBody() {
        return this.body;
    }

    @Override
    public List<HtmlComponent> getChildren() {
        List<HtmlComponent> children = super.getChildren();
        
        if (this.body != null) {
            children.add(this.body);
        }
        
        return children;
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag = super.getOwnTag(context);
        
        tag.setName("li");
        
        if (this.body != null) {
            tag.addChild(this.body.getOwnTag(context));
        }
        
        return tag;
    }
}
