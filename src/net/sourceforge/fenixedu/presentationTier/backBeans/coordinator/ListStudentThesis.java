package net.sourceforge.fenixedu.presentationTier.backBeans.coordinator;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ListStudentThesis {

    private Integer degreeCurricularPlanID;

    public ListStudentThesis() {
    }

    public Integer getDegreeCurricularPlanID() {
	return degreeCurricularPlanID;
    }

    public void setDegreeCurricularPlanID(Integer degreeCurricularPlanID) {
	this.degreeCurricularPlanID = degreeCurricularPlanID;

	((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).setAttribute(
		"degreeCurricularPlanID", degreeCurricularPlanID);

    }

    public List getMasterDegreeThesisDataVersions() {

	Object[] args = { degreeCurricularPlanID };
	try {
	    return (List) ServiceUtils.executeService("ReadActiveMasterDegreeThesisDataVersionsByDegreeCurricularPlan", args);
	} catch (FenixFilterException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (FenixServiceException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return null;

    }

}
