package net.sourceforge.fenixedu.presentationTier.Action.manager.payments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.QueueJob;
import net.sourceforge.fenixedu.domain.accounting.events.export.SIBSOutgoingPaymentFile;
import net.sourceforge.fenixedu.domain.accounting.events.export.SIBSOutgoingPaymentQueueJob;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/exportSIBSPayments", module = "manager")
@Forwards( {
	@Forward(name = "list-outgoing-payment-files", path = "/manager/payments/exportSIBS/listOutgoingPaymentFiles.jsp"),
	@Forward(name = "prepare-create-outgoing-payments-file", path = "/manager/payments/exportSIBS/prepareCreateOutgoingPaymentsFiles.jsp"),
	@Forward(name = "view-outgoing-payment-file", path = "/manager/payments/exportSIBS/viewOutgoingPaymentFile.jsp"),
	@Forward(name = "prepare-set-successful-sent-date", path = "/manager/payments/exportSIBS/prepareSetSuccessfulSentDate.jsp") })
public class ExportSIBSPaymentsDA extends FenixDispatchAction {

    public ActionForward prepareCreateOutgoingPaymentsFile(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	createPaymentFileDataBean(request);

	return mapping.findForward("prepare-create-outgoing-payments-file");
    }

    public ActionForward launchOutgoingPaymentsFileCreation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	SIBSOutgoingPaymentQueueJob.launchJob(getPaymentFileDataBeanFromViewState().getLastOutgoingPaymentFileSent());

	return listOutgoingPaymentsFile(mapping, actionForm, request, response);
    }

    public ActionForward launchOutgoingPaymentsFileCreationInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("sibsOutgoingPaymentFileDataBean", getPaymentFileDataBeanFromViewState());

	return mapping.findForward("prepare-create-outgoing-payments-file");
    }

    public ActionForward listOutgoingPaymentsFile(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("sibsOutgoingPaymentFiles", SIBSOutgoingPaymentFile.readGeneratedPaymentFiles());
	request.setAttribute("sibsOutgoingPaymentQueueJobs", SIBSOutgoingPaymentQueueJob.readAllSIBSOutgoingPaymentQueueJobs());
	request.setAttribute("canLaunchJob", !SIBSOutgoingPaymentQueueJob.hasExportationQueueJobToRun());

	return mapping.findForward("list-outgoing-payment-files");
    }

    public ActionForward viewOutgoingPaymentFile(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("sibsPaymentFile", getDomainObject(request, "paymentFileId"));

	return mapping.findForward("view-outgoing-payment-file");
    }

    public ActionForward cancelQueueJob(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	QueueJob job = getDomainObject(request, "queueJobId");
	job.cancel();

	return listOutgoingPaymentsFile(mapping, actionForm, request, response);
    }

    public ActionForward prepareSetSuccessfulSentPaymentsFileDate(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	SIBSOutgoingPaymentFile file = getDomainObject(request, "paymentFileId");

	request.setAttribute("sibsOutgoingPaymentFileDataBean", new SIBSOutgoingPaymentFileDataBean(file));

	return mapping.findForward("prepare-set-successful-sent-date");
    }

    public ActionForward setSuccessfulSentPaymentsFileDate(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request,
	    HttpServletResponse response) {
	SIBSOutgoingPaymentFile paymentFile = getPaymentFileDataBeanFromViewState().getPaymentFile();
	paymentFile.markAsSuccessfulSent(getPaymentFileDataBeanFromViewState().getLastOutgoingPaymentFileSent());
	
	return listOutgoingPaymentsFile(mapping, actionForm, request, response);
    }

    private SIBSOutgoingPaymentFileDataBean getPaymentFileDataBeanFromViewState() {
	return (SIBSOutgoingPaymentFileDataBean) getObjectFromViewState("sibs.outgoing.payment.file.data.bean");
    }

    private void createPaymentFileDataBean(HttpServletRequest request) {
	request.setAttribute("sibsOutgoingPaymentFileDataBean", new SIBSOutgoingPaymentFileDataBean());

    }

    public static class SIBSOutgoingPaymentFileDataBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DateTime lastOutgoingPaymentFileSent;
	
	private SIBSOutgoingPaymentFile paymentFile;

	public SIBSOutgoingPaymentFileDataBean() {
	    SIBSOutgoingPaymentFile lastSuccessfulSent = SIBSOutgoingPaymentFile.readLastSuccessfulSentPaymentFile();

	    lastOutgoingPaymentFileSent = lastSuccessfulSent != null ? lastSuccessfulSent.getSuccessfulSentDate()
		    : new DateTime();
	}

	public SIBSOutgoingPaymentFileDataBean(SIBSOutgoingPaymentFile paymentFile) {
	    this.paymentFile = paymentFile;
	}

	public DateTime getLastOutgoingPaymentFileSent() {
	    return this.lastOutgoingPaymentFileSent;
	}

	public void setLastOutgoingPaymentFileSent(DateTime value) {
	    this.lastOutgoingPaymentFileSent = value;
	}

	public SIBSOutgoingPaymentFile getPaymentFile() {
	    return this.paymentFile;
	}
    }

}
