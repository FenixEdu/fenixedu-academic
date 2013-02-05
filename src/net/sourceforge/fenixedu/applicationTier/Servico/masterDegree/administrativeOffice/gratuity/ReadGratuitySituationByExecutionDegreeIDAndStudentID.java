package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class ReadGratuitySituationByExecutionDegreeIDAndStudentID extends FenixService {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static InfoGratuitySituation run(Integer executionDegreeID, Integer studentID) throws FenixServiceException {

        InfoGratuitySituation infoGratuitySituation = null;

        ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeID);
        Registration registration = rootDomainObject.readRegistrationByOID(studentID);

        if ((executionDegree == null) || (registration == null)) {
            return null;
        }

        GratuitySituation gratuitySituation = registration.readGratuitySituationByExecutionDegree(executionDegree);

        infoGratuitySituation = InfoGratuitySituation.newInfoFromDomain(gratuitySituation);

        return infoGratuitySituation;
    }

}