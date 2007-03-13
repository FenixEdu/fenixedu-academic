package net.sourceforge.fenixedu.presentationTier.Action.managementAssiduousness;

import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTimeFieldType;
import org.joda.time.YearMonthDay;

public class MonthClosureDispatchAction extends FenixDispatchAction {

    public ActionForward prepareToCloseMonth(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	YearMonth yearMonthToExport = getYearMonthToClose(false);
	YearMonth yearMonth = new YearMonth(2007, 01);
	if (yearMonthToExport != null) {
	    request.setAttribute("yearMonthToExport", yearMonthToExport);
	    yearMonth = new YearMonth(yearMonthToExport.getYear(), yearMonthToExport.getNumberOfMonth());
	    yearMonth.addMonth();
	}
	request.setAttribute("yearMonth", yearMonth);
	return mapping.findForward("close-month");
    }

    private YearMonth getYearMonthToClose(boolean extraWork) {
	ClosedMonth lastClosedMonth = ClosedMonth.getLastMonthClosed(extraWork);
	if (lastClosedMonth == null) {
	    return null;
	}
	return new YearMonth(lastClosedMonth.getClosedYearMonth().get(DateTimeFieldType.year()),
		lastClosedMonth.getClosedYearMonth().get(DateTimeFieldType.monthOfYear()));
    }

    public ActionForward closeMonth(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonth");
	if (yearMonth != null && yearMonth.getCanCloseMonth()) {
	    final YearMonthDay beginDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth()
		    .ordinal() + 1, 01);
	    int endDay = beginDate.dayOfMonth().getMaximumValue();
	    if (yearMonth.getYear() == new YearMonthDay().getYear()
		    && yearMonth.getMonth().ordinal() + 1 == new YearMonthDay().getMonthOfYear()) {
		endDay = new YearMonthDay().getDayOfMonth();
	    }
	    final YearMonthDay endDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth()
		    .ordinal() + 1, endDay);
	    final IUserView userView = SessionUtils.getUserView(request);
	    ServiceUtils.executeService(userView, "CloseAssiduousnessMonth", new Object[] { beginDate,
		    endDate });
	}
	RenderUtils.invalidateViewState();
	return prepareToCloseMonth(mapping, actionForm, request, response);
    }

    public ActionForward exportClosedMonth(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	YearMonth yearMonth = (YearMonth) getRenderedObject("yearMonthToExport");
	if (yearMonth != null && yearMonth.getIsThisYearMonthClosed()) {
	    final YearMonthDay beginDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth()
		    .ordinal() + 1, 01);
	    int endDay = beginDate.dayOfMonth().getMaximumValue();
	    if (yearMonth.getYear() == new YearMonthDay().getYear()
		    && yearMonth.getMonth().ordinal() + 1 == new YearMonthDay().getMonthOfYear()) {
		endDay = new YearMonthDay().getDayOfMonth();
	    }
	    final YearMonthDay endDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth()
		    .ordinal() + 1, endDay);
	    final IUserView userView = SessionUtils.getUserView(request);
	    final ClosedMonth closedMonth = ClosedMonth.getClosedMonth(yearMonth);
	    final String result = (String) ServiceUtils.executeService(userView, "ExportClosedMonth",
		    new Object[] { closedMonth, beginDate, endDate });
	    response.setContentType("text/plain");
	    response.addHeader("Content-Disposition", "attachment; filename=Telep.dat");
	    byte[] data = result.getBytes();
	    response.setContentLength(data.length);
	    ServletOutputStream writer = response.getOutputStream();
	    writer.write(data);
	    writer.flush();
	    writer.close();
	    response.flushBuffer();
	    return mapping.findForward("");
	}
	return prepareToCloseMonth(mapping, actionForm, request, response);
    }

    // public ActionForward prepareToCloseExtraWorkMonth(ActionMapping
    // mapping, ActionForm actionForm,
    // HttpServletRequest request, HttpServletResponse response) {
    // request.setAttribute("yearMonth", getYearMonthToClose(true));
    // return mapping.findForward("close-extra-work-month");
    // }
    //
    // public ActionForward closeExtraWorkMonth(ActionMapping mapping,
    // ActionForm actionForm,
    // HttpServletRequest request, HttpServletResponse response) throws
    // Exception {
    //
    // YearMonth yearMonth = null;
    // ViewState viewState = (ViewState) RenderUtils.getViewState();
    // if (viewState != null) {
    // yearMonth = (YearMonth) viewState.getMetaObject().getObject();
    // }
    // if (yearMonth == null) {
    // // erro
    // return prepareToCloseMonth(mapping, actionForm, request, response);
    // // } else if (!yearMonth.equals(getYearMonthToClose())) {
    // // // erro
    // // return prepareToCloseMonth(mapping, actionForm, request,
    // response);
    // }
    //
    // YearMonthDay beginDate = new YearMonthDay(yearMonth.getYear(),
    // yearMonth.getMonth().ordinal() + 1, 01);
    // int endDay = beginDate.dayOfMonth().getMaximumValue();
    // if (yearMonth.getYear() == new YearMonthDay().getYear()
    // && yearMonth.getMonth().ordinal() + 1 == new
    // YearMonthDay().getMonthOfYear()) {
    // endDay = new YearMonthDay().getDayOfMonth();
    // // naõ pode ser este mês!!!!
    // }
    //
    // YearMonthDay endDate = new YearMonthDay(yearMonth.getYear(),
    // yearMonth.getMonth().ordinal() + 1,
    // endDay);
    // final IUserView userView = SessionUtils.getUserView(request);
    // Partial p = new Partial().with(DateTimeFieldType.monthOfYear(),
    // 11).with(
    // DateTimeFieldType.year(), 2006);
    // ClosedMonth closedMonth = getMonthToCloseExtraWork(p);
    // String result = (String) ServiceUtils.executeService(userView,
    // "CloseExtraWorkMonth",
    // new Object[] { closedMonth });
    // response.setContentType("text/plain");
    // ResourceBundle bundleEnumeration =
    // ResourceBundle.getBundle("resources.EnumerationResources",
    // LanguageUtils.getLocale());
    // String month =
    // bundleEnumeration.getString(yearMonth.getMonth().toString());
    // response.addHeader("Content-Disposition", new
    // StringBuilder("attachment; filename=").append(
    // month).append("-").append(yearMonth.getYear()).toString()
    // + ".txt");
    //
    // byte[] data = result.getBytes();
    // response.setContentLength(data.length);
    // ServletOutputStream writer = response.getOutputStream();
    // writer.write(data);
    // writer.flush();
    // writer.close();
    // response.flushBuffer();
    //
    // return mapping.findForward("");
    // }
    //
    // public ClosedMonth getMonthToCloseExtraWork(Partial yearMonth) {
    // for (ClosedMonth closedMonth :
    // RootDomainObject.getInstance().getClosedMonths()) {
    // if (closedMonth.getClosedYearMonth().equals(yearMonth)) {
    // return closedMonth;
    // }
    // }
    // return null;
    // }
}
