/*
 * Created on 26/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.gaugingTests.physics;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.gaugingTests.physics.InfoGaugingTestResult;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.gaugingTests.physics.IGaugingTestResult;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.OJB.gaugingTests.physics.IPersistentGaugingTestResult;
import net.sourceforge.fenixedu.util.TipoCurso;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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
            ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();
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