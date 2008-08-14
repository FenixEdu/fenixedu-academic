package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class TeacherAdviseService extends TeacherAdviseService_Base {

    public TeacherAdviseService(TeacherService teacherService, Advise advise, Double percentage, RoleType roleType) {
	super();

	if (teacherService == null || advise == null || percentage == null) {
	    throw new DomainException("arguments can't be null");
	}

	setTeacherService(teacherService);
	getTeacherService().getExecutionPeriod().checkValidCreditsPeriod(roleType);
	checkPercentage(percentage);
	advise.checkPercentageCoherenceWithOtherAdvises(teacherService.getExecutionPeriod(), percentage.doubleValue());
	setPercentage(percentage);
	setAdvise(advise);
    }

    public void delete(RoleType roleType) {
	getTeacherService().getExecutionPeriod().checkValidCreditsPeriod(roleType);
	removeAdvise();
	removeTeacherService();
	super.delete();
    }

    public void updatePercentage(Double percentage, RoleType roleType) {
	getTeacherService().getExecutionPeriod().checkValidCreditsPeriod(roleType);
	checkPercentage(percentage);
	getAdvise().checkPercentageCoherenceWithOtherAdvises(getTeacherService().getExecutionPeriod(), percentage.doubleValue());
	setPercentage(percentage);
    }

    private void checkPercentage(Double percentage) {
	if (percentage == null || percentage > 100 || percentage < 0) {
	    throw new DomainException("error.invalid.advise.service.percentage");
	}
    }
}
