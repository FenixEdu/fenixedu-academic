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
import net.sourceforge.fenixedu.stm.TransactionReport;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.YearMonthDay;

/**
 * @author Luis Cruz
 */
public class TransactionSystemDA extends FenixDispatchAction {

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

//        final YearMonthDay today = new YearMonthDay();
//        final TransactionReport transactionReport = new TransactionReport(today, today.minusDays(7));
//        request.setAttribute("transactionReport", transactionReport);

        return mapping.findForward("Show");
    }

    public ActionForward viewChart(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws Exception {

        final YearMonthDay today = new YearMonthDay();
        final TransactionReport transactionReport = new TransactionReport(today.minusDays(7), today);
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