package net.sourceforge.fenixedu.presentationTier.Action.manager.payments;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.debts.PhdGratuityPR;
import net.sourceforge.fenixedu.util.Money;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdPostingRules", module = "manager", formBeanClass = PostingRulesManagementDA.PostingRulesManagementForm.class)
@Forwards({
	@Forward(name = "showPhdProgramPostingRules", path = "/manager/payments/postingRules/management/phd/showPhdProgramPostingRules.jsp"),
	@Forward(name = "viewPostingRuleDetails", path = "/manager/payments/postingRules/management/phd/viewPostingRuleDetails.jsp"),
	@Forward(name = "editPhdProgramPostingRule", path = "/manager/payments/postingRules/management/phd/editPhdProgramPostingRule.jsp"),
	@Forward(name = "addPhdProgramPostingRule", path = "/manager/payments/postingRules/management/phd/addPhdProgramPostingRule.jsp")
})
public class PhdPostingRulesManagementDA extends PostingRulesManagementDA {

    public static class CreateGratuityPhdBean implements Serializable{
	DateTime startDate = new DateTime();
	DateTime endDate;
	Integer gratuity = PhdGratuityPR.STANDARD_GRATUITY;
	Double fineRate = PhdGratuityPR.STANDARD_FINE_RATE;
	
	public DateTime getStartDate() {
	    return startDate;
	}
	public void setStartDate(DateTime startDate) {
	    this.startDate = startDate;
	}
	public DateTime getEndDate() {
	    return endDate;
	}
	public void setEndDate(DateTime endDate) {
	    this.endDate = endDate;
	}
	public Integer getGratuity() {
	    return gratuity;
	}
	public void setGratuity(Integer gratuity) {
	    this.gratuity = gratuity;
	}
	public Double getFineRate() {
	    return fineRate;
	}
	public void setFineRate(Double fineRate) {
	    this.fineRate = fineRate;
	}
    }
    
    
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

    public ActionForward prepareAddGratuityPhdPostingRule (ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdProgram phdProgram = getDomainObject(request, "phdProgramId");
	request.setAttribute("phdProgram", phdProgram);
	request.setAttribute("bean",new CreateGratuityPhdBean());
	
	return mapping.findForward("addPhdProgramPostingRule");
    }
    
    @Service
    private void makeGratuityPostingRule(CreateGratuityPhdBean bean, PhdProgram phdProgram){
	new PhdGratuityPR(bean.getStartDate(), bean.getEndDate(), phdProgram.getServiceAgreementTemplate(), new Money(bean.getGratuity()),bean.getFineRate());
    }
    public ActionForward addGratuityPhdPostingRule(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdProgram phdProgram = getDomainObject(request, "phdProgramId");
	CreateGratuityPhdBean bean = (CreateGratuityPhdBean) getRenderedObject("bean");
	try{
	    makeGratuityPostingRule(bean, phdProgram);
	}catch (DomainException e) {
	    addErrorMessage(request, "bean", e.getMessage());
	    return prepareAddGratuityPhdPostingRule(mapping, form,request,response);
	}
	return showPhdProgramPostingRules(mapping, form,request,response);
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
