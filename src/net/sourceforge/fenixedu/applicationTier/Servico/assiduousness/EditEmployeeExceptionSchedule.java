package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeExceptionScheduleBean;

public class EditEmployeeExceptionSchedule extends FenixService {

    public void run(EmployeeExceptionScheduleBean employeeExceptionScheduleBean) {
	employeeExceptionScheduleBean.getSchedule().editException(employeeExceptionScheduleBean);
    }
}
