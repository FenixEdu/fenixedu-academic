package net.sourceforge.fenixedu.presentationTier.Action.manager.payments;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.accounting.postingRule.CreateDFAGratuityPostingRuleBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PostingRulesManagementDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	return mapping.findForward("chooseCategory");
    }

    public ActionForward chooseDFADegreeCurricularPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final List<DegreeCurricularPlan> degreeCurricularPlans = DegreeCurricularPlan.readByDegreeTypeAndState(
		DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA, DegreeCurricularPlanState.ACTIVE);

	request.setAttribute("degreeCurricularPlans", degreeCurricularPlans);

	return mapping.findForward("chooseDFADegreeCurricularPlan");
    }

    public ActionForward showPostingRulesForDFADegreeCurricularPlan(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);

	request.setAttribute("allowCreateGratuityPR", !degreeCurricularPlan.getServiceAgreementTemplate()
		.hasActivePostingRuleFor(EventType.GRATUITY));
	request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);

	return mapping.findForward("showPostingRulesForDFADegreeCurricularPlan");
    }

    public ActionForward viewDFAPostingRuleDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("degreeCurricularPlan", getDegreeCurricularPlan(request));
	request.setAttribute("postingRule", getPostingRule(request));

	return mapping.findForward("viewDFAPostingRuleDetails");
    }

    private PostingRule getPostingRule(HttpServletRequest request) {
	return rootDomainObject.readPostingRuleByOID(getIntegerFromRequest(request, "postingRuleId"));
    }

    private DegreeCurricularPlan getDegreeCurricularPlan(final HttpServletRequest request) {
	return rootDomainObject.readDegreeCurricularPlanByOID(getIntegerFromRequest(request, "degreeCurricularPlanId"));
    }

    public ActionForward prepareCreateDFAGratuityPR(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);

	request.setAttribute("createDFAGratuityPostingRuleBean", new CreateDFAGratuityPostingRuleBean(degreeCurricularPlan
		.getServiceAgreementTemplate()));

	return mapping.findForward("createDFAGratuityPR");
    }

    public ActionForward prepareCreateDFAGratuityPRInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("createDFAGratuityPostingRuleBean", getCreateDFAGratuityPostingRuleBeanFromRequest());

	return mapping.findForward("createDFAGratuityPR");
    }

    public ActionForward createDFAGratuityPR(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	try {
	    executeService("CreatePostingRule", getCreateDFAGratuityPostingRuleBeanFromRequest());
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey(), ex.getArgs());

	    request.setAttribute("createDFAGratuityPostingRuleBean", getCreateDFAGratuityPostingRuleBeanFromRequest());

	    return mapping.findForward("createDFAGratuityPR");
	}

	request.setAttribute("degreeCurricularPlanId", getCreateDFAGratuityPostingRuleBeanFromRequest().getDegreeCurricularPlan()
		.getIdInternal());

	return showPostingRulesForDFADegreeCurricularPlan(mapping, form, request, response);

    }

    private CreateDFAGratuityPostingRuleBean getCreateDFAGratuityPostingRuleBeanFromRequest() {
	return (CreateDFAGratuityPostingRuleBean) getObjectFromViewState("createDFAGratuityPostingRuleBean");
    }

    public ActionForward prepareEditDFADegreeCurricularPlanPostingRule(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("degreeCurricularPlan", getDegreeCurricularPlan(request));
	request.setAttribute("postingRule", getPostingRule(request));

	return mapping.findForward("editDFADegreeCurricularPlanPostingRule");
    }

    public ActionForward prepareEditDFADegreeCurricularPlanPostingRuleInvalid(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("degreeCurricularPlan", getDegreeCurricularPlan(request));
	request.setAttribute("postingRule", getPostingRule(request));

	return mapping.findForward("editDFADegreeCurricularPlanPostingRule");

    }

}
