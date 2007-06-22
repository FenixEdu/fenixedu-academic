package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeExceptionScheduleBean;
import net.sourceforge.fenixedu.domain.assiduousness.Schedule;

public class CreateEmployeeExceptionSchedule extends Service {

    public void run(EmployeeExceptionScheduleBean employeeExceptionScheduleBean) {
	new Schedule(employeeExceptionScheduleBean);
    }

}
