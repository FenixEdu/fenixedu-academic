/*
 * Created on 26/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.student.gaugingTests.physics;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.gaugingTests.physics.InfoGaugingTestResult;
import DataBeans.util.Cloner;
import Dominio.IPerson;
import Dominio.IStudent;
import Dominio.gaugingTests.physics.IGaugingTestResult;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.OJB.gaugingTests.physics.IPersistentGaugingTestResult;
import Util.TipoCurso;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a>
 *  
 */

public class readGaugingTestsResults implements IService {

    /**
     * The constructor of this class.
     */
    public readGaugingTestsResults() {
    }

    /**
     * Executes the service.
     */

    public InfoGaugingTestResult run(IUserView userView) throws FenixServiceException {
        try {
            ISuportePersistente ps = SuportePersistenteOJB.getInstance();
            IPersistentGaugingTestResult persistentGaugingTestResult = ps
                    .getIPersistentGaugingTestResult();
            IPessoaPersistente persistentPerson = ps.getIPessoaPersistente();
            IPerson person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());

            IPersistentStudent persistentStudent = ps.getIPersistentStudent();

            IStudent student = persistentStudent.readByPersonAndDegreeType(person,
                    TipoCurso.LICENCIATURA_OBJ);
            if (student == null) {
                return null;
            }
            IGaugingTestResult gaugingTestsResult = persistentGaugingTestResult.readByStudent(student);
            if (gaugingTestsResult != null) {

                InfoGaugingTestResult infoGaugingTestResult = Cloner
                        .copyIGaugingTestResult2IngoGaugingTestResult(gaugingTestsResult);
                return infoGaugingTestResult;
            }

            return null;

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }
}