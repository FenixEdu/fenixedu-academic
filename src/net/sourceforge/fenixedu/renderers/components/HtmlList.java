package net.sourceforge.fenixedu.renderers.components;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlList extends HtmlComponent {

    private List <HtmlListItem> items;
    
    public HtmlList() {
        super();
        
        this.items = new ArrayList<HtmlListItem>();
    }

    public HtmlListItem createItem() {
        HtmlListItem newItem = new HtmlListItem();

        this.items.add(newItem);
        
        return newItem;
    }

    @Override
    public List<HtmlComponent> getChildren() {
        List<HtmlComponent> children = new ArrayList<HtmlComponent>(super.getChildren());
        
        children.addAll(this.items);
        
        return children;
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag = super.getOwnTag(context);
        
        tag.setName("ul");

        for (HtmlListItem item : this.items) {
            tag.addChild(item.getOwnTag(context));
        }
        
        return tag;
    }
}
