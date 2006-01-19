/*
 * Created on 17/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentWithInfoPerson;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Susana Fernandes
 */
public class ReadStudentsWithoutDistributedTest extends Service {

    public List run(Integer executionCourseId, Integer distributedTestId) throws FenixServiceException, ExcepcaoPersistencia {
        List<InfoStudent> infoStudentList = new ArrayList<InfoStudent>();
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        List<Attends> attendList = persistentSuport.getIFrequentaPersistente().readByExecutionCourse(executionCourseId);
        List<Student> studentList = persistentSuport.getIPersistentStudentTestQuestion().readStudentsByDistributedTest(distributedTestId);
        for (Attends attend : attendList) {
            if (!studentList.contains(attend.getAluno()))
                infoStudentList.add(InfoStudentWithInfoPerson.newInfoFromDomain(attend.getAluno()));
        }
        return infoStudentList;
    }
}