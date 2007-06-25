/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.gratuity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class PenaltyExemptionDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	GratuitySituation gratuitySituation = (GratuitySituation) getRenderedObject();
	if(gratuitySituation != null){
	    request.setAttribute("degreeType", "MASTER_DEGREE");
	    request.setAttribute("studentNumber", gratuitySituation.getStudentCurricularPlan()
		    .getRegistration().getNumber());
	    return mapping.findForward("return");
	}
	
	request.setAttribute("gratuitySituation", rootDomainObject
		.readGratuitySituationByOID(getIntegerFromRequest(request, "gratuitySituationID")));
	return mapping.findForward("edit");
    }

}
