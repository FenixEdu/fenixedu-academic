/*
 * Created on 26/Ago/2003
 *
 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IAttends;
import Dominio.IGroupProperties;
import Dominio.IStudent;
import ServidorAplicacao.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.PeriodState;

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