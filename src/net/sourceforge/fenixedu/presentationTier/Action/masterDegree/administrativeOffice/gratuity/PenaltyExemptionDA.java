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
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@Mapping(module = "masterDegreeAdministrativeOffice", path = "/penaltyExemption", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "return", path = "/studentSituation.do?page=0&method=readStudent"),
		@Forward(name = "edit", path = "gratuity.penaltyExemption.edit") })
public class PenaltyExemptionDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	GratuitySituation gratuitySituation = getRenderedObject();
	if (gratuitySituation != null) {
	    request.setAttribute("degreeType", "MASTER_DEGREE");
	    request.setAttribute("studentNumber", gratuitySituation.getStudentCurricularPlan().getRegistration().getNumber());
	    return mapping.findForward("return");
	}

	request.setAttribute("gratuitySituation", rootDomainObject.readGratuitySituationByOID(getIntegerFromRequest(request,
		"gratuitySituationID")));
	return mapping.findForward("edit");
    }

}
