package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Collection;
import java.util.Collections;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;

public class TemporarySubstitution extends TemporarySubstitution_Base {

    public TemporarySubstitution() {
	super();
    }

    public TemporarySubstitution(StudentCurricularPlan studentCurricularPlan, Collection<SelectedCurricularCourse> dismissals,
	    Collection<IEnrolment> enrolments, ExecutionSemester executionSemester) {
	this();
	init(studentCurricularPlan, dismissals, enrolments, executionSemester);
    }

    @Override
    public boolean isTemporary() {
	return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<ICurriculumEntry> getAverageEntries() {
	return Collections.EMPTY_SET;
    }

}
