package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class ReadNonActivesMasterDegreeProofVersionsByStudentCurricularPlan implements IService {

    public List run(InfoStudentCurricularPlan infoStudentCurricularPlan) throws FenixServiceException {
        List infoMasterDegreeProofVersions = new ArrayList();

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IStudentCurricularPlan studentCurricularPlan = Cloner
                    .copyInfoStudentCurricularPlan2IStudentCurricularPlan(infoStudentCurricularPlan);

            List masterDegreeProofVersions = sp.getIPersistentMasterDegreeProofVersion()
                    .readNotActiveByStudentCurricularPlan(studentCurricularPlan);

            if (masterDegreeProofVersions.isEmpty() == false) {
                infoMasterDegreeProofVersions = Cloner
                        .copyListIMasterDegreeProofVersion2ListInfoMasterDegreeProofVersion(masterDegreeProofVersions);
            }

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return infoMasterDegreeProofVersions;
    }
}