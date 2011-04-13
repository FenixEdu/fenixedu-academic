package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class InquiryRegentAnswer extends InquiryRegentAnswer_Base {

    public InquiryRegentAnswer(Professorship professorship) {
	super();
	setProfessorship(professorship);
    }

    public void delete() {
	if (hasAnyQuestionAnswers()) {
	    throw new DomainException("error.inquiryAnswer.questionAnswersAssociated");
	}
	removeProfessorship();
	removeRootDomainObject();
	super.deleteDomainObject();
    }
}
