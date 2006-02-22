package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.student.WeeklyWorkLoad;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Interval;
import org.joda.time.Period;

public class WeeklyWorkLoadDA extends FenixDispatchAction {

	private static final Comparator<Attends> ATTENDS_COMPARATOR = new Comparator<Attends>(){
		public int compare(final Attends attends1, final Attends attends2) {
			final ExecutionCourse executionCourse1 = attends1.getDisciplinaExecucao();
			final ExecutionCourse executionCourse2 = attends2.getDisciplinaExecucao();
			return executionCourse1 != executionCourse2 ?
					Collator.getInstance().compare(executionCourse1.getNome(), executionCourse2.getNome())
					: attends1.getAluno().getNumber().compareTo(attends2.getAluno().getNumber());
		}};

	private static final Comparator<ExecutionCourse> EXECUTION_COURSE_COMPARATOR = new BeanComparator("nome", Collator.getInstance());

	public class WeeklyWorkLoadView {
        final Interval executionPeriodInterval;
        final int numberOfWeeks;

        final Map<Attends, WeeklyWorkLoad[]> weeklyWorkLoadMap = new TreeMap<Attends, WeeklyWorkLoad[]>(ATTENDS_COMPARATOR);
        final Interval[] intervals;

        public WeeklyWorkLoadView(final Interval executionPeriodInterval) {
            this.executionPeriodInterval = executionPeriodInterval;
            final Period period = executionPeriodInterval.toPeriod();
            int extraWeek = period.getDays() > 0 ? 1 : 0;
            numberOfWeeks = (period.getYears() * 12 + period.getMonths()) * 4 + period.getWeeks() + extraWeek;
            intervals = new Interval[numberOfWeeks];
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

    public ActionForward prepare(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {

        final Object[] args = { ExecutionPeriod.class };
        final List<ExecutionPeriod> executionPeriods = (List<ExecutionPeriod>) executeService(request, "ReadAllDomainObjects", args);
        final Set<ExecutionPeriod> sortedExecutionPeriods = new TreeSet<ExecutionPeriod>(executionPeriods);
        request.setAttribute("executionPeriods", sortedExecutionPeriods);

        final DynaActionForm dynaActionForm = (DynaActionForm) form;

        final Integer executionPeriodID = getExecutionPeriodID(dynaActionForm);
        final ExecutionPeriod selectedExecutionPeriod = findExecutionPeriod(executionPeriods, executionPeriodID);
        dynaActionForm.set("executionPeriodID", selectedExecutionPeriod.getIdInternal().toString());

        final String degreeCurricularPlanIDString = (String) dynaActionForm.get("degreeCurricularPlanID");
        final Integer degreeCurricularPlanID = degreeCurricularPlanIDString == null || degreeCurricularPlanIDString.length() == 0 ?
        		null : Integer.valueOf(degreeCurricularPlanIDString);
        final Object[] args2 = { DegreeCurricularPlan.class, degreeCurricularPlanID };
        final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) executeService(request, "ReadDomainObject", args2);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);

        final Set<ExecutionCourse> executionCourses = new TreeSet<ExecutionCourse>(EXECUTION_COURSE_COMPARATOR);
        request.setAttribute("executionCourses", executionCourses);

        final Interval executionPeriodInterval = getExecutionPeriodInterval(selectedExecutionPeriod);
        final WeeklyWorkLoadView weeklyWorkLoadView = new WeeklyWorkLoadView(executionPeriodInterval);
        request.setAttribute("weeklyWorkLoadView", weeklyWorkLoadView);

        if (degreeCurricularPlan != null) {
        	for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
        		for (final ExecutionCourse executionCourse : curricularCourse.getExecutionCoursesByExecutionPeriod(selectedExecutionPeriod)) {
        			executionCourses.add(executionCourse);
        			for (final Attends attend : executionCourse.getAttends()) {
        				weeklyWorkLoadView.add(attend);
        			}
        		}
        	}
        }

        return mapping.findForward("showWeeklyWorkLoad");
    }

    private Interval getExecutionPeriodInterval(final ExecutionPeriod executionPeriod) {
        final DateTime beginningOfSemester = new DateTime(executionPeriod.getBeginDate());
        final DateTime firstMonday = beginningOfSemester.withField(DateTimeFieldType.dayOfWeek(), 1);
        final DateTime endOfSemester = new DateTime(executionPeriod.getEndDate());
        final DateTime nextLastMonday = endOfSemester.withField(DateTimeFieldType.dayOfWeek(), 1).plusWeeks(1);
        return new Interval(firstMonday, nextLastMonday);
    }

    private Integer getExecutionPeriodID(final DynaActionForm dynaActionForm) {
        final String exeutionPeriodIDString = dynaActionForm.getString("executionPeriodID");
        return exeutionPeriodIDString == null || exeutionPeriodIDString.length() == 0 ? null : Integer.valueOf(exeutionPeriodIDString);
    }

    private ExecutionPeriod findExecutionPeriod(final List<ExecutionPeriod> executionPeriods, final Integer executionPeriodID) {
        for (final ExecutionPeriod executionPeriod : executionPeriods) {
            if (executionPeriodID == null && executionPeriod.getState().equals(PeriodState.CURRENT)) {
                return executionPeriod;
            }
            if (executionPeriodID != null && executionPeriod.getIdInternal().equals(executionPeriodID)) {
                return executionPeriod;
            }
        }
        return null;
    }

}
