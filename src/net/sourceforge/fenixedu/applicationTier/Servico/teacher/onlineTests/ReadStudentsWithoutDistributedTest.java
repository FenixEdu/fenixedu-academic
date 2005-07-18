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
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadStudentsWithoutDistributedTest implements IService {

    public List run(Integer executionCourseId, Integer distributedTestId) throws FenixServiceException, ExcepcaoPersistencia {
        List<InfoStudent> infoStudentList = new ArrayList<InfoStudent>();
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        List<IAttends> attendList = persistentSuport.getIFrequentaPersistente().readByExecutionCourse(executionCourseId);
        List<IStudent> studentList = persistentSuport.getIPersistentStudentTestQuestion().readStudentsByDistributedTest(distributedTestId);
        for (IAttends attend : attendList) {
            if (!studentList.contains(attend.getAluno()))
                infoStudentList.add(InfoStudentWithInfoPerson.newInfoFromDomain(attend.getAluno()));
        }
        return infoStudentList;
    }
}