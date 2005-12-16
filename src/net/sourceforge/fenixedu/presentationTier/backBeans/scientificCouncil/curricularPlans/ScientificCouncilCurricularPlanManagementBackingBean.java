/*
 * Created on Dec 7, 2005
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.scientificCouncil.curricularPlans;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

public class ScientificCouncilCurricularPlanManagementBackingBean extends FenixBackingBean {
    
    public List<IDegree> getBolonhaDegrees() throws FenixFilterException, FenixServiceException {
        Object[] args = { Degree.class };
        List<IDegree> allDegrees = (List<IDegree>) ServiceUtils.executeService(null, "ReadAllDomainObject", args);
        
        List<IDegree> result = new ArrayList<IDegree>();
        for (IDegree degree : allDegrees) {
            if (degree.getBolonhaDegreeType() != null) {
                result.add(degree);
            }
        }
        
        return result;
    }
    
    public String createCurricularPlan() {
        return "curricularPlansManagement";
    }
    
    public String editCurricularPlan() {
        return "curricularPlansManagement";
    }

}
