package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.IMasterDegreeThesisDataVersion;
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
public class ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlan implements IService {

    public InfoMasterDegreeThesisDataVersion run(InfoStudentCurricularPlan infoStudentCurricularPlan)
            throws FenixServiceException {
        InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            //CLONER
            //            IStudentCurricularPlan studentCurricularPlan = Cloner
            //                    .copyInfoStudentCurricularPlan2IStudentCurricularPlan(infoStudentCurricularPlan);
            IStudentCurricularPlan studentCurricularPlan = InfoStudentCurricularPlan
                    .newDomainFromInfo(infoStudentCurricularPlan);
            IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion = sp
                    .getIPersistentMasterDegreeThesisDataVersion().readActiveByStudentCurricularPlan(
                            studentCurricularPlan);

            if (masterDegreeThesisDataVersion == null)
                throw new NonExistingServiceException(
                        "error.exception.masterDegree.nonExistingMasterDegreeThesis");

            //CLONER
            //            infoMasterDegreeThesisDataVersion = Cloner
            //                    .copyIMasterDegreeThesisDataVersion2InfoMasterDegreeThesisDataVersion(masterDegreeThesisDataVersion);
            infoMasterDegreeThesisDataVersion = InfoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis
                    .newInfoFromDomain(masterDegreeThesisDataVersion);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return infoMasterDegreeThesisDataVersion;
    }
}