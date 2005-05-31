package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesisDataVersionWithGuidersAndResp;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.IMasterDegreeThesisDataVersion;
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
public class ReadNonActivesMasterDegreeThesisDataVersionsByStudentCurricularPlan implements IService {

    public List run(InfoStudentCurricularPlan infoStudentCurricularPlan) throws ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        List masterDegreeThesisDataVersions = sp
                .getIPersistentMasterDegreeThesisDataVersion()
                .readNotActivesVersionsByStudentCurricularPlan(infoStudentCurricularPlan.getIdInternal());

        List infoMasterDegreeThesisDataVersions = new ArrayList(masterDegreeThesisDataVersions.size());
        for (IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion : (List<IMasterDegreeThesisDataVersion>) masterDegreeThesisDataVersions) {
            infoMasterDegreeThesisDataVersions.add(InfoMasterDegreeThesisDataVersionWithGuidersAndResp
                    .newInfoFromDomain(masterDegreeThesisDataVersion));
        }

        return infoMasterDegreeThesisDataVersions;
    }
}