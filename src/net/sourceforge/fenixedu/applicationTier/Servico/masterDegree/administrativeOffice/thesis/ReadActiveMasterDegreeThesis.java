/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.MasterDegreeProofVersion;
import net.sourceforge.fenixedu.domain.MasterDegreeThesis;
import net.sourceforge.fenixedu.domain.masterDegree.MasterDegreeThesisState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ReadActiveMasterDegreeThesis extends Service {

    public Collection<MasterDegreeThesis> run(MasterDegreeThesisState thesisState, Integer year,
	    Degree degree) throws ExcepcaoPersistencia {

	Collection<MasterDegreeThesis> result = new ArrayList<MasterDegreeThesis>();
	for (MasterDegreeThesis masterDegreeThesis : rootDomainObject.getMasterDegreeThesiss()) {

	    if (masterDegreeThesis.getStudentCurricularPlan().getDegree() != degree) {
		continue;
	    }

	    if (year != null && thesisState != MasterDegreeThesisState.NOT_DELIVERED) {
		final MasterDegreeProofVersion proofVersion = masterDegreeThesis
			.getActiveMasterDegreeProofVersion();

		if (proofVersion == null) {
		    continue;
		}

		if (thesisState == MasterDegreeThesisState.CONCLUDED
			&& (proofVersion.getProofDateYearMonthDay() == null || proofVersion
				.getProofDateYearMonthDay().getYear() != year)) {
		    continue;
		}

		if (thesisState == MasterDegreeThesisState.DELIVERED
			&& (proofVersion.getThesisDeliveryDateYearMonthDay() == null || proofVersion
				.getThesisDeliveryDateYearMonthDay().getYear() != year)) {
		    continue;
		}

	    }

	    if (masterDegreeThesis.getState() == thesisState) {
		result.add(masterDegreeThesis);
	    }
	}

	return result;
    }

}
