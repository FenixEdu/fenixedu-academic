/*
 * Created on 17/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentWithInfoPerson;
import net.sourceforge.fenixedu.domain.DistributedTest;
import net.sourceforge.fenixedu.domain.IDistributedTest;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadStudentsWithDistributedTest implements IService {

    public ReadStudentsWithDistributedTest() {
    }

    public List run(Integer executionCourseId, Integer distributedTestId) throws FenixServiceException {

        ISuportePersistente persistentSuport;
        List infoStudentList = new ArrayList();
        try {
            persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IDistributedTest distributedTest = (IDistributedTest) persistentSuport
                    .getIPersistentDistributedTest().readByOID(DistributedTest.class, distributedTestId);
            if (distributedTest == null)
                throw new FenixServiceException();

            List studentList = persistentSuport.getIPersistentStudentTestQuestion()
                    .readStudentsByDistributedTest(distributedTest);

            for (int i = 0; i < studentList.size(); i++)
                infoStudentList.add(InfoStudentWithInfoPerson.newInfoFromDomain((IStudent) studentList
                        .get(i)));

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return infoStudentList;
    }
}