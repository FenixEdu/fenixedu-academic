/*
 * Created on 17/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentWithInfoPerson;
import net.sourceforge.fenixedu.domain.DistributedTest;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IDistributedTest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadStudentsWithoutDistributedTest implements IService {

    public List run(Integer executionCourseId, Integer distributedTestId) throws FenixServiceException,
            ExcepcaoPersistencia {

        ISuportePersistente persistentSuport;
        List infoStudentList = new ArrayList();

        persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IDistributedTest distributedTest = (IDistributedTest) persistentSuport
                .getIPersistentDistributedTest().readByOID(DistributedTest.class, distributedTestId);
        if (distributedTest == null)
            throw new FenixServiceException();
        //Todos os alunos
        List attendList = persistentSuport.getIFrequentaPersistente().readByExecutionCourse(
                executionCourseId);
        //alunos que tem test
        List studentList = persistentSuport.getIPersistentStudentTestQuestion()
                .readStudentsByDistributedTest(distributedTest);
        Iterator it = attendList.iterator();
        while (it.hasNext()) {
            IAttends attend = (IAttends) it.next();

            if (!studentList.contains(attend.getAluno()))
                infoStudentList.add(InfoStudentWithInfoPerson.newInfoFromDomain(attend.getAluno()));
        }
        return infoStudentList;
    }
}