package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.log.requests.ExceptionType;
import net.sourceforge.fenixedu.domain.log.requests.RequestLog;
import net.sourceforge.fenixedu.domain.log.requests.RequestLogDay;
import net.sourceforge.fenixedu.domain.log.requests.RequestLogMonth;
import net.sourceforge.fenixedu.domain.log.requests.RequestLogYear;
import net.sourceforge.fenixedu.domain.log.requests.RequestMapping;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/errorReport", module = "manager")
@Forwards({ @Forward(name = "selectDay", path = "/manager/selectDay.jsp"),
		@Forward(name = "errorsList", path = "/manager/listErrors.jsp"),
		@Forward(name = "viewReportDetail", path = "/manager/viewReportDetail.jsp"),
		@Forward(name = "viewFilteredData", path = "/manager/viewFilteredData.jsp"),
		@Forward(name = "selectInterval", path = "/manager/selectInterval.jsp") })
public class ErrorLogDispatchAction extends FenixDispatchAction {

	public static class RequestLogDayBean implements Serializable {
		private RequestLogDay day;
		private RequestLogMonth month;
		private RequestLogYear year;

		public RequestLogDay getDay() {
			return day;
		}

		public void setDay(RequestLogDay day) {
			this.day = day;;
		}

		public RequestLogMonth getMonth() {
			return month;
		}

		public void setMonth(RequestLogMonth month) {
			this.month = month;;
		}

		public RequestLogYear getYear() {
			return year;
		}

		public void setYear(RequestLogYear year) {
			this.year = year;
		}
	}

	private List<String[]> prettyStackTrace(String stackTrace) {
		String[] lines = stackTrace.split("\n");
		ArrayList<String[]> elements = new ArrayList<String[]>();
		for (String line : lines) {
			if (!line.contains("StackTrace:")) {
				Pattern pattern =
						Pattern.compile("\\s*([0-9a-z_A-Z$\\.]+)\\.([0-9_a-zA-Z$]+)\\.([0-9_a-zA-Z$]+)\\(([0-9a-z_A-Z$\\.]+):([0-9]+)\\)");
				Matcher matcher = pattern.matcher(line);
				if (matcher.matches()) {
					String[] s = { matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5) };
					elements.add(s);
				}
			}
		}
		return elements;
	}

	private List<String[]> processAttributes(String toProcess) {
		Pattern pattern = Pattern.compile("(Request)?Element:([^\\s]+)\\s(Request)?Element\\sValue:([^\\s]+)");
		Matcher matcher = pattern.matcher(toProcess);
		ArrayList<String[]> elements = new ArrayList<String[]>();
		while (matcher.find()) {
			elements.add(new String[] { matcher.group(2), matcher.group(4) });
		}
		return elements;
	}

	private List<RequestLog> sliceRequestLogList(int page, List<RequestLog> requestLogsOriginal) {
		int itemsPerPage = 100;

		int pageLowerLimit = (page - 1) * itemsPerPage;
		int pageHighLimit = itemsPerPage * page >= requestLogsOriginal.size() ? requestLogsOriginal.size() : itemsPerPage * page;

		return requestLogsOriginal.subList(pageLowerLimit, pageHighLimit);

	}

	private int totalNumberOfPages(int total) {
		return (int) Math.ceil(((double) total / (double) 100));
	}

	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		RequestLogDayBean requestLogDayBean = getRenderedObject();
		if (requestLogDayBean != null) {
			if (requestLogDayBean.getDay() != null && requestLogDayBean.getMonth() != null && requestLogDayBean.getYear() != null) {
				getErrors(request, requestLogDayBean.getDay());

				return mapping.findForward("errorsList");
			}
			request.setAttribute("bean", requestLogDayBean);
		} else {
			request.setAttribute("bean", new RequestLogDayBean());
		}
		RenderUtils.invalidateViewState();
		return mapping.findForward("selectDay");
	}

	public ActionForward listErrors(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		getErrors(request, (RequestLogDay) RequestLogDay.fromExternalId(request.getParameter("requestLogDay")));

		return mapping.findForward("errorsList");
	}

	private void getErrors(HttpServletRequest request, RequestLogDay requestLogDay) {
		List<RequestLog> requestLogsOriginal = requestLogDay.getLogs();
		List<RequestLog> requestLogs = new ArrayList<RequestLog>(requestLogsOriginal);

		Collections.sort(requestLogs, new Comparator<RequestLog>() {
			@Override
			public int compare(RequestLog o1, RequestLog o2) {
				return -1 * o1.getRequestTime().compareTo(o2.getRequestTime());
			}
		});

		request.setAttribute("requestLogs", requestLogs);
		request.setAttribute("numberOfPages", totalNumberOfPages(requestLogsOriginal.size()));
		request.setAttribute("requestLogDay", requestLogDay);
	}

	private int pageNumber(String pageParam) {
		int page;

		if (pageParam != null && pageParam != "" && pageParam != "0") {
			page = java.lang.Integer.parseInt(pageParam);
		} else {
			page = 1;
		}
		return page;
	}

	public List<String[]> processParameters(String paramsList) {
		if (paramsList == null) {
			return new ArrayList<String[]>();
		} else {
			String[] params = paramsList.split("&");
			ArrayList<String[]> elements = new ArrayList<String[]>();
			for (String param : params) {
				String[] keyAndValue = param.split("=");
				if (keyAndValue.length > 0) {
					elements.add(new String[] { keyAndValue[0], keyAndValue[1] });
				} else {
					elements.add(new String[] { keyAndValue[0], "" });
				}
			}
			return elements;
		}
	}

	public ActionForward reportDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixActionException {
		String oid = request.getParameter("oid");
		if (oid == null || oid == "") {
			throw new FenixActionException("oid is required");
		}

		RequestLog reportLog = RequestLog.fromExternalId(oid);

		if (reportLog == null) {
			throw new FenixActionException("invalid OID");
		}

		request.setAttribute("error", reportLog);
		request.setAttribute("requestParam", processParameters(reportLog.getQueryString()));
		request.setAttribute("requestAttr", processAttributes(reportLog.getRequestAttributes()));
		request.setAttribute("sessionAttr", processAttributes(reportLog.getSessionAttributes()));
		request.setAttribute("stackTrace", prettyStackTrace(reportLog.getErrorLog().getStackTrace()));
		return mapping.findForward("viewReportDetail");
	}

	private Predicate filterForParameter(String filter, final String parameter) {
		if (filter.equals("exception")) {
			final ExceptionType exceptionType = ExceptionType.fromExternalId(parameter);
			return new Predicate() {
				@Override
				public boolean evaluate(Object arg0) {
					return ((RequestLog) arg0).getErrorLog().getException() == exceptionType;
				}
			};
		} else if (filter.equals("url")) {
			final RequestMapping requestMapping = RequestMapping.fromExternalId(parameter);
			return new Predicate() {
				@Override
				public boolean evaluate(Object arg0) {
					return ((RequestLog) arg0).getMapping() == requestMapping;
				}
			};
		} else if (filter.equals("user")) {
			return new Predicate() {
				@Override
				public boolean evaluate(Object arg0) {
					return ((RequestLog) arg0).getRequester().equals(parameter);
				}
			};
		}
		return null;
	}

	private String nameForFilter(String filter) {
		if (filter.equals("exception")) {
			return "Excepção";
		} else if (filter.equals("url")) {
			return "URL";
		} else if (filter.equals("user")) {
			return "Utilizador";
		} else {
			return "Parametro";
		}
	}

	private String nameForParameter(String filter, String parameter) {
		if (filter.equals("exception")) {
			return ((ExceptionType) ExceptionType.fromExternalId(parameter)).getType();
		} else if (filter.equals("url")) {
			return ((RequestMapping) RequestMapping.fromExternalId(parameter)).getPath() + "?"
					+ ((RequestMapping) RequestMapping.fromExternalId(parameter)).getParameters();
		} else if (filter.equals("user")) {
			return parameter;
		} else {
			return "Parametro";
		}
	}

	private List<RequestLog> filterRequestLog(String filter, String parameter, RequestLogDayBean requestLogFromBean,
			RequestLogDayBean requestLogToBean) {
		RequestLogDay day = requestLogFromBean.getDay();

		List<RequestLog> requestLogs = new ArrayList<RequestLog>();

		while (day != null && day != requestLogToBean.getDay()) {
			requestLogs.addAll(day.getLogs());
			day = day.getNext();
		}
		if (day != null) {
			requestLogs.addAll(day.getLogs());
		}

		CollectionUtils.filter(requestLogs, filterForParameter(filter, parameter));
		return requestLogs;
	}

	public ActionForward filter(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws FenixActionException {

		RequestLogDayBean requestLogFromBean = getRenderedObject("from");
		RequestLogDayBean requestLogToBean = getRenderedObject("to");

		if (requestLogFromBean == null) {
			requestLogFromBean = new RequestLogDayBean();
		}

		if (requestLogToBean == null) {
			requestLogToBean = new RequestLogDayBean();
		}

		request.setAttribute("addr", "/errorReport.do?method=filter&filterBy=" + request.getParameter("filterBy") + "&parameter="
				+ request.getParameter("parameter"));

		if (requestLogFromBean.getDay() == null || requestLogToBean.getDay() == null) {
			request.setAttribute("from", requestLogFromBean);
			request.setAttribute("to", requestLogToBean);

			RenderUtils.invalidateViewState();
			return mapping.findForward("selectInterval");
		}

		String filteringType = request.getParameter("filterBy");
		String parameter = request.getParameter("parameter");

		if (parameter == null || filteringType == null) {
			throw new FenixActionException("parameter is required");
		}
		List<RequestLog> requestLogsFiltered = filterRequestLog(filteringType, parameter, requestLogFromBean, requestLogToBean);
		int total = requestLogsFiltered.size();

		Collections.sort(requestLogsFiltered, new Comparator<RequestLog>() {
			@Override
			public int compare(RequestLog o1, RequestLog o2) {

				return -1 * o1.getRequestTime().compareTo(o2.getRequestTime());
			}
		});
		request.setAttribute("filterBy", nameForFilter(filteringType));
		request.setAttribute("param", nameForParameter(filteringType, parameter));
		request.setAttribute("size", total);
		request.setAttribute("requestLogs", requestLogsFiltered);
		request.setAttribute("numberOfPages", totalNumberOfPages(total));
		return mapping.findForward("viewFilteredData");

	}
}
