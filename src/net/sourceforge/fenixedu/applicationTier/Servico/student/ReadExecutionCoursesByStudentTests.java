/*
 * Created on 28/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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