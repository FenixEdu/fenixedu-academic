package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.Professorship;

public class InquiryRegentAnswer extends InquiryRegentAnswer_Base {

    public InquiryRegentAnswer(Professorship professorship) {
	super();
	setProfessorship(professorship);
    }
}
