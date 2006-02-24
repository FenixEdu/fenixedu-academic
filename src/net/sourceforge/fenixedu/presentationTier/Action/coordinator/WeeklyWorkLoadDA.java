package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.text.Collator;
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
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionCourse.WeeklyWorkLoadView;
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

//	public class WeeklyWorkLoadView {
//        final Interval executionPeriodInterval;
//        final int numberOfWeeks;
//
//        final Interval[] intervals;
//        final int[] numberResponses;
//        final int[] contactSum;
//        final int[] autonomousStudySum;
//        final int[] otherSum;
//        final int[] totalSum;
//
//        public WeeklyWorkLoadView(final Interval executionPeriodInterval) {
//            this.executionPeriodInterval = executionPeriodInterval;
//            final Period period = executionPeriodInterval.toPeriod();
//            int extraWeek = period.getDays() > 0 ? 1 : 0;
//            numberOfWeeks = (period.getYears() * 12 + period.getMonths()) * 4 + period.getWeeks() + extraWeek;
//            intervals = new Interval[numberOfWeeks];
//            numberResponses = new int[numberOfWeeks];
//            contactSum = new int[numberOfWeeks];
//            autonomousStudySum = new int[numberOfWeeks];
//            otherSum = new int[numberOfWeeks];
//            totalSum = new int[numberOfWeeks];
//            for (int i = 0; i < numberOfWeeks; i++) {
//                final DateTime start = executionPeriodInterval.getStart().plusWeeks(i);
//                final DateTime end = start.plusWeeks(1);
//                intervals[i] = new Interval(start, end);
//            }
//        }
//
//        public void add(final Attends attends) {
//            for (final WeeklyWorkLoad weeklyWorkLoad : attends.getWeeklyWorkLoads()) {
//            	final int weekIndex = weeklyWorkLoad.getWeekOffset();
//            	numberResponses[weekIndex]++;
//
//            	final Integer contact = weeklyWorkLoad.getContact();
//            	contactSum[weekIndex] += contact != null ? contact.intValue() : 0;
//
//            	final Integer autounomousStudy = weeklyWorkLoad.getAutonomousStudy();
//            	autonomousStudySum[weekIndex] += autounomousStudy != null ? autounomousStudy.intValue() : 0;
//
//            	final Integer other = weeklyWorkLoad.getOther();
//            	otherSum[weekIndex] += other != null ? other.intValue() : 0;
//
//            	totalSum[weekIndex] = contactSum[weekIndex] + autonomousStudySum[weekIndex] + otherSum[weekIndex];
//            }
//        }
//
//        public Interval[] getIntervals() {
//            return intervals;
//        }
//
//        public Interval getExecutionPeriodInterval() {
//            return executionPeriodInterval;
//        }
//
//		public int[] getAutonomousStudySum() {
//			return autonomousStudySum;
//		}
//
//		public int[] getContactSum() {
//			return contactSum;
//		}
//
//		public int[] getNumberResponses() {
//			return numberResponses;
//		}
//
//		public int[] getOtherSum() {
//			return otherSum;
//		}
//
//		private int add(final int[] values) {
//			int total = 0;
//			for (int i = 0; i < values.length; i++) {
//				total += values[i];
//			}
//			return total;
//		}
//
//		public int getAutonomousStudyTotal() {
//			return add(autonomousStudySum);
//		}
//
//		public int getContactTotal() {
//			return add(contactSum);
//		}
//
//		public int getNumberResponsesTotal() {
//			return add(numberResponses);
//		}
//
//		public int getOtherSumTotal() {
//			return add(otherSum);
//		}
//
//		public int[] getTotalSum() {
//			return totalSum;
//		}
//
//		public int getTotalSumTotal() {
//			return add(totalSum);
//		}		
//	}
//
//    private Interval getExecutionPeriodInterval() {
//    	final ExecutionPeriod executionPeriod = getExecutionPeriod();
//        final DateTime beginningOfSemester = new DateTime(executionPeriod.getBeginDate());
//        final DateTime firstMonday = beginningOfSemester.withField(DateTimeFieldType.dayOfWeek(), 1);
//        final DateTime endOfSemester = new DateTime(executionPeriod.getEndDate());
//        final DateTime nextLastMonday = endOfSemester.withField(DateTimeFieldType.dayOfWeek(), 1).plusWeeks(1);
//        return new Interval(firstMonday, nextLastMonday);
//    }
//
//	public WeeklyWorkLoadView getWeeklyWorkLoadView() {
//		final Interval interval = getExecutionPeriodInterval();
//		final WeeklyWorkLoadView weeklyWorkLoadView = new WeeklyWorkLoadView(interval);
//        for (final Attends attend : getAttends()) {
//            weeklyWorkLoadView.add(attend);
//        }
//		return weeklyWorkLoadView;
//	}

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

        final Set<CurricularYear> curricularYears = new TreeSet<CurricularYear>();
        request.setAttribute("curricularYears", curricularYears);

        final Set<ExecutionCourse> executionCourses = new TreeSet<ExecutionCourse>(ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR);
        request.setAttribute("executionCourses", executionCourses);

        final Integer curricularYearID = getCurricularYearID(dynaActionForm);

        final Map<CurricularSemester, Set<ExecutionCourse>> executionCoursesMap = new TreeMap<CurricularSemester, Set<ExecutionCourse>>();
        if (degreeCurricularPlan != null) {
        	for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
        		for (final CurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
        			final CurricularSemester curricularSemester = curricularCourseScope.getCurricularSemester();
        			final CurricularYear curricularYear = curricularSemester.getCurricularYear();
        			curricularYears.add(curricularYear);

        			if (curricularYearID == null || curricularYear.getIdInternal().equals(curricularYearID)) {
        				final Set<ExecutionCourse> executionCoursesSet;
        				if (executionCoursesMap.containsKey(curricularSemester)) {
        					executionCoursesSet = executionCoursesMap.get(curricularSemester);
        				} else {
        					executionCoursesSet = new TreeSet<ExecutionCourse>(new BeanComparator("nome", Collator.getInstance()));
        					executionCoursesMap.put(curricularSemester, executionCoursesSet);
        				}

        				for (final ExecutionCourse executionCourse : curricularCourse.getExecutionCoursesByExecutionPeriod(selectedExecutionPeriod)) {
        					executionCoursesSet.add(executionCourse);
        					executionCourses.add(executionCourse);
        				}
        			}
        		}
        	}
        }
        request.setAttribute("executionCoursesMap", executionCoursesMap);

        return mapping.findForward("showWeeklyWorkLoad");
    }

    private Integer getCurricularYearID(final DynaActionForm dynaActionForm) {
        final String curricularYearIDString = dynaActionForm.getString("curricularYearID");
        return curricularYearIDString == null || curricularYearIDString.length() == 0 ? null : Integer.valueOf(curricularYearIDString);
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
