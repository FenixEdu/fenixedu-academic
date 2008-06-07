package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.WeeklyWorkLoad;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;

import pt.ist.fenixWebFramework.renderers.components.state.LifeCycleConstants;
import pt.ist.fenixWebFramework.renderers.components.state.ViewState;

public class WeeklyWorkLoadDA extends FenixDispatchAction {

    public enum IntervalType {
	LESSON_INTERVAL, EXAM_INTERVAL;
    }

    private static final Comparator<Attends> ATTENDS_COMPARATOR = new Comparator<Attends>() {
	public int compare(final Attends attends1, final Attends attends2) {
	    final ExecutionCourse executionCourse1 = attends1.getExecutionCourse();
	    final ExecutionCourse executionCourse2 = attends2.getExecutionCourse();
	    return executionCourse1.getNome().compareTo(executionCourse2.getNome());
	}
    };

    public class WeeklyWorkLoadView {
	final Interval executionPeriodInterval;

	final int numberOfWeeks;

	final Map<Attends, WeeklyWorkLoad[]> weeklyWorkLoadMap = new TreeMap<Attends, WeeklyWorkLoad[]>(
		ATTENDS_COMPARATOR);

	final Interval[] intervals;

	final IntervalType[] intervalTypes;

	public WeeklyWorkLoadView(final Interval executionPeriodInterval) {
	    this.executionPeriodInterval = executionPeriodInterval;
	    final Period period = executionPeriodInterval.toPeriod();
	    int extraWeek = period.getDays() > 0 ? 1 : 0;
	    numberOfWeeks = (period.getYears() * 12 + period.getMonths()) * 4 + period.getWeeks()
		    + extraWeek + 1;
	    intervals = new Interval[numberOfWeeks];
	    intervalTypes = new IntervalType[numberOfWeeks];
	    for (int i = 0; i < numberOfWeeks; i++) {
		final DateTime start = executionPeriodInterval.getStart().plusWeeks(i);
		final DateTime end = start.plusWeeks(1);
		intervals[i] = new Interval(start, end);
	    }
	}

	public void add(final Attends attends) {
	    final WeeklyWorkLoad[] weeklyWorkLoadArray = new WeeklyWorkLoad[numberOfWeeks];
	    for (final WeeklyWorkLoad weeklyWorkLoad : attends.getWeeklyWorkLoads()) {
		weeklyWorkLoadArray[weeklyWorkLoad.getWeekOffset()] = weeklyWorkLoad;
	    }
	    weeklyWorkLoadMap.put(attends, weeklyWorkLoadArray);
	}

	public Interval[] getIntervals() {
	    return intervals;
	}

	public Interval getExecutionPeriodInterval() {
	    return executionPeriodInterval;
	}

	public Map<Attends, WeeklyWorkLoad[]> getWeeklyWorkLoadMap() {
	    return weeklyWorkLoadMap;
	}
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final Collection<ExecutionSemester> executionSemesters = rootDomainObject.getExecutionPeriodsSet();
	final Set<ExecutionSemester> sortedExecutionPeriods = new TreeSet<ExecutionSemester>(
		executionSemesters);
	request.setAttribute("executionPeriods", sortedExecutionPeriods);

	final DynaActionForm dynaActionForm = (DynaActionForm) form;

	final Integer executionPeriodID = getExecutionPeriodID(dynaActionForm);
	final ExecutionSemester selectedExecutionPeriod = findExecutionPeriod(executionSemesters,
		executionPeriodID);
	request.setAttribute("selectedExecutionPeriod", selectedExecutionPeriod);

	dynaActionForm.set("executionPeriodID", selectedExecutionPeriod.getIdInternal().toString());

//	if (selectedExecutionPeriod.isCurrent()) {
//	    request.setAttribute("weeks", getWeeks(selectedExecutionPeriod));
//	}

	final Attends firstAttends = findFirstAttends(request, selectedExecutionPeriod);
	request.setAttribute("firstAttends", firstAttends);
	if (firstAttends != null) {
	    final Interval executionPeriodInterval = firstAttends.getWeeklyWorkLoadInterval();
	    final WeeklyWorkLoadView weeklyWorkLoadView = new WeeklyWorkLoadView(executionPeriodInterval);
	    request.setAttribute("weeklyWorkLoadView", weeklyWorkLoadView);

	    final Collection<Attends> attends = new ArrayList<Attends>();
	    request.setAttribute("attends", attends);

	    for (final Registration registration : getUserView(request).getPerson().getStudents()) {
		for (final Attends attend : registration.getOrderedAttends()) {
		    if (attend.getEnrolment() != null) {
			final ExecutionCourse executionCourse = attend.getExecutionCourse();
			if (executionCourse.getExecutionPeriod() == selectedExecutionPeriod) {
			    weeklyWorkLoadView.add(attend);
			    attends.add(attend);
			}
		    }
		}
	    }

	    request.setAttribute("weeklyWorkLoadBean", new WeeklyWorkLoadBean());
	}

	dynaActionForm.set("contact", null);
	dynaActionForm.set("autonomousStudy", null);
	dynaActionForm.set("other", null);

	return mapping.findForward("showWeeklyWorkLoad");
    }

    private static class Week {
	private int weekOffset;
	private Interval interval;

	public int getWeekOffset() {
	    return weekOffset;
	}
	public void setWeekOffset(int weekOffset) {
	    this.weekOffset = weekOffset;
	}
	public Interval getInterval() {
	    return interval;
	}
	public void setInterval(Interval interval) {
	    this.interval = interval;
	}

    }

    private SortedSet<Week> getWeeks(final ExecutionSemester executionSemester) {
	return null;
    }

    private Attends findFirstAttends(final HttpServletRequest request,
	    final ExecutionSemester selectedExecutionPeriod) throws FenixFilterException,
	    FenixServiceException {
	for (final Registration registration : getUserView(request).getPerson().getStudents()) {
	    for (final Attends attend : registration.getOrderedAttends()) {
		final ExecutionCourse executionCourse = attend.getExecutionCourse();
		if (executionCourse.getExecutionPeriod() == selectedExecutionPeriod
			&& attend.getEnrolment() != null) {
		    return attend;
		}
	    }
	}
	return null;
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	final WeeklyWorkLoadBean weeklyWorkLoadBean = getWeeklyWorkLoadBean(request);

	final Integer attendsID = weeklyWorkLoadBean.getAttendsID();
	final Integer contact = weeklyWorkLoadBean.getContact();
	final Integer autonomousStudy = weeklyWorkLoadBean.getAutonomousStudy();
	final Integer other = weeklyWorkLoadBean.getOther();

	create(request, attendsID, contact, autonomousStudy, other);

	return prepare(mapping, form, request, response);
    }

    public ActionForward createFromForm(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {
	final DynaActionForm dynaActionForm = (DynaActionForm) form;

	final Integer attendsID = getInteger(dynaActionForm, "attendsID");
	final Integer contact = getInteger(dynaActionForm, "contact");
	final Integer autonomousStudy = getInteger(dynaActionForm, "autonomousStudy");
	final Integer other = getInteger(dynaActionForm, "other");

	create(request, attendsID, contact, autonomousStudy, other);

	dynaActionForm.set("contact", null);
	dynaActionForm.set("autonomousStudy", null);
	dynaActionForm.set("other", null);

	return prepare(mapping, form, request, response);
    }

    public void create(final HttpServletRequest request, final Integer attendsID, final Integer contact,
	    final Integer autonomousStudy, final Integer other) throws FenixFilterException,
	    FenixServiceException {
	final Object[] args = { attendsID, contact, autonomousStudy, other };
	executeService(request, "CreateWeeklyWorkLoad", args);
    }

    private WeeklyWorkLoadBean getWeeklyWorkLoadBean(final HttpServletRequest request) {
	ViewState viewState = (ViewState) request.getAttribute(LifeCycleConstants.VIEWSTATE_PARAM_NAME);
	return (WeeklyWorkLoadBean) viewState.getMetaObject().getObject();
    }

    private Integer getExecutionPeriodID(final DynaActionForm dynaActionForm) {
	final String exeutionPeriodIDString = dynaActionForm.getString("executionPeriodID");
	return exeutionPeriodIDString == null || exeutionPeriodIDString.length() == 0 ? null : Integer
		.valueOf(exeutionPeriodIDString);
    }

    private ExecutionSemester findExecutionPeriod(final Collection<ExecutionSemester> executionSemesters,
	    final Integer executionPeriodID) {
	for (final ExecutionSemester executionSemester : executionSemesters) {
	    if (executionPeriodID == null && executionSemester.getState().equals(PeriodState.CURRENT)) {
		return executionSemester;
	    }
	    if (executionPeriodID != null && executionSemester.getIdInternal().equals(executionPeriodID)) {
		return executionSemester;
	    }
	}
	return null;
    }

}
