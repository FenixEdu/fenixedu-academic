package net.sourceforge.fenixedu.renderers.components;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlMenu extends HtmlSimpleValueComponent {

    private Integer size;
    private boolean disabled;
    private Integer tabIndex;

    private List<HtmlMenuEntry> entries;
    
    public HtmlMenu() {
        super();
        
        this.entries = new ArrayList<HtmlMenuEntry>();
    }
    
    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public HtmlMenuGroup createGroup(String label) {
        HtmlMenuGroup group = new HtmlMenuGroup(label);
        
        this.entries.add(group);
        
        return group;
    }
    
    public HtmlMenuOption createDefaultOption(String text) {
        HtmlMenuOption option = new HtmlMenuOption(text, "") {           
            @Override
            public void setValue(String value) {
                // Does not allow value to change
            }
        };
        
        if (this.entries.isEmpty()) {
            this.entries.add(option);
        }
        else {
            this.entries.set(0, option);
        }
        
        return option;
    }
    
    public HtmlMenuOption createOption(String text) {
        HtmlMenuOption option = new HtmlMenuOption(text);
        
        this.entries.add(option);
        
        return option;
    }
    
    @Override
    public void setValue(String value) {
        super.setValue(value);
        
        for (HtmlMenuEntry entry : this.entries) {
            entry.setSelected(value);
        }
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag =  super.getOwnTag(context);
        
        tag.setName("select");
        
        tag.setAttribute("size", this.size);
        if (this.disabled) {
            tag.setAttribute("disabled", this.disabled);
        }
        tag.setAttribute("tabindex", this.tabIndex);
        
        for (HtmlMenuEntry entry : this.entries) {
            tag.addChild(entry.getOwnTag(context));
        }
        
        return tag;
    }
    
    @Override
    public List<HtmlComponent> getChildren() {
        List<HtmlComponent> children = super.getChildren();
        
        children.addAll(this.entries);
        
        return children;
    }
}
