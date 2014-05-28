/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
