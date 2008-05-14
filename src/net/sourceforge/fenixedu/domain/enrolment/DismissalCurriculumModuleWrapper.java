package net.sourceforge.fenixedu.domain.enrolment;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;

public class DismissalCurriculumModuleWrapper extends EnroledCurriculumModuleWrapper {

    private static final long serialVersionUID = 12L;
    private DomainReference<Dismissal> dismissal;

    public DismissalCurriculumModuleWrapper(final Dismissal dismissal, final ExecutionSemester executionSemester) {
	super(dismissal.getCurriculumGroup(), executionSemester);
	setDismissal(dismissal);
    }

    private Dismissal getDismissal() {
	return (this.dismissal != null) ? this.dismissal.getObject() : null;
    }

    private void setDismissal(Dismissal dismissal) {
	this.dismissal = (dismissal != null) ? new DomainReference<Dismissal>(dismissal) : null;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof DismissalCurriculumModuleWrapper) {
	    final DismissalCurriculumModuleWrapper other = (DismissalCurriculumModuleWrapper) obj;
	    return getDismissal().equals(other.getDismissal());
	}
	return false;
    }

    @Override
    public int hashCode() {
	return getDismissal().hashCode();
    }

    @Override
    public boolean canCollectRules() {
	return true;
    }

    @Override
    public boolean isAnnualCurricularCourse(ExecutionYear executionYear) {
	if (getDegreeModule() != null && getDegreeModule().isLeaf()) {
	    return ((CurricularCourse) getDegreeModule()).isAnual(executionYear);
	}

	return false;
    }
}
