package net.sourceforge.fenixedu.presentationTier.Action.student.thesis;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisVisibilityType;

public class DeclarationBean implements Serializable {

    /**
     * Serial version is.
     */
    private static final long serialVersionUID = 1L;

    private ThesisVisibilityType visibility;

    public DeclarationBean(Thesis thesis) {
        super();
        
        setVisibility(thesis.getVisibility());
    }

    public ThesisVisibilityType getVisibility() {
        return this.visibility;
    }

    public void setVisibility(ThesisVisibilityType visibility) {
        this.visibility = visibility;
    }
    
}
