/*
 * Created on 28/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestQuestion;

/**
 * @author Susana Fernandes
 */
public class ReadExecutionCoursesByStudentTests extends Service {

    public Object run(String userName) throws ExcepcaoPersistencia {
        final IPersistentStudentTestQuestion persistentStudentTestQuestion = persistentSupport
                .getIPersistentStudentTestQuestion();

        final Student student = Student.readByUsername(userName);
        final List<Attends> attends = student.getAssociatedAttends();

        final List<InfoExecutionCourse> infoExecutionCourses = new ArrayList<InfoExecutionCourse>();
        for (Attends attend : attends) {
            final ExecutionCourse executionCourse = attend.getDisciplinaExecucao();
            if (persistentStudentTestQuestion.countStudentTestByStudentAndExecutionCourse(
                    executionCourse.getIdInternal(), student.getIdInternal()) != 0) {
                infoExecutionCourses.add(InfoExecutionCourse.newInfoFromDomain(executionCourse));
            }
        }
        return infoExecutionCourses;
    }
}