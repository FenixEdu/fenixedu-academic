/*
 * Created on 10/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Tânia Pousão
 * 
 */
public class DeleteGratuitySituationById extends FenixService {

	@Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
	@Service
	public static Boolean run(Integer gratuitySituationID) throws FenixServiceException {
		GratuitySituation gratuitySituation = rootDomainObject.readGratuitySituationByOID(gratuitySituationID);
		if (gratuitySituation == null) {
			return Boolean.TRUE;
		}

		gratuitySituation.setExemptionPercentage(null);
		gratuitySituation.setExemptionValue(null);
		gratuitySituation.setExemptionType(null);
		gratuitySituation.setExemptionDescription(null);

		return Boolean.TRUE;
	}

}