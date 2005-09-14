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
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.PeriodState;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadEnroledExecutionCourses implements IService {

    private boolean checkPeriodEnrollment(final IGrouping grouping) {
        final IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
        final IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(grouping);
        return strategy.checkEnrolmentDate(grouping, Calendar.getInstance());
        
    }

    private boolean checkPeriodEnrollment(List<IGrouping> allGroupProperties) {
        for (final IGrouping grouping : allGroupProperties) {
            if (checkPeriodEnrollment(grouping)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkStudentInAttendsSet(List allGroupProperties, IStudent student) {
        boolean result = false;

        Iterator iter = allGroupProperties.iterator();
        while (iter.hasNext()) {
            IGrouping groupProperties = (IGrouping) iter.next();
            if (groupProperties.getStudentAttend(student) != null)
                return true;
        }

        return result;
    }

    public List run(String username) throws ExcepcaoPersistencia {
        List allInfoExecutionCourses = new ArrayList();

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
        IPersistentStudent persistentStudent = sp.getIPersistentStudent();

        IStudent student = persistentStudent.readByUsername(username);
        List allAttend = persistentAttend.readByStudentNumber(student.getNumber(), student
                .getDegreeType());

        Iterator iter = allAttend.iterator();
        allInfoExecutionCourses = new ArrayList();

        while (iter.hasNext()) {
            IExecutionCourse executionCourse = ((IAttends) iter.next()).getDisciplinaExecucao();
            if (executionCourse.getExecutionPeriod().getState().equals(PeriodState.CURRENT)) {
                List allGroupProperties = executionCourse.getGroupings();
                boolean result = checkPeriodEnrollment(allGroupProperties);
                if (result && checkStudentInAttendsSet(allGroupProperties, student)) {
                    final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);
                    final List<InfoGrouping> infoGroupings = new ArrayList<InfoGrouping>();
                    for (final IGrouping grouping : executionCourse.getGroupings()) {
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