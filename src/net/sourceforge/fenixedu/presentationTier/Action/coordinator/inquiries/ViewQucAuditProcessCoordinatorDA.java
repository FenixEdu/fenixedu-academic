package net.sourceforge.fenixedu.presentationTier.Action.coordinator.inquiries;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;
import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.inquiries.ViewQucAuditProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(path = "/auditResult", module = "coordinator")
@Forwards( { @Forward(name = "viewProcessDetails", path = "/pedagogicalCouncil/inquiries/viewProcessDetailsNoAction.jsp") })
public class ViewQucAuditProcessCoordinatorDA extends ViewQucAuditProcessDA {

    public ActionForward viewProcessDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	DegreeCurricularPlan dcp = AbstractDomainObject.fromExternalId(request.getParameter("degreeCurricularPlanOID"));
	request.setAttribute("degreeCurricularPlanID", dcp.getIdInternal().toString());
	CoordinatedDegreeInfo.setCoordinatorContext(request);
	return super.viewProcessDetails(mapping, form, request, response);
    }
}
