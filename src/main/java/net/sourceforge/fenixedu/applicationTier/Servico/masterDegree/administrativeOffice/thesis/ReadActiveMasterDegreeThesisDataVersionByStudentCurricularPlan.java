package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;


import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlan {

    @Atomic
    public static InfoMasterDegreeThesisDataVersion run(InfoStudentCurricularPlan infoStudentCurricularPlan)
            throws FenixServiceException {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);
        InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion = null;

        StudentCurricularPlan studentCurricularPlan =
                FenixFramework.getDomainObject(infoStudentCurricularPlan.getExternalId());

        MasterDegreeThesisDataVersion masterDegreeThesisDataVersion =
                studentCurricularPlan.readActiveMasterDegreeThesisDataVersion();

        if (masterDegreeThesisDataVersion == null) {
            throw new NonExistingServiceException("error.exception.masterDegree.nonExistingMasterDegreeThesis");
        }

        infoMasterDegreeThesisDataVersion =
                InfoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis.newInfoFromDomain(masterDegreeThesisDataVersion);

        return infoMasterDegreeThesisDataVersion;
    }
}