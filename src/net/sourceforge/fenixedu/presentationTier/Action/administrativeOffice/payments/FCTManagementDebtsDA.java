package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.payments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.debts.FctScholarshipPhdGratuityContribuitionEvent;
import net.sourceforge.fenixedu.domain.phd.debts.PhdGratuityEvent;
import net.sourceforge.fenixedu.domain.phd.debts.PhdGratuityFctScholarshipExemption;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.Money;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/fctDebts", module = "academicAdminOffice")
@Forwards({ @Forward(name = "selectPhdStudent", path = "/academicAdminOffice/payments/selectPhdStudent.jsp"),
	@Forward(name = "showScolarship", path = "/academicAdminOffice/payments/showScolarship.jsp") })
public class FCTManagementDebtsDA extends FenixDispatchAction {

    public static class AmountBean implements Serializable {
	private Money value;

	public Money getValue() {
	    return value;
	}

	public void setValue(Money value) {
	    this.value = value;
	}
    }

    protected PhdIndividualProgramProcess getProcess(HttpServletRequest request) {
	final String processIdAttribute = (String) request.getAttribute("processId");
	return DomainObject.fromExternalId(processIdAttribute != null ? processIdAttribute : (String) request
		.getParameter("processId"));
    }

    public ActionForward viewDebtsForProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdIndividualProgramProcess process = getProcess(request);
	ArrayList<PhdGratuityFctScholarshipExemption> list = new ArrayList<PhdGratuityFctScholarshipExemption>();

	for (Event event : process.getPerson().getEvents()) {
	    if (event instanceof PhdGratuityEvent) {
		for (Exemption exemption : event.getExemptions()) {
		    if (exemption instanceof PhdGratuityFctScholarshipExemption) {
			PhdGratuityFctScholarshipExemption exemption2 = (PhdGratuityFctScholarshipExemption) exemption;
			list.add(exemption2);
		    }
		}
	    }
	}

	request.setAttribute("person", process.getPerson());
	request.setAttribute("debts", list);
	return mapping.findForward("selectPhdStudent");
    }

    public ActionForward prepareLiquidation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	String exemptionId = (String) request.getParameter("exemptiontId");
	PhdGratuityFctScholarshipExemption exemption = (PhdGratuityFctScholarshipExemption) Exemption.fromExternalId(exemptionId);
	request.setAttribute("processId", ((PhdGratuityEvent) exemption.getEvent()).getPhdIndividualProgramProcess()
		.getExternalId());
	request.setAttribute("exemption", exemption);
	request.setAttribute("person", exemption.getEvent().getPerson());
	AmountBean bean = new AmountBean();
	bean.setValue(exemption.getAmoutStillMissing());
	request.setAttribute("bean", bean);
	return mapping.findForward("showScolarship");
    }
    
    @Service
    public ActionForward liquidate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	String exemptionId = (String) request.getParameter("externalId");
	PhdGratuityFctScholarshipExemption exemption = (PhdGratuityFctScholarshipExemption) Exemption
		.fromExternalId(exemptionId == null ? (String) request.getAttribute("externalId") : exemptionId);
	FctScholarshipPhdGratuityContribuitionEvent event = exemption.getFctScholarshipPhdGratuityContribuitionEvent();
	AmountBean bean = getRenderedObject("bean");
	List<EntryDTO> list = new ArrayList<EntryDTO>();
	list.add(new EntryDTO(EntryType.FCT_SCOLARSHIP_PAYMENT, event, bean.getValue()));
	event.process(AccessControl.getPerson().getUser(), list, new AccountingTransactionDetailDTO(new DateTime(),
		PaymentMode.CASH));
	
	PhdGratuityEvent gratuityEvent = (PhdGratuityEvent) exemption.getEvent();
	PhdIndividualProgramProcess process = gratuityEvent.getPhdIndividualProgramProcess();
	request.setAttribute("processId", process.getExternalId());
	
	return viewDebtsForProcess(mapping, form, request, response);
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	String exemptionId = (String) request.getParameter("externalId");
	PhdGratuityFctScholarshipExemption exemption = (PhdGratuityFctScholarshipExemption) Exemption
		.fromExternalId(exemptionId == null ? (String) request.getAttribute("externalId") : exemptionId);
	PhdGratuityEvent event = (PhdGratuityEvent) exemption.getEvent();
	PhdIndividualProgramProcess process = event.getPhdIndividualProgramProcess();
	request.setAttribute("processId", process.getExternalId());
	return viewDebtsForProcess(mapping, form, request, response);
    }
}
