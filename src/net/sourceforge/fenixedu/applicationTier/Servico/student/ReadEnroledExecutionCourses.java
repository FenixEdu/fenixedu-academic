/*
 * Created on 26/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.PeriodState;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author asnr and scpo
 * 
 */
public class ReadEnroledExecutionCourses implements IService {

    private boolean checkPeriodEnrollment(List allGroupProperties) {
        boolean result = false;

        Iterator iter = allGroupProperties.iterator();
        while (iter.hasNext()) {
            IGroupProperties groupProperties = (IGroupProperties) iter.next();

            IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
                    .getInstance();
            IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
                    .getGroupEnrolmentStrategyInstance(groupProperties);
            result = result || strategy.checkEnrolmentDate(groupProperties, Calendar.getInstance());
        }

        return result;
    }

    private boolean checkStudentInAttendsSet(List allGroupProperties, IStudent student) {
        boolean result = false;

        Iterator iter = allGroupProperties.iterator();
        while (iter.hasNext()) {
            IGroupProperties groupProperties = (IGroupProperties) iter.next();
            if (groupProperties.getAttendsSet().getStudentAttend(student) != null)
                return true;
        }

        return result;
    }

    public List run(String username) throws ExcepcaoPersistencia {
        List allInfoExecutionCourses = new ArrayList();

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
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
                List allGroupProperties = executionCourse.getGroupProperties();
                boolean result = checkPeriodEnrollment(allGroupProperties);
                if (result && checkStudentInAttendsSet(allGroupProperties, student)) {
                    final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse
                            .newInfoFromDomain(executionCourse);
                    allInfoExecutionCourses.add(infoExecutionCourse);
                }
            }
        }

        return allInfoExecutionCourses;

    }

}