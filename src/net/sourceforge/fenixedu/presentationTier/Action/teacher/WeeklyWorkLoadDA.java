package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseWithExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.student.WeeklyWorkLoad;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Interval;
import org.joda.time.Period;

public class WeeklyWorkLoadDA extends FenixDispatchAction {

    private static final Comparator<Attends> ATTENDS_COMPARATOR = new Comparator<Attends>() {
		public int compare(final Attends attends1, final Attends attends2) {
            final Student student1 = attends1.getAluno();
            final Student student2 = attends2.getAluno();
            return student1.getNumber().compareTo(student2.getNumber());
		}
    };

	public class WeeklyWorkLoadView {
        final Interval executionPeriodInterval;
        final int numberOfWeeks;

        final Interval[] intervals;
        final int[] numberResponses;
        final int[] contactSum;
        final int[] autonomousStudySum;
        final int[] otherSum;
        final int[] totalSum;

        public WeeklyWorkLoadView(final Interval executionPeriodInterval) {
            this.executionPeriodInterval = executionPeriodInterval;
            final Period period = executionPeriodInterval.toPeriod();
            int extraWeek = period.getDays() > 0 ? 1 : 0;
            numberOfWeeks = (period.getYears() * 12 + period.getMonths()) * 4 + period.getWeeks() + extraWeek;
            intervals = new Interval[numberOfWeeks];
            numberResponses = new int[numberOfWeeks];
            contactSum = new int[numberOfWeeks];
            autonomousStudySum = new int[numberOfWeeks];
            otherSum = new int[numberOfWeeks];
            totalSum = new int[numberOfWeeks];
            for (int i = 0; i < numberOfWeeks; i++) {
                final DateTime start = executionPeriodInterval.getStart().plusWeeks(i);
                final DateTime end = start.plusWeeks(1);
                intervals[i] = new Interval(start, end);
            }
        }

        public void add(final Attends attends) {
            for (final WeeklyWorkLoad weeklyWorkLoad : attends.getWeeklyWorkLoads()) {
            	final int weekIndex = weeklyWorkLoad.getWeekOffset();
            	numberResponses[weekIndex]++;

            	final Integer contact = weeklyWorkLoad.getContact();
            	contactSum[weekIndex] += contact != null ? contact.intValue() : 0;

            	final Integer autounomousStudy = weeklyWorkLoad.getAutonomousStudy();
            	autonomousStudySum[weekIndex] += autounomousStudy != null ? autounomousStudy.intValue() : 0;

            	final Integer other = weeklyWorkLoad.getOther();
            	otherSum[weekIndex] += other != null ? other.intValue() : 0;

            	totalSum[weekIndex] = contactSum[weekIndex] + autonomousStudySum[weekIndex] + otherSum[weekIndex];
            }
        }

        public Interval[] getIntervals() {
            return intervals;
        }

        public Interval getExecutionPeriodInterval() {
            return executionPeriodInterval;
        }

		public int[] getAutonomousStudySum() {
			return autonomousStudySum;
		}

		public int[] getContactSum() {
			return contactSum;
		}

		public int[] getNumberResponses() {
			return numberResponses;
		}

		public int[] getOtherSum() {
			return otherSum;
		}

		private int add(final int[] values) {
			int total = 0;
			for (int i = 0; i < values.length; i++) {
				total += values[i];
			}
			return total;
		}

		public int getAutonomousStudyTotal() {
			return add(autonomousStudySum);
		}

		public int getContactTotal() {
			return add(contactSum);
		}

		public int getNumberResponsesTotal() {
			return add(numberResponses);
		}

		public int getOtherSumTotal() {
			return add(otherSum);
		}

		public int[] getTotalSum() {
			return totalSum;
		}

		public int getTotalSumTotal() {
			return add(totalSum);
		}		
	}

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixFilterException, FenixServiceException {

        final String executionCourseIDString = request.getParameter("executionCourseID");
        final Integer executionCourseID = (executionCourseIDString == null || executionCourseIDString.length() == 0) ?
                null : Integer.valueOf(executionCourseIDString);

        final Object[] args = { ExecutionCourse.class, executionCourseID };
        final ExecutionCourse executionCourse = (ExecutionCourse) executeService(request, "ReadDomainObject", args);
        request.setAttribute("executionCourse", executionCourse);

        final InfoSiteCommon infoSiteCommon = new InfoSiteCommon();
        infoSiteCommon.setExecutionCourse(InfoExecutionCourseWithExecutionPeriod.newInfoFromDomain(executionCourse));
        final ExecutionCourseSiteView executionCourseSiteView = new ExecutionCourseSiteView(infoSiteCommon, null);
        request.setAttribute("siteView", executionCourseSiteView);

        final Set<Attends> attends = new TreeSet<Attends>(ATTENDS_COMPARATOR);
        attends.addAll(executionCourse.getAttends());
        request.setAttribute("attends", attends);

        final Interval executionPeriodInterval = getExecutionPeriodInterval(executionCourse);
        final WeeklyWorkLoadView weeklyWorkLoadView = new WeeklyWorkLoadView(executionPeriodInterval);
        for (final Attends attend : attends) {
            weeklyWorkLoadView.add(attend);
        }
        request.setAttribute("weeklyWorkLoadView", weeklyWorkLoadView);

        return mapping.findForward("showWeeklyWorkLoad");
    }

    private Interval getExecutionPeriodInterval(final ExecutionCourse executionCourse) {
        final ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
        final DateTime beginningOfSemester = new DateTime(executionPeriod.getBeginDate());
        final DateTime firstMonday = beginningOfSemester.withField(DateTimeFieldType.dayOfWeek(), 1);
        final DateTime endOfSemester = new DateTime(executionPeriod.getEndDate());
        final DateTime nextLastMonday = endOfSemester.withField(DateTimeFieldType.dayOfWeek(), 1).plusWeeks(1);
        return new Interval(firstMonday, nextLastMonday);
    }

}
