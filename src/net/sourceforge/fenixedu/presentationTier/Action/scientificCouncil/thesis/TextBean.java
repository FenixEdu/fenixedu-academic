package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.thesis;

import java.io.Serializable;

public class TextBean implements Serializable {

    /**
     * Serial version id. 
     */
    private static final long serialVersionUID = 1L;
    
    private String text;
    
    public TextBean() {
        super();
        
        this.text = "";
    }

    public String getText() {
        return this.text;
    }
    
    public void setText(String text) {
	this.text = text;
    }
}
