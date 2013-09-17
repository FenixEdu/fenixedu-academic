package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class InquiryTeacherAnswer extends InquiryTeacherAnswer_Base {

    public InquiryTeacherAnswer(Professorship professorship) {
        super();
        setProfessorship(professorship);
    }

    public void delete() {
        if (hasAnyQuestionAnswers()) {
            throw new DomainException("error.inquiryAnswer.questionAnswersAssociated");
        }
        setProfessorship(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }
    @Deprecated
    public boolean hasAllowAcademicPublicizing() {
        return getAllowAcademicPublicizing() != null;
    }

    @Deprecated
    public boolean hasProfessorship() {
        return getProfessorship() != null;
    }

}
