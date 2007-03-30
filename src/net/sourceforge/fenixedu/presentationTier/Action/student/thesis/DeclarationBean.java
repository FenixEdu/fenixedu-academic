package net.sourceforge.fenixedu.presentationTier.Action.student.thesis;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisVisibilityType;

import org.joda.time.DateTime;

public class DeclarationBean implements Serializable {

    /**
     * Serial version is.
     */
    private static final long serialVersionUID = 1L;

    private ThesisVisibilityType visibility;
    private DateTime availableAfter;

    public DeclarationBean(Thesis thesis) {
        super();
        
        setVisibility(thesis.getVisibility());
        setAvailableAfter(thesis.getDocumentsAvailableAfter());
    }

    public ThesisVisibilityType getVisibility() {
        return this.visibility;
    }

    public void setVisibility(ThesisVisibilityType visibility) {
        this.visibility = visibility;
    }

    public DateTime getAvailableAfter() {
        return this.availableAfter;
    }

    public void setAvailableAfter(DateTime availableAfter) {
        this.availableAfter = availableAfter;
    }
    
}
