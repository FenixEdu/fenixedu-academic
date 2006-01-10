package net.sourceforge.fenixedu.renderers.components;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlMenuGroup extends HtmlMenuEntry {

    private List<HtmlMenuOption> options;
    
    public HtmlMenuGroup(String label) {
        super(label, null);
        
        this.options = new ArrayList<HtmlMenuOption>();
    }

    public HtmlMenuOption createOption() {
        HtmlMenuOption option = new HtmlMenuOption();
        
        this.options.add(option);
        
        return option;
    }
    
    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag = super.getOwnTag(context);
        
        tag.setName("optgroup");
        
        for (HtmlMenuOption option : this.options) {
            tag.addChild(option.getOwnTag(context));
        }
        
        return tag;
    }

    @Override
    public List<HtmlComponent> getChildren() {
        List<HtmlComponent> children = super.getChildren();
        
        children.addAll(this.options);
        
        return children;
    }

    @Override
    public void setSelected(String value) {
        for (HtmlMenuOption option : this.options) {
            option.setSelected(value);
        }
    }
}
