package net.sourceforge.fenixedu.presentationTier.Action.manager.payments;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.util.sibs.SibsOutgoingPaymentFile;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.YearMonthDay;

public class DegreePaymentsManagementDA extends FenixDispatchAction {

    public ActionForward preparePrintGratuityLetters(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	return mapping.findForward("preparePrintGratuityLetters");
    }

    public ActionForward printGratuityLetters(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	request.setAttribute("gratuityLetterDTOs", ServiceUtils.executeService(getUserView(request),
		"BuildInformationForDegreeGratuityLetters", new Object[] { ExecutionYear
			.readCurrentExecutionYear() }));

	return mapping.findForward("printGratuityLetters");
    }

    public ActionForward prepareGenerateSibsOutgoingFile(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	return mapping.findForward("prepareGenerateSibsOutgoingFile");
    }

    public ActionForward generateSibsOutgoingFile(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws IOException,
	    FenixFilterException, FenixServiceException {

	final SibsOutgoingPaymentFile sibsOutgoingPaymentFile = (SibsOutgoingPaymentFile) ServiceUtils
		.executeService(getUserView(request),
			"GenerateDegreeAdministrativeOfficeSibsOutgoingPaymentFile",
			new Object[] { ExecutionYear.readCurrentExecutionYear() });

	response.setContentType("application/octet-stream");
	response.setHeader("Content-disposition", "attachment; filename=SIBS-"
		+ new YearMonthDay().toString("dd-MM-yyyy") + ".txt");
	final ServletOutputStream writer = response.getOutputStream();
	writer.write(sibsOutgoingPaymentFile.render().getBytes());
	writer.flush();

	return null;
    }

}
