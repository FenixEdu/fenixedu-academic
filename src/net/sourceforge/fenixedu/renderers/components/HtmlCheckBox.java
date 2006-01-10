package net.sourceforge.fenixedu.renderers.components;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlCheckBox extends HtmlInputComponent {

    private String text;
    
    private boolean checked;

    private boolean rendered;

    private String onClick;
    
    public HtmlCheckBox() {
        super("checkbox");
        
        this.checked = false;
        this.rendered = false;
    }
    
    public HtmlCheckBox(boolean checked) {
        this();
        
        this.checked = checked;
    }

    public HtmlCheckBox(String text) {
        this();
        
        this.text = text;
    }
    
    public HtmlCheckBox(String text, boolean checked) {
        this();
        
        this.text = text;
        this.checked = checked;
    }

    public void setOnClick(String script) {
        this.onClick = script;
    }
    
    public String getOnClick() {
        return this.onClick;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void setValue(String value) {
        // Only allow changing value before rendering because an unchecked checkbox
        // will be set the value 'null' when submited
        if (! this.rendered) {
            super.setValue(value);
        }
        
        setChecked(value != null);
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag =  super.getOwnTag(context);
        
        if (this.checked) {
            tag.setAttribute("checked", this.checked);
        }

        if (this.onClick != null) {
            tag.setAttribute("onclick", this.onClick);
        }
        
        if (this.text == null) {
            return tag;
        }
        
        HtmlTag span = new HtmlTag("span");
        
        span.addChild(tag);
        span.addChild(new HtmlTag(null, this.text));

        this.rendered = true;
                
        return span;
    }
}
