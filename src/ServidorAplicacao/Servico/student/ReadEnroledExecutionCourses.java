/*
 * Created on 26/Ago/2003
 *
 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IGroupProperties;
import Dominio.IStudent;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *  
 */
public class ReadEnroledExecutionCourses implements IServico {

    private static ReadEnroledExecutionCourses _servico = new ReadEnroledExecutionCourses();

    /**
     * The singleton access method of this class.
     */
    public static ReadEnroledExecutionCourses getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private ReadEnroledExecutionCourses() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "ReadEnroledExecutionCourses";
    }

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

    public List run(String username) throws FenixServiceException {
        List allInfoExecutionCourses = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
            IPersistentGroupProperties persistentGroupProperties = sp.getIPersistentGroupProperties();

            IStudent student = persistentStudent.readByUsername(username);
            List allAttend = persistentAttend.readByStudentNumber(student.getNumber());

            Iterator iter = allAttend.iterator();
            allInfoExecutionCourses = new ArrayList();

            while (iter.hasNext()) {
                IExecutionCourse executionCourse = ((IFrequenta) iter.next()).getDisciplinaExecucao();
                List allGroupProperties = persistentGroupProperties
                        .readAllGroupPropertiesByExecutionCourse(executionCourse);
                boolean result = checkPeriodEnrollment(allGroupProperties);
                if (result) {
                    allInfoExecutionCourses.add(Cloner.get(executionCourse));
                }

            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return allInfoExecutionCourses;

    }

}