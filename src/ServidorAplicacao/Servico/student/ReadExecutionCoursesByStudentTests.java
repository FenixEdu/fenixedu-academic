/*
 * Created on 28/Ago/2003
 *
 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IAttends;
import Dominio.IStudent;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentStudentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadExecutionCoursesByStudentTests implements IService {

    public Object run(String userName) throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
        final IPersistentStudent persistentStudent = persistentSuport.getIPersistentStudent();
        final IFrequentaPersistente persistentAttends = persistentSuport.getIFrequentaPersistente();
        final IPersistentStudentTestQuestion persistentStudentTestQuestion = persistentSuport.getIPersistentStudentTestQuestion();

        final IStudent student = persistentStudent.readByUsername(userName);
        final List attends = persistentAttends.readByStudentNumber(student.getNumber(), student.getDegreeType());

        final List infoExecutionCourses = new ArrayList();
        for (final Iterator iterator = attends.iterator(); iterator.hasNext(); ) {
            final IAttends attend = (IAttends) iterator.next();
            final IExecutionCourse executionCourse = attend.getDisciplinaExecucao();

            final int count = persistentStudentTestQuestion.countStudentTestByStudentAndExecutionCourse(executionCourse, student);

            if (count != 0) {
                final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);
                infoExecutionCourses.add(infoExecutionCourse);
            }
        }
        return infoExecutionCourses;
    }
}