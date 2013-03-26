package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.TutorshipSummary;

public class EditSummaryBean extends CreateSummaryBean implements Serializable {

    private static final long serialVersionUID = 161580336110944806L;

    public EditSummaryBean(TutorshipSummary tutorshipSummary) {
        super(tutorshipSummary);
    }
}