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
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadStudentsWithDistributedTest implements IService {

    public List run(Integer executionCourseId, Integer distributedTestId) throws FenixServiceException {
        List<InfoStudent> infoStudentList = new ArrayList<InfoStudent>();
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IDistributedTest distributedTest = (IDistributedTest) persistentSuport.getIPersistentDistributedTest().readByOID(DistributedTest.class,
                    distributedTestId);
            if (distributedTest == null)
                throw new FenixServiceException();

            List<IStudent> studentList = persistentSuport.getIPersistentStudentTestQuestion().readStudentsByDistributedTest(
                    distributedTest.getIdInternal());

            for (IStudent student : studentList)
                infoStudentList.add(InfoStudentWithInfoPerson.newInfoFromDomain(student));

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return infoStudentList;
    }
}