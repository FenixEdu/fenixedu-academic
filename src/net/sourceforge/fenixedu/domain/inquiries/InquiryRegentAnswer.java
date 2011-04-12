package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class InquiryRegentAnswer extends InquiryRegentAnswer_Base {

    public InquiryRegentAnswer(Professorship professorship) {
	super();
	if (professorship == null) {
	    throw new DomainException("error.inquiry.regent.noProfessorship");
	}
	setProfessorship(professorship);
    }
}
