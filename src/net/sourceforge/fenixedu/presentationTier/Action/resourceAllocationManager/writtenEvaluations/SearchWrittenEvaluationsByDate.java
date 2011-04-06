package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.writtenEvaluations;

import java.text.ParseException;
import java.util.Date;
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

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.utl.ist.fenix.tools.util.DateFormatUtil;
import pt.utl.ist.fenix.tools.util.StringAppender;

public class SearchWrittenEvaluationsByDate extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	final AcademicInterval academicInterval = AcademicInterval.getAcademicIntervalFromResumedString((String) request
		.getAttribute(PresentationConstants.ACADEMIC_INTERVAL));
	DynaActionForm dynaActionForm = (DynaActionForm) form;
	dynaActionForm.set(PresentationConstants.ACADEMIC_INTERVAL, academicInterval.getResumedRepresentationInStringFormat());
	return mapping.findForward("show");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	final DynaActionForm dynaActionForm = (DynaActionForm) form;

	final Date day = getDate(dynaActionForm);
	final Date begin = getTimeDateFromForm(dynaActionForm, "beginningHour", "beginningMinute");
	final Date end = getTimeDateFromForm(dynaActionForm, "endHour", "endMinute");

	return search(mapping, request, day, begin, end, dynaActionForm);
    }

    public ActionForward returnToSearchPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final DynaActionForm dynaActionForm = (DynaActionForm) form;

	final String date = request.getParameter("date");
	final String selectedBegin = request.getParameter("selectedBegin");
	final String selectedEnd = request.getParameter("selectedEnd");

	dynaActionForm.set("year", date.substring(0, 4));
	dynaActionForm.set("month", date.substring(5, 7));
	dynaActionForm.set("day", date.substring(8, 10));

	final Date begin;
	if (selectedBegin != null && selectedBegin.length() > 0) {
	    dynaActionForm.set("beginningHour", selectedBegin.substring(0, 2));
	    dynaActionForm.set("beginningMinute", selectedBegin.substring(3, 5));
	    begin = DateFormatUtil.parse("HH:mm", selectedBegin);
	} else {
	    begin = null;
	}
	final Date end;
	if (selectedEnd != null && selectedEnd.length() > 0) {
	    dynaActionForm.set("endHour", selectedEnd.substring(0, 2));
	    dynaActionForm.set("endMinute", selectedEnd.substring(3, 5));
	    end = DateFormatUtil.parse("HH:mm", selectedEnd);
	} else {
	    end = null;
	}

	return search(mapping, request, DateFormatUtil.parse("yyyy/MM/dd", date), begin, end, dynaActionForm);
    }
    
    public static boolean isEvalBetweenDates(WrittenEvaluation eval, Date begin , Date end) {
	final String format = "HH:mm";
//	return (DateFormatUtil.isInBetween(format, eval.getBeginningDate(), begin, end) || 
//		DateFormatUtil.isInBetween(format, eval.getEndDate(), begin, end)) || 
//		(DateFormatUtil.isInBetweenOpenEnd(format, begin, eval.getBeginningDate(), eval.getEndDate()) || 
//			DateFormatUtil.isInBetween(format, end, eval.getBeginningDate(), eval.getEndDate()));
	if (begin == null || DateFormatUtil.equalDates(format, begin, eval.getBeginningDate())) {
		if (end == null || DateFormatUtil.equalDates(format, end, eval.getEndDate())) {
		    return true;
		}
	}
	return false;
    }
    
    public ActionForward search(ActionMapping mapping, HttpServletRequest request, final Date day, final Date begin,
	    final Date end, DynaActionForm dynaActionForm) throws Exception {
	Integer totalOfStudents = 0;
	AcademicInterval academicInterval = getAcademicInterval(dynaActionForm, request);
	final Set<WrittenEvaluation> writtenEvaluations = new HashSet<WrittenEvaluation>();
	for (final ExecutionCourse executionCourse : ExecutionCourse.filterByAcademicInterval(academicInterval)) {
	    for (final Evaluation evaluation : executionCourse.getAssociatedEvaluations()) {
		if (evaluation instanceof WrittenEvaluation) {
		    final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) evaluation;
		    final Date evaluationDate = writtenEvaluation.getDayDate();
		    if (evaluationDate != null) {
			if (DateFormatUtil.equalDates("yyyy/MM/dd", day, evaluationDate)) {
			    if (isEvalBetweenDates(writtenEvaluation,begin,end)) {
				    if (!writtenEvaluations.contains(writtenEvaluation)) {
					totalOfStudents += writtenEvaluation.getCountStudentsEnroledAttendingExecutionCourses();
				    }
				    writtenEvaluations.add(writtenEvaluation);
				}
			    }
			}
		    }
		}
	    }
	request.setAttribute("availableRoomIndicationMsg",BundleUtil.getStringFromResourceBundle("resources.ResourceAllocationManagerResources", "info.total.students.vs.available.seats", totalOfStudents.toString(), Room.countAllAvailableSeatsForExams().toString()));
	request.setAttribute("writtenEvaluations",writtenEvaluations);
	return mapping.findForward("show");
    }

    private AcademicInterval getAcademicInterval(DynaActionForm dynaActionForm, HttpServletRequest request)
	    throws FenixFilterException, FenixServiceException {
	return AcademicInterval.getAcademicIntervalFromResumedString(dynaActionForm
		.getString(PresentationConstants.ACADEMIC_INTERVAL));
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