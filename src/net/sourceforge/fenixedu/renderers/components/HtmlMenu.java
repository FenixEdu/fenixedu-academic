package net.sourceforge.fenixedu.renderers.components;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlMenu extends HtmlSimpleValueComponent {

    private Integer size;
    private Integer tabIndex;
    private String onChange;

    private List<HtmlMenuEntry> entries;
    
    public HtmlMenu() {
        super();
        
        this.entries = new ArrayList<HtmlMenuEntry>();
    }
    
    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(Integer tabIndex) {
        this.tabIndex = tabIndex;
    }

    public String getOnChange() {
        return onChange;
    }

    public void setOnChange(String onChange) {
        this.onChange = onChange;
    }

    public List<HtmlMenuEntry> getEntries() {
        return this.entries;
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
        
        tag.setAttribute("size", getSize());
        tag.setAttribute("onchange", getOnChange());
        
        if (isDisabled()) {
            tag.setAttribute("disabled", true);
        }
        tag.setAttribute("tabindex", getTabIndex());
        
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
