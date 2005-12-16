/*
 * Created on Dec 7, 2005
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.curricularPlans;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

public class CurricularPlanManagementBackingBean extends FenixBackingBean {
    private final ResourceBundle messages = getResourceBundle("ServidorApresentacao/BolonhaManagerResources");
    
    public String getPersonDepartmentName() {
        IEmployee employee = getUserView().getPerson().getEmployee();
        return (employee != null && employee.getCurrentDepartmentWorkingPlace() != null) ? employee
                .getCurrentDepartmentWorkingPlace().getRealName() : "";
    }
    
    public List<IDegree> getDepartmentDegrees() throws FenixFilterException, FenixServiceException {
        Object[] args = { Degree.class, 10 };
        List result = new ArrayList();
        result.add(((IDegree) ServiceUtils.executeService(null, "ReadDomainObject", args)));
        return result;
        
//        IEmployee employee = getUserView().getPerson().getEmployee();
//        if (employee != null && employee.getDepartmentWorkingPlace() != null)  {
//            IUnit deparmentUnit = employee.getDepartmentWorkingPlace().getUnit();
//            
//            for (IUnit degreeUnit : deparmentUnit.getDegreeUnits()) {
//                result.add(degreeUnit.getDegree());
//            }
//        }
//        
//        return result;
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
