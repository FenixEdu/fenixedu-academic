package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoGrouping;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.PeriodState;
import net.sourceforge.fenixedu.applicationTier.Service;

public class ReadEnroledExecutionCourses extends Service {

    private boolean checkPeriodEnrollment(final Grouping grouping) {
        final IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
        final IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(grouping);
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

    private boolean checkStudentInAttendsSet(List allGroupProperties, Student student) {
        boolean result = false;

        Iterator iter = allGroupProperties.iterator();
        while (iter.hasNext()) {
            Grouping groupProperties = (Grouping) iter.next();
            if (groupProperties.getStudentAttend(student) != null)
                return true;
        }

        return result;
    }

    public List run(String username) throws ExcepcaoPersistencia {
        List allInfoExecutionCourses = new ArrayList();

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IFrequentaPersistente persistentAttend = persistentSupport.getIFrequentaPersistente();
        IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();

        Student student = persistentStudent.readByUsername(username);
        List allAttend = persistentAttend.readByStudentNumber(student.getNumber(), student
                .getDegreeType());

        Iterator iter = allAttend.iterator();
        allInfoExecutionCourses = new ArrayList();

        while (iter.hasNext()) {
            ExecutionCourse executionCourse = ((Attends) iter.next()).getDisciplinaExecucao();
            if (executionCourse.getExecutionPeriod().getState().equals(PeriodState.CURRENT)) {
                List allGroupProperties = executionCourse.getGroupings();
                boolean result = checkPeriodEnrollment(allGroupProperties);
                if (result && checkStudentInAttendsSet(allGroupProperties, student)) {
                    final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);
                    final List<InfoGrouping> infoGroupings = new ArrayList<InfoGrouping>();
                    for (final Grouping grouping : executionCourse.getGroupings()) {
                        if (checkPeriodEnrollment(grouping)) {
                            infoGroupings.add(InfoGrouping.newInfoFromDomain(grouping));
                        }
                    }
                    infoExecutionCourse.setInfoGroupings(infoGroupings);
                    allInfoExecutionCourses.add(infoExecutionCourse);
                }
            }
        }

        return allInfoExecutionCourses;

    }

}