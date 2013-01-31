package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.writtenEvaluations;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.util.BundleUtil;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(
		module = "resourceAllocationManager",
		path = "/searchWrittenEvaluationsByDate",
		input = "/searchWrittenEvaluationsByDate.do?method=prepare&page=0",
		attribute = "examSearchByDateForm",
		formBean = "examSearchByDateForm",
		scope = "request",
		parameter = "method")
@Forwards(value = { @Forward(name = "show", path = "df.page.showWrittenEvaluationsByDate") })
public class SearchWrittenEvaluationsByDate extends FenixContextDispatchAction {

	public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		final AcademicInterval academicInterval =
				AcademicInterval.getAcademicIntervalFromResumedString((String) request
						.getAttribute(PresentationConstants.ACADEMIC_INTERVAL));
		DynaActionForm dynaActionForm = (DynaActionForm) form;
		dynaActionForm.set(PresentationConstants.ACADEMIC_INTERVAL, academicInterval.getResumedRepresentationInStringFormat());
		return mapping.findForward("show");
	}

	public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		final DynaActionForm dynaActionForm = (DynaActionForm) form;

		final LocalDate day = getDate(dynaActionForm);
		LocalTime begin = getTimeDateFromForm(dynaActionForm, "beginningHour", "beginningMinute");
		if (begin == null) {
			begin = new LocalTime(0, 0, 0);
		}
		LocalTime end = getTimeDateFromForm(dynaActionForm, "endHour", "endMinute");
		if (end == null) {
			end = new LocalTime(23, 59, 59);
		}

		return search(mapping, request, day, begin, end, dynaActionForm);
	}

	public ActionForward returnToSearchPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		final DynaActionForm dynaActionForm = (DynaActionForm) form;

		final String date = request.getParameter("date");
		final String selectedBegin = request.getParameter("selectedBegin");
		final String selectedEnd = request.getParameter("selectedEnd");

		final String year = date.substring(0, 4);
		final String month = date.substring(5, 7);
		final String day = date.substring(8, 10);
		dynaActionForm.set("year", year);
		dynaActionForm.set("month", month);
		dynaActionForm.set("day", day);

		final LocalTime begin;
		if (selectedBegin != null && selectedBegin.length() > 0) {
			final String hour = selectedBegin.substring(0, 2);
			final String minute = selectedBegin.substring(3, 5);
			dynaActionForm.set("beginningHour", hour);
			dynaActionForm.set("beginningMinute", minute);
			begin = getTimeDateFromForm(hour, minute);
		} else {
			begin = new LocalTime(0, 0, 0);
		}
		final LocalTime end;
		if (selectedEnd != null && selectedEnd.length() > 0) {
			final String hour = selectedEnd.substring(0, 2);
			final String minute = selectedEnd.substring(3, 5);
			dynaActionForm.set("endHour", selectedEnd.substring(0, 2));
			dynaActionForm.set("endMinute", selectedEnd.substring(3, 5));
			end = getTimeDateFromForm(hour, minute);
		} else {
			end = new LocalTime(23, 59, 59);
		}

		final LocalDate localDate = new LocalDate(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));

		return search(mapping, request, localDate, begin, end, dynaActionForm);
	}

	public static boolean isEvalBetweenDates(final WrittenEvaluation eval, final LocalTime begin, final LocalTime end) {
		final HourMinuteSecond bhms = eval.getBeginningDateHourMinuteSecond();
		final HourMinuteSecond ehms = eval.getEndDateHourMinuteSecond();
		return bhms.toLocalTime().isBefore(end) && ehms.toLocalTime().isAfter(begin);
	}

	public ActionForward search(ActionMapping mapping, HttpServletRequest request, final LocalDate day, final LocalTime begin,
			final LocalTime end, DynaActionForm dynaActionForm) throws Exception {
		Integer totalOfStudents = 0;
		AcademicInterval academicInterval = getAcademicInterval(dynaActionForm, request);
		final Set<WrittenEvaluation> writtenEvaluations = new HashSet<WrittenEvaluation>();
		for (final ExecutionCourse executionCourse : ExecutionCourse.filterByAcademicInterval(academicInterval)) {
			for (final Evaluation evaluation : executionCourse.getAssociatedEvaluations()) {
				if (evaluation instanceof WrittenEvaluation) {
					final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) evaluation;
					final LocalDate evaluationDate = writtenEvaluation.getDayDateYearMonthDay().toLocalDate();
					if (evaluationDate != null && evaluationDate.equals(day) && isEvalBetweenDates(writtenEvaluation, begin, end)) {
						if (!writtenEvaluations.contains(writtenEvaluation)) {
							totalOfStudents += writtenEvaluation.getCountStudentsEnroledAttendingExecutionCourses();
						}
						writtenEvaluations.add(writtenEvaluation);
					}
				}
			}
		}
		request.setAttribute("availableRoomIndicationMsg", BundleUtil.getStringFromResourceBundle(
				"resources.ResourceAllocationManagerResources", "info.total.students.vs.available.seats",
				totalOfStudents.toString(), Room.countAllAvailableSeatsForExams().toString()));
		request.setAttribute("writtenEvaluations", writtenEvaluations);
		return mapping.findForward("show");
	}

	private AcademicInterval getAcademicInterval(DynaActionForm dynaActionForm, HttpServletRequest request)
			throws FenixFilterException, FenixServiceException {
		return AcademicInterval.getAcademicIntervalFromResumedString(dynaActionForm
				.getString(PresentationConstants.ACADEMIC_INTERVAL));
	}

	private LocalDate getDate(final DynaActionForm dynaActionForm) throws ParseException {
		final String yearString = dynaActionForm.getString("year");
		final String monthString = dynaActionForm.getString("month");
		final String dayString = dynaActionForm.getString("day");
		return new LocalDate(Integer.parseInt(yearString), Integer.parseInt(monthString), Integer.parseInt(dayString));
	}

	private LocalTime getTimeDateFromForm(final DynaActionForm dynaActionForm, final String hourField, final String minuteField)
			throws ParseException {
		final String hourString = dynaActionForm.getString(hourField);
		final String minuteString = dynaActionForm.getString(minuteField);
		return hourString == null || hourString.isEmpty() || minuteString == null || minuteString.isEmpty() ? null : getTimeDateFromForm(
				hourString, minuteString);
	}

	private LocalTime getTimeDateFromForm(final String hourString, final String minuteString) throws ParseException {
		return new LocalTime(Integer.parseInt(hourString), Integer.parseInt(minuteString), 0);
	}

	private boolean valid(final String integerString) {
		return integerString != null && integerString.length() > 0 && StringUtils.isNumeric(integerString);
	}

}