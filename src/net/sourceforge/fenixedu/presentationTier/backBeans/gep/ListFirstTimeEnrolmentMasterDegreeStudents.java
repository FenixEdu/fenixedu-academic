package net.sourceforge.fenixedu.presentationTier.backBeans.gep;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 *
 */
public class ListFirstTimeEnrolmentMasterDegreeStudents extends FenixBackingBean {

    public ListFirstTimeEnrolmentMasterDegreeStudents() {
        super();
    }
    
    public Collection getStudentCurricularPlans() throws FenixFilterException, FenixServiceException {
        
        Object[] args = { "2004/2005" };
        return (Collection) ServiceUtils.executeService(userView, "ListMasterDegreeStudents", args);
        
    }

}
