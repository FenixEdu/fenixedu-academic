package net.sourceforge.fenixedu.renderers.components;

import org.apache.struts.taglib.html.Constants;

public class HtmlCancelButton extends HtmlSubmitButton {

    public HtmlCancelButton() {
        super();

        setName(Constants.CANCEL_PROPERTY);
    }
    
    public HtmlCancelButton(String text) {
        this();
        
        setText(text);
    }
}
