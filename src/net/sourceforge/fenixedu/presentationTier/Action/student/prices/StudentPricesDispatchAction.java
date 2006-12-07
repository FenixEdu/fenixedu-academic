package net.sourceforge.fenixedu.presentationTier.Action.student.prices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StudentPricesDispatchAction extends FenixDispatchAction {

    public ActionForward viewPrices(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	
	request.setAttribute("postingRulesByAdminOfficeType", getPostingRulesByAdminOfficeType());
	request.setAttribute("insurancePostingRule", getInsurancePR());
	
	return mapping.findForward("viewPrices");
    }

    private PostingRule getInsurancePR() {
	return rootDomainObject.getInstitutionUnit().getUnitServiceAgreementTemplate().findPostingRuleByEventType(EventType.INSURANCE);
    }

    private Map<AdministrativeOfficeType, List<PostingRule>> getPostingRulesByAdminOfficeType() {
	
	final Map<AdministrativeOfficeType, List<PostingRule>> postingRulesByAdminOfficeType = new HashMap<AdministrativeOfficeType, List<PostingRule>>();
	
	for (final AdministrativeOfficeType officeType : AdministrativeOfficeType.values()) {
	    final AdministrativeOffice office = AdministrativeOffice.readByAdministrativeOfficeType(officeType);
	    if (office != null) {
		final List<PostingRule> postingRules = new ArrayList<PostingRule>(office.getServiceAgreementTemplate().getActiveVisiblePostingRules());
		Collections.sort(postingRules, PostingRule.COMPARATOR_BY_EVENT_TYPE);
		postingRulesByAdminOfficeType.put(officeType, postingRules);
	    }
	}
	return postingRulesByAdminOfficeType;
    }
 
}
