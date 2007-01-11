package net.sourceforge.fenixedu.renderers.components;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlRadioButton extends HtmlInputComponent {

    private String text;
    
    private boolean checked;

    public HtmlRadioButton() {
        super("radio");
        
        this.checked = false;
    }
    
    public HtmlRadioButton(boolean checked) {
        this();
        
        this.checked = checked;
    }

    public HtmlRadioButton(String text) {
        this();
        
        this.text = text;
    }
    
    public HtmlRadioButton(String text, boolean checked) {
        this();
        
        this.text = text;
        this.checked = checked;
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

    public String getUserValue() {
        return super.getValue();
    }

    public void setUserValue(String userValue) {
        super.setValue(userValue);
    }

    @Override
    public void setValue(String value) {
        setChecked(String.valueOf(getUserValue()).equals(value));
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag =  super.getOwnTag(context);
        
        if (this.checked) {
            tag.setAttribute("checked", this.checked);
        }

        if (this.text == null) {
            return tag;
        }
        
        HtmlTag span = new HtmlTag("span");
        
        span.addChild(tag);
        span.addChild(new HtmlTag(null, this.text));

        return span;
    }
}
