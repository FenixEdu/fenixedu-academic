package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.dismissal;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public class CreateNewCreditsDismissal extends Service {
    
    public void run(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup, Collection<GenericPair<CurriculumGroup, CurricularCourse>> dismissals,
	    Collection<Enrolment> enrolments, Double givenCredits) {
	studentCurricularPlan.createNewCreditsDismissal(studentCurricularPlan, curriculumGroup, dismissals, enrolments, givenCredits);
    }
    
}
