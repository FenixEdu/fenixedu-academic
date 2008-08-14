package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.credits;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.teacherCredits.TeacherCreditsPeriodBean;

public class CreateTeacherCreditsFillingPeriod extends Service {

    public void run(TeacherCreditsPeriodBean bean) {
	if (bean != null) {
	    if (bean.isTeacher()) {
		bean.getExecutionPeriod().editTeacherCreditsPeriod(bean.getBeginForTeacher(), bean.getEndForTeacher());
	    } else {
		bean.getExecutionPeriod().editDepartmentOfficeCreditsPeriod(bean.getBeginForDepartmentAdmOffice(),
			bean.getEndForDepartmentAdmOffice());
	    }
	}
    }
}
