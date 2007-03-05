package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoGrouping;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.PeriodState;

public class ReadEnroledExecutionCourses extends Service {

    private boolean checkPeriodEnrollment(final Grouping grouping) {
        final IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
                .getInstance();
        final IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
                .getGroupEnrolmentStrategyInstance(grouping);
        return strategy.checkEnrolmentDate(grouping, Calendar.getInstance());

    }

    private boolean checkPeriodEnrollment(List<Grouping> allGroupProperties) {
        for (final Grouping grouping : allGroupProperties) {
            if (checkPeriodEnrollment(grouping)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkStudentInAttendsSet(List allGroupProperties, Registration registration) {
        boolean result = false;

        Iterator iter = allGroupProperties.iterator();
        while (iter.hasNext()) {
            Grouping groupProperties = (Grouping) iter.next();
            if (groupProperties.getStudentAttend(registration) != null)
                return true;
        }

        return result;
    }

    public List run(String username) throws ExcepcaoPersistencia {
        List<InfoExecutionCourse> allInfoExecutionCourses = new ArrayList<InfoExecutionCourse>();

        Registration registration = Registration.readByUsername(username);
        List allAttend = registration.getAssociatedAttends();

        Iterator iter = allAttend.iterator();

        while (iter.hasNext()) {
            ExecutionCourse executionCourse = ((Attends) iter.next()).getExecutionCourse();
            if (executionCourse.getExecutionPeriod().getState().equals(PeriodState.CURRENT)) {
                List<Grouping> allGroupProperties = executionCourse.getGroupings();
                boolean result = checkPeriodEnrollment(allGroupProperties);
                if (result && checkStudentInAttendsSet(allGroupProperties, registration)) {
                    final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse
                            .newInfoFromDomain(executionCourse);
                    final List<InfoGrouping> infoGroupings = new ArrayList<InfoGrouping>();
                    for (final Grouping grouping : executionCourse.getGroupings()) {
                        if (checkPeriodEnrollment(grouping)) {
                            infoGroupings.add(InfoGrouping.newInfoFromDomain(grouping));
                        }
                    }
                    infoExecutionCourse.setFilteredInfoGroupings(infoGroupings);
                    allInfoExecutionCourses.add(infoExecutionCourse);
                }
            }
        }

        return allInfoExecutionCourses;

    }

}