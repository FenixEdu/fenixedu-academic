package net.sourceforge.fenixedu.renderers.components;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlSelectList extends HtmlMultipleValueComponent {
    
    private Integer size;
    private Integer tabIndex;

    private List<HtmlMenuOption> options;
    
    public HtmlSelectList() {
        super();
        
        this.options = new ArrayList<HtmlMenuOption>();
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

    public HtmlMenuOption createOption(String text) {
        HtmlMenuOption option = new HtmlMenuOption(text);
        
        this.options.add(option);
        
        return option;
    }
    
    @Override
    public void setValues(String... values) {
        super.setValues(values);
        
        for (String value : values) {
            for (HtmlMenuOption option: getOptions()) {
                if (option.getValue().equals(value)) {
                    option.setSelected(true);
                }
            }
        }
    }

    public List<HtmlMenuOption> getOptions() {
        return this.options;
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag = super.getOwnTag(context);
        
        tag.setName("select");
        
        tag.setAttribute("size", getSize());
        tag.setAttribute("multiple", true);
        tag.setAttribute("tabindex", getTabIndex());
        
        if (isDisabled()) {
            tag.setAttribute("disabled", true);
        }
        
        for (HtmlMenuEntry entry : getOptions()) {
            tag.addChild(entry.getOwnTag(context));
        }
        
        return tag;
    }
    
    @Override
    public List<HtmlComponent> getChildren() {
        List<HtmlComponent> children = super.getChildren();
        
        children.addAll(getOptions());
        
        return children;
    }

}
