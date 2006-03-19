package net.sourceforge.fenixedu.presentationTier.Action.sop.writtenEvaluations;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import pt.utl.ist.fenix.tools.util.StringAppender;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class SearchWrittenEvaluationsByDate extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("show");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	final DynaActionForm dynaActionForm = (DynaActionForm) form;

    	final Date day = getDate(dynaActionForm);
    	final Date begin = getTimeDateFromForm(dynaActionForm, "beginningHour", "beginningMinute");
    	final Date end = getTimeDateFromForm(dynaActionForm, "endHour", "endMinute");

        return search(mapping, request, day, begin, end);
    }

    public ActionForward returnToSearchPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final DynaActionForm dynaActionForm = (DynaActionForm) form;

        final String date = request.getParameter("date");

        dynaActionForm.set("year", date.substring(0, 4));
        dynaActionForm.set("month", date.substring(5, 7));
        dynaActionForm.set("day", date.substring(8, 10));

        return search(mapping, request, DateFormatUtil.parse("yyyy/MM/dd", date), null, null);
    }

    public ActionForward search(ActionMapping mapping, HttpServletRequest request,
            final Date day, final Date begin, final Date end) throws Exception {
        final ExecutionPeriod executionPeriod = getExecutionPeriod(request);
        final Set<WrittenEvaluation> writtenEvaluations = new HashSet<WrittenEvaluation>();
        for (final ExecutionCourse executionCourse : executionPeriod.getAssociatedExecutionCourses()) {
            for (final Evaluation evaluation : executionCourse.getAssociatedEvaluations()) {
                if (evaluation instanceof WrittenEvaluation) {
                    final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) evaluation;
                    if (DateFormatUtil.equalDates("yyyy/MM/dd", day, writtenEvaluation.getDayDate())) {
                        if (begin == null || DateFormatUtil.equalDates("HH:mm", begin, writtenEvaluation.getBeginningDate())) {
                            if (end == null || DateFormatUtil.equalDates("HH:mm", end, writtenEvaluation.getEndDate())) {
                                writtenEvaluations.add(writtenEvaluation);
                            }
                        }
                    }
                }
            }
        }

        request.setAttribute("writtenEvaluations", writtenEvaluations);

        return mapping.findForward("show");
    }

	private ExecutionPeriod getExecutionPeriod(HttpServletRequest request) throws FenixFilterException, FenixServiceException {
		final IUserView userView = SessionUtils.getUserView(request);

		final String executionPeriodString = (String) request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID);
		final Integer executionPeriodID = Integer.valueOf(executionPeriodString);

		final Object[] args = { ExecutionPeriod.class, executionPeriodID };
		return (ExecutionPeriod) ServiceUtils.executeService(userView, "ReadDomainObject", args);
	}

	private Date getDate(final DynaActionForm dynaActionForm) throws ParseException {
		final String yearString = dynaActionForm.getString("year");
		final String monthString = dynaActionForm.getString("month");
		final String dayString = dynaActionForm.getString("day");

		final String dateString = StringAppender.append(yearString, "/", monthString, "/", dayString);

		return DateFormatUtil.parse("yyyy/MM/dd", dateString);
	}

	private Date getTimeDateFromForm(final DynaActionForm dynaActionForm, final String hourField, final String minuteField) 
			throws ParseException {
		final String hourString = dynaActionForm.getString(hourField);
		final String minuteString = dynaActionForm.getString(minuteField);
		return getTimeDate(hourString, minuteString);
	}

	private Date getTimeDate(final String hourString, final String minuteString) throws ParseException {
		if (valid(hourString) && valid(minuteString)) {
			final String timeDateString = StringAppender.append(hourString, ":", minuteString);
			return DateFormatUtil.parse("HH:mm", timeDateString);
		} else {
			return null;
		}
	}

	private boolean valid(final String integerString) {
		return integerString != null && integerString.length() > 0 && StringUtils.isNumeric(integerString);
	}

}