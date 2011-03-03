package net.sourceforge.fenixedu.presentationTier.Action.manager.payments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdPostingRules", module = "manager", formBeanClass = PostingRulesManagementDA.PostingRulesManagementForm.class)
@Forwards({
	@Forward(name = "showPhdProgramPostingRules", path = "/manager/payments/postingRules/management/phd/showPhdProgramPostingRules.jsp"),
	@Forward(name = "viewPostingRuleDetails", path = "/manager/payments/postingRules/management/phd/viewPostingRuleDetails.jsp"),
	@Forward(name = "editPhdProgramPostingRule", path = "/manager/payments/postingRules/management/phd/editPhdProgramPostingRule.jsp")
})
public class PhdPostingRulesManagementDA extends PostingRulesManagementDA {

    /* Phd Programs */

    public ActionForward showPhdProgramPostingRules(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdProgram phdProgram = getDomainObject(request, "phdProgramId");
	request.setAttribute("phdProgram", phdProgram);

	return mapping.findForward("showPhdProgramPostingRules");
    }

    @Override
    public ActionForward viewPostingRuleDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("phdProgram", getDomainObject(request, "phdProgramId"));
	request.setAttribute("postingRule", getPostingRule(request));

	return mapping.findForward("viewPostingRuleDetails");
    }

    @Override
    protected PostingRule getPostingRule(HttpServletRequest request) {
	return getDomainObject(request, "postingRuleId");
    }

    public ActionForward prepareEditPhdProgramPostingRule(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PostingRule postingRule = getPostingRule(request);

	request.setAttribute("phdProgram", getDomainObject(request, "phdProgramId"));
	request.setAttribute("postingRule", postingRule);

	return mapping.findForward("editPhdProgramPostingRule");
    }

    public ActionForward editPhdProgramPostingRuleInvalid(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("phdProgram", getDomainObject(request, "phdProgramId"));
	request.setAttribute("postingRule", getRenderedObject("postingRule"));

	return mapping.findForward("editPhdProgramPostingRule");
    }

}
