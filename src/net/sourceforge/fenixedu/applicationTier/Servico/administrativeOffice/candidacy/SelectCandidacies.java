/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.candidacy;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.SelectDFACandidacyBean;
import net.sourceforge.fenixedu.domain.candidacy.SubstituteCandidacySituation;
import net.sourceforge.fenixedu.domain.util.StateMachine;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class SelectCandidacies extends Service {

    public void run(List<SelectDFACandidacyBean> admittedCandidacies,
	    List<SelectDFACandidacyBean> substituteCandidacies,
	    List<SelectDFACandidacyBean> notAdmittedCandidacies) {

	createNewCandidacySituations(notAdmittedCandidacies);
	createNewCandidacySituations(admittedCandidacies);
	createNewCandidacySituations(substituteCandidacies);

	fillSubstituteCandidaciesOrder(substituteCandidacies);

    }

    private void fillSubstituteCandidaciesOrder(List<SelectDFACandidacyBean> substituteCandidacies) {
	if (substituteCandidacies != null && !substituteCandidacies.isEmpty()) {
	    for (SelectDFACandidacyBean candidacyBean : substituteCandidacies) {
		SubstituteCandidacySituation candidacySituation = (SubstituteCandidacySituation) candidacyBean
			.getCandidacy().getActiveCandidacySituation();
		candidacySituation.setCandidacyOrder(candidacyBean.getOrder());
	    }
	}
    }

    private void createNewCandidacySituations(List<SelectDFACandidacyBean> candidacies) {
	if (candidacies != null && !candidacies.isEmpty()) {
	    for (SelectDFACandidacyBean candidacyBean : candidacies) {
		StateMachine.execute(candidacyBean.getCandidacy().getActiveCandidacySituation(),
			candidacyBean.getSelectionSituation().name());
		candidacyBean.getCandidacy().getActiveCandidacySituation().setRemarks(
			candidacyBean.getRemarks());
	    }
	}
    }

}
