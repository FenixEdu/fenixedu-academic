package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeExceptionScheduleBean;

public class EditEmployeeExceptionSchedule extends Service {

    public void run(EmployeeExceptionScheduleBean employeeExceptionScheduleBean) {
	employeeExceptionScheduleBean.getSchedule().editException(employeeExceptionScheduleBean);
    }

}
