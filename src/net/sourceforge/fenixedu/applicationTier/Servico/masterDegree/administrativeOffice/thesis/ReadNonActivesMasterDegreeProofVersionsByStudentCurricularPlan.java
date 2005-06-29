package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeProofVersion;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.IMasterDegreeProofVersion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class ReadNonActivesMasterDegreeProofVersionsByStudentCurricularPlan implements IService {

    public List run(InfoStudentCurricularPlan infoStudentCurricularPlan) throws FenixServiceException,
            ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        List masterDegreeProofVersions = sp.getIPersistentMasterDegreeProofVersion()
                .readNotActiveByStudentCurricularPlan(infoStudentCurricularPlan.getIdInternal());

        CollectionUtils.transform(masterDegreeProofVersions, new Transformer() {

            public Object transform(Object arg0) {
                IMasterDegreeProofVersion masterDegreeProofVersion = (IMasterDegreeProofVersion) arg0;
                return InfoMasterDegreeProofVersion.newInfoFromDomain(masterDegreeProofVersion);
            }

        });

        return masterDegreeProofVersions;
    }
}