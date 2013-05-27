package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class ReadMasterDegreeThesisDataVersionByID {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static Object run(Integer masterDegreeThesisDataVersionID) throws FenixServiceException {
        MasterDegreeThesisDataVersion masterDegreeThesisDataVersion =
                RootDomainObject.getInstance().readMasterDegreeThesisDataVersionByOID(masterDegreeThesisDataVersionID);
        if (masterDegreeThesisDataVersion == null) {
            throw new NonExistingServiceException("error.exception.masterDegree.nonExistingMasterDegreeThesisDataVersion");
        }

        return InfoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis.newInfoFromDomain(masterDegreeThesisDataVersion);
    }

}