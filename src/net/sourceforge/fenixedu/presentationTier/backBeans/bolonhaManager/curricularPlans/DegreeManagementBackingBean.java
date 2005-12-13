/*
 * Created on Dec 7, 2005
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.curricularPlans;

import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

public class DegreeManagementBackingBean extends FenixBackingBean {

    public String getPersonDepartmentName() {
        IEmployee employee = getUserView().getPerson().getEmployee();
        return (employee != null && employee.getDepartmentWorkingPlace() != null) ? employee
                .getDepartmentWorkingPlace().getRealName() : "";
    }
    
    public String createDegree() {
        return "curricularPlansManagement";
    }
    
    public String editDegree() {
        return "curricularPlansManagement";
    }    
}
