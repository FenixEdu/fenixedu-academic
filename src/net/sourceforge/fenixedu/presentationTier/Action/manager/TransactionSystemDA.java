/*
 * Created on 2003/12/25
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.stm.TransactionAction;
import net.sourceforge.fenixedu.stm.TransactionReport;
import net.sourceforge.fenixedu.util.date.SerializationTool;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.YearMonthDay;

/**
 * @author Luis Cruz
 */
public class TransactionSystemDA extends FenixDispatchAction {

    private TransactionReport getTransactionReport(final HttpServletRequest request) {
	TransactionReport transactionReport = (TransactionReport) getRenderedObject();
	if (transactionReport == null) {
	    transactionReport = (TransactionReport) request.getAttribute("transactionReport");
	    if (transactionReport == null) {
		final YearMonthDay today = new YearMonthDay();
		transactionReport = new TransactionReport(today.minusDays(1), today, null, null);
	    }
	}
	request.setAttribute("transactionReport", transactionReport);
	return transactionReport;
    }

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
	getTransactionReport(request);
        return mapping.findForward("Show");
    }

    public ActionForward viewChart(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws Exception {

	final String startOfReportString = request.getParameter("startOfReport");
	final String endOfReportString = request.getParameter("endOfReport");
	final String actionString = request.getParameter("action");
	final String serverString = request.getParameter("server");
	final YearMonthDay startOfReport = SerializationTool.yearMonthDaySerialize(startOfReportString);
	final YearMonthDay endOfReport = SerializationTool.yearMonthDaySerialize(endOfReportString);
	final TransactionAction transactionAction = actionString == null || actionString.length() == 0 ? 
		null : TransactionAction.valueOf(actionString);
	final String server = serverString == null || serverString.length() == 0 ? null : serverString;
	final TransactionReport transactionReport = new TransactionReport(startOfReport, endOfReport, transactionAction, server);
	transactionReport.report();

        ServletOutputStream writer = null;
        try {
            writer = response.getOutputStream();
            response.setContentType("image/jpeg");
            writer.write(transactionReport.getChart());
            writer.flush();
        } catch (IOException e1) {
            throw new FenixActionException();
        } finally {
            writer.close();
            response.flushBuffer();
        }

        return null;
    }

}