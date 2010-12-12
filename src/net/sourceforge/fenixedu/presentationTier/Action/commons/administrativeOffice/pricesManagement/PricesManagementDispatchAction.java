package net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.pricesManagement;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.accounting.postingRules.PartialRegistrationRegimeRequestPR;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class PricesManagementDispatchAction extends FenixDispatchAction {

    public ActionForward viewPrices(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final SortedSet<PostingRule> sortedPostingRules = new TreeSet<PostingRule>(PostingRule.COMPARATOR_BY_EVENT_TYPE);
	sortedPostingRules.addAll(getAdministrativeOffice(request).getServiceAgreementTemplate().getActiveVisiblePostingRules());
	request.setAttribute("postingRules", sortedPostingRules);

	return mapping.findForward("viewPrices");
    }

    public ActionForward prepareEditPrice(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("executionYearBean", new ExecutionYearBean(ExecutionYear.readCurrentExecutionYear()));
	request.setAttribute("postingRule", rootDomainObject.readPostingRuleByOID(getRequestParameterAsInteger(request,
		"postingRuleId")));
	return mapping.findForward("editPrice");
    }

    public ActionForward changeExecutionYearPostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ExecutionYearBean executionYearBean = (ExecutionYearBean) getObjectFromViewState("executionYearBean");

	PostingRule postingRule = PartialRegistrationRegimeRequestPR.readMostRecentPostingRuleForExecutionYear(executionYearBean
		.getExecutionYear());

	request.setAttribute("postingRule", postingRule);
	request.setAttribute("executionYearBean", executionYearBean);

	return mapping.findForward("editPrice");
    }

    abstract protected AdministrativeOffice getAdministrativeOffice(final HttpServletRequest request);

    public static class ExecutionYearBean implements java.io.Serializable {
	private ExecutionYear executionYear;

	public ExecutionYearBean(ExecutionYear executionYear) {
	    this.executionYear = executionYear;
	}

	public ExecutionYear getExecutionYear() {
	    return executionYear;
	}

	public void setExecutionYear(ExecutionYear executionYear) {
	    this.executionYear = executionYear;
	}
    }

}
