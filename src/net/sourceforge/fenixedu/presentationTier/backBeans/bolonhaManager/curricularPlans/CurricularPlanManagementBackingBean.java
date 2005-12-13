/*
 * Created on Dec 7, 2005
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.curricularPlans;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

public class CurricularPlanManagementBackingBean extends FenixBackingBean {
    private final ResourceBundle messages = getResourceBundle("ServidorApresentacao/BolonhaManagerResources");
    
    public String getPersonDepartmentName() {
        IEmployee employee = getUserView().getPerson().getEmployee();
        return (employee != null && employee.getDepartmentWorkingPlace() != null) ? employee
                .getDepartmentWorkingPlace().getRealName() : "";
    }
    
    public List<SelectItem> getCurricularStageTypes() {
        List<SelectItem> result = new ArrayList<SelectItem>(3);        
        result.add(new SelectItem(CurricularStage.DRAFT, messages.getString(CurricularStage.DRAFT.getName())));
        result.add(new SelectItem(CurricularStage.PUBLISHED, messages.getString(CurricularStage.PUBLISHED.getName())));
        result.add(new SelectItem(CurricularStage.APPROVED, messages.getString(CurricularStage.APPROVED.getName())));
        return result;
    }
    
    public String createCurricularPlan() {
        return "curricularPlansManagement";
    }
    
    public String editCurricularPlan() {
        return "curricularPlansManagement";
    }
}
