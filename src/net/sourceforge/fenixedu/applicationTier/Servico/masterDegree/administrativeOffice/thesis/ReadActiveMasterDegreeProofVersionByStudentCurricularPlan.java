package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ScholarshipNotFinishedServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.strategys.IMasterDegreeCurricularPlanStrategy;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeProofVersion;
import net.sourceforge.fenixedu.domain.MasterDegreeProofVersion;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
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
public class ReadActiveMasterDegreeProofVersionByStudentCurricularPlan implements IService {

    public InfoMasterDegreeProofVersion run(Integer studentCurricularPlanID)
            throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) sp
                .getIStudentCurricularPlanPersistente().readByOID(StudentCurricularPlan.class,
                        studentCurricularPlanID);

        IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory = DegreeCurricularPlanStrategyFactory
                .getInstance();
        IMasterDegreeCurricularPlanStrategy masterDegreeCurricularPlanStrategy = (IMasterDegreeCurricularPlanStrategy) degreeCurricularPlanStrategyFactory
                .getDegreeCurricularPlanStrategy(studentCurricularPlan.getDegreeCurricularPlan());

        if (!masterDegreeCurricularPlanStrategy.checkEndOfScholarship(studentCurricularPlan))
            throw new ScholarshipNotFinishedServiceException(
                    "error.exception.masterDegree.scholarshipNotFinished");

        MasterDegreeProofVersion masterDegreeProofVersion = sp.getIPersistentMasterDegreeProofVersion()
                .readActiveByStudentCurricularPlan(studentCurricularPlan);

        if (masterDegreeProofVersion == null)
            throw new NonExistingServiceException(
                    "error.exception.masterDegree.nonExistingMasterDegreeProofVersion");

        return InfoMasterDegreeProofVersion.newInfoFromDomain(masterDegreeProofVersion);

    }
}