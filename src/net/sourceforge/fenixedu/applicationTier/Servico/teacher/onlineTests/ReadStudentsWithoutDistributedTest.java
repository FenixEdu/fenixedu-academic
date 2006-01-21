/*
 * Created on 17/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentWithInfoPerson;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 */
public class ReadStudentsWithoutDistributedTest extends Service {

    public List run(Integer executionCourseId, Integer distributedTestId) throws FenixServiceException, ExcepcaoPersistencia {
        List<InfoStudent> infoStudentList = new ArrayList<InfoStudent>();
        List<Attends> attendList = persistentSupport.getIFrequentaPersistente().readByExecutionCourse(executionCourseId);
        List<Student> studentList = persistentSupport.getIPersistentStudentTestQuestion().readStudentsByDistributedTest(distributedTestId);
        for (Attends attend : attendList) {
            if (!studentList.contains(attend.getAluno()))
                infoStudentList.add(InfoStudentWithInfoPerson.newInfoFromDomain(attend.getAluno()));
        }
        return infoStudentList;
    }
}