package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class ReadGratuitySituationById extends Service {

    public InfoGratuitySituation run(Integer gratuitySituationID) throws FenixServiceException {
	InfoGratuitySituation infoGratuitySituation = null;

	GratuitySituation gratuitySituation = rootDomainObject.readGratuitySituationByOID(gratuitySituationID);
	if (gratuitySituation == null) {
	    throw new NonExistingServiceException("error.exception.masterDegree.gratuity.notExistingGratuitySituation");
	}

	infoGratuitySituation = InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree.newInfoFromDomain(gratuitySituation);

	return infoGratuitySituation;

    }
}