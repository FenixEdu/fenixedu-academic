package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class ReadGratuitySituationByExecutionDegreeIDAndStudentID extends Service {

    public InfoGratuitySituation run(Integer executionDegreeID, Integer studentID)
            throws FenixServiceException, ExcepcaoPersistencia {

        InfoGratuitySituation infoGratuitySituation = null;

        ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeID);
        Registration student = rootDomainObject.readRegistrationByOID(studentID);

        if ((executionDegree == null) || (student == null)) {
            return null;
        }

        GratuitySituation gratuitySituation = student
                .readGratuitySituationByExecutionDegree(executionDegree);

        infoGratuitySituation = InfoGratuitySituation.newInfoFromDomain(gratuitySituation);

        return infoGratuitySituation;
    }

}