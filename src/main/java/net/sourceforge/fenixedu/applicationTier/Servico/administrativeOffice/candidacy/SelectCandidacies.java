/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.candidacy;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.SelectDFACandidacyBean;
import net.sourceforge.fenixedu.domain.candidacy.SubstituteCandidacySituation;
import net.sourceforge.fenixedu.domain.util.workflow.StateMachine;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class SelectCandidacies {

    @Atomic
    public static void run(List<SelectDFACandidacyBean> admittedCandidacies, List<SelectDFACandidacyBean> substituteCandidacies,
            List<SelectDFACandidacyBean> notAdmittedCandidacies) {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);

        createNewCandidacySituations(notAdmittedCandidacies);
        createNewCandidacySituations(admittedCandidacies);
        createNewCandidacySituations(substituteCandidacies);

        fillSubstituteCandidaciesOrder(substituteCandidacies);

    }

    private static void fillSubstituteCandidaciesOrder(List<SelectDFACandidacyBean> substituteCandidacies) {
        if (substituteCandidacies != null && !substituteCandidacies.isEmpty()) {
            for (SelectDFACandidacyBean candidacyBean : substituteCandidacies) {
                SubstituteCandidacySituation candidacySituation =
                        (SubstituteCandidacySituation) candidacyBean.getCandidacy().getActiveCandidacySituation();
                candidacySituation.setCandidacyOrder(candidacyBean.getOrder());
            }
        }
    }

    private static void createNewCandidacySituations(List<SelectDFACandidacyBean> candidacies) {
        if (candidacies != null && !candidacies.isEmpty()) {
            for (SelectDFACandidacyBean candidacyBean : candidacies) {
                StateMachine.execute(candidacyBean.getCandidacy().getActiveCandidacySituation(), candidacyBean);
                candidacyBean.getCandidacy().getActiveCandidacySituation().setRemarks(candidacyBean.getRemarks());
            }
        }
    }

}