/*
 * Created on Dec 7, 2005
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.curricularPlans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

public class CurricularCourseManagementBackingBean extends FenixBackingBean {
    
    public String getPersonDepartmentName() {
        IEmployee employee = getUserView().getPerson().getEmployee();
        return (employee != null && employee.getDepartmentWorkingPlace() != null) ? employee
                .getDepartmentWorkingPlace().getRealName() : "";
    }
    
    public List<SelectItem> getCurricularYears() {
        List<SelectItem> result = new ArrayList<SelectItem>(5);
        for (int i = 1; i <= 5; i++) {
            result.add(new SelectItem(Integer.valueOf(i), String.valueOf(i) + "º"));
        }
        return result;
    }
    
    public List<SelectItem> getSemesters() {
        List<SelectItem> result = new ArrayList<SelectItem>(2);
        result.add(new SelectItem(Integer.valueOf(1), String.valueOf(1) + "º"));
        result.add(new SelectItem(Integer.valueOf(2), String.valueOf(2) + "º"));
        return result;
    }
    
    public String createCurricularCourse() {
        return "curricularPlansManagement";
    }
    
    public String editCurricularCourse() {
        return "curricularPlansManagement";
    }
    
    public String associateCurricularCourse() {
        return "curricularPlansManagement";
    }
}
