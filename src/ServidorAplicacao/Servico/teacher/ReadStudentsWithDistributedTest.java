/*
 * Created on 17/Set/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoStudentWithInfoPerson;
import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import Dominio.IStudent;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
            persistentSuport = SuportePersistenteOJB.getInstance();

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