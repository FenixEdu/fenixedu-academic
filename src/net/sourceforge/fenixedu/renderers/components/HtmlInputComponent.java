package net.sourceforge.fenixedu.renderers.components;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlInputComponent extends HtmlSimpleValueComponent {

    private String type;

    private String alternateText;

    private Integer tabIndex;

    private String accessKey;
    
    private String size;
    
    private String onChange;

    public HtmlInputComponent(String type) {
        super();
        
        this.type = type;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getAlternateText() {
        return alternateText;
    }

    public void setAlternateText(String alternateText) {
        this.alternateText = alternateText;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
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

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag = super.getOwnTag(context);
        
        tag.setName("input");
        
        tag.setAttribute("type", this.type);
        
        if (isDisabled()) {
            tag.setAttribute("disabled", true);
        }

        tag.setAttribute("alt", getAlternateText());
        tag.setAttribute("tabindex", getTabIndex());
        tag.setAttribute("accesskey", getAccessKey());
        tag.setAttribute("size", getSize());
        tag.setAttribute("onchange", getOnChange());
        
        return tag;
    }
}
