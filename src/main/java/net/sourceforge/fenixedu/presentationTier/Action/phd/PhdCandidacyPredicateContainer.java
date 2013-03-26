package net.sourceforge.fenixedu.presentationTier.Action.phd;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import pt.utl.ist.fenix.tools.predicates.InlinePredicate;
import pt.utl.ist.fenix.tools.predicates.OrPredicate;
import pt.utl.ist.fenix.tools.predicates.Predicate;
import pt.utl.ist.fenix.tools.predicates.PredicateContainer;

public enum PhdCandidacyPredicateContainer implements PredicateContainer<PhdIndividualProgramProcess> {

    DELIVERED {
        @Override
        public Predicate<PhdIndividualProgramProcess> getPredicate() {

            final PhdCandidacyPredicate missingInformationFilter =
                    new PhdCandidacyPredicate(PhdProgramCandidacyProcessState.STAND_BY_WITH_MISSING_INFORMATION);
            final PhdCandidacyPredicate completeInformationFilter =
                    new PhdCandidacyPredicate(PhdProgramCandidacyProcessState.STAND_BY_WITH_COMPLETE_INFORMATION);

            return new OrPredicate<PhdIndividualProgramProcess>() {
                {
                    add(missingInformationFilter);
                    add(completeInformationFilter);
                }
            };
        }
    },

    PENDING {
        @Override
        public Predicate<PhdIndividualProgramProcess> getPredicate() {
            return new PhdCandidacyPredicate(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION);
        }
    },

    APPROVED {
        @Override
        public Predicate<PhdIndividualProgramProcess> getPredicate() {
            final PhdCandidacyPredicate waitingScientificFilter =
                    new PhdCandidacyPredicate(PhdProgramCandidacyProcessState.WAITING_FOR_SCIENTIFIC_COUNCIL_RATIFICATION);
            final PhdCandidacyPredicate ratifiedScientificFilter =
                    new PhdCandidacyPredicate(PhdProgramCandidacyProcessState.RATIFIED_BY_SCIENTIFIC_COUNCIL);

            return new OrPredicate<PhdIndividualProgramProcess>() {
                {
                    add(waitingScientificFilter);
                    add(ratifiedScientificFilter);
                }
            };
        }
    },

    CONCLUDED {
        @Override
        public Predicate<PhdIndividualProgramProcess> getPredicate() {
            return new PhdCandidacyPredicate(PhdProgramCandidacyProcessState.CONCLUDED) {

                @Override
                public boolean eval(PhdIndividualProgramProcess process) {
                    return !process.hasSeminarProcess() && !process.hasThesisProcess() && super.eval(process);
                }
            };
        }
    };

    private static class PhdCandidacyPredicate extends
            InlinePredicate<PhdIndividualProgramProcess, PhdProgramCandidacyProcessState> {
        public PhdCandidacyPredicate(PhdProgramCandidacyProcessState candidacyState) {
            super(candidacyState);
        }

        @Override
        public boolean eval(PhdIndividualProgramProcess process) {
            return checkState(process) && checkValue(process);
        }

        private boolean checkState(PhdIndividualProgramProcess process) {
            return process.getActiveState().isActive();
        }

        private boolean checkValue(PhdIndividualProgramProcess process) {
            return process.getCandidacyProcess().getActiveState().equals(getValue());
        }
    }
}
