package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Collection;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

public class TemporarySubstitution extends TemporarySubstitution_Base {
    
    public TemporarySubstitution() {
        super();
    }
    
    public TemporarySubstitution(StudentCurricularPlan studentCurricularPlan,
	    Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments, ExecutionPeriod executionPeriod) {
	init(studentCurricularPlan, dismissals, enrolments, executionPeriod);
    }
    
    @Override
    public boolean isTemporary() {
	return true;
    }
}
