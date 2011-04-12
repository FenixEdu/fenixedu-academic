package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class InquiryTeacherAnswer extends InquiryTeacherAnswer_Base {

    public InquiryTeacherAnswer(Professorship professorship) {
	super();
	if (professorship == null) {
	    throw new DomainException("error.inquiry.teacher.noProfessorship");
	}
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
