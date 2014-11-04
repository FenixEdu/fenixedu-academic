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

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;
import pt.utl.ist.fenix.tools.predicates.InlinePredicate;
import pt.utl.ist.fenix.tools.predicates.OrPredicate;
import pt.utl.ist.fenix.tools.predicates.Predicate;
import pt.utl.ist.fenix.tools.predicates.PredicateContainer;

public enum PhdInactivePredicateContainer implements PredicateContainer<PhdIndividualProgramProcess> {

    SUSPENDED {
        @Override
        public Predicate<PhdIndividualProgramProcess> getPredicate() {
            return new PhdInactivePredicate(PhdIndividualProgramProcessState.SUSPENDED);
        }
    },

    CONCLUDED {
        @Override
        public Predicate<PhdIndividualProgramProcess> getPredicate() {
            return new PhdInactivePredicate(PhdIndividualProgramProcessState.CONCLUDED);
        }
    },

    CONCLUDED_THIS_YEAR {
        @Override
        public Predicate<PhdIndividualProgramProcess> getPredicate() {
            return new PhdInactivePredicate(PhdIndividualProgramProcessState.CONCLUDED) {
                @Override
                public boolean eval(PhdIndividualProgramProcess process) {
                    return super.eval(process) && process.getConclusionYear().equals(ExecutionYear.readCurrentExecutionYear());
                }
            };
        }
    },

    ABOLISHED {
        @Override
        public Predicate<PhdIndividualProgramProcess> getPredicate() {
            final PhdInactivePredicate cancelledPredicate = new PhdInactivePredicate(PhdIndividualProgramProcessState.CANCELLED);
            final PhdInactivePredicate flunkedPredicate = new PhdInactivePredicate(PhdIndividualProgramProcessState.FLUNKED);
            final PhdInactivePredicate notAdmittedPredicate =
                    new PhdInactivePredicate(PhdIndividualProgramProcessState.NOT_ADMITTED);

            return new OrPredicate<PhdIndividualProgramProcess>() {
                {
                    add(cancelledPredicate);
                    add(flunkedPredicate);
                    add(notAdmittedPredicate);
                }
            };
        }
    };

    private static class PhdInactivePredicate extends
            InlinePredicate<PhdIndividualProgramProcess, PhdIndividualProgramProcessState> {
        public PhdInactivePredicate(PhdIndividualProgramProcessState processState) {
            super(processState);
        }

        @Override
        public boolean eval(PhdIndividualProgramProcess process) {
            return checkState(process) && checkValue(process);
        }

        private boolean checkState(PhdIndividualProgramProcess process) {
            return !process.getActiveState().isPhdActive();
        }

        private boolean checkValue(PhdIndividualProgramProcess process) {
            return process.getActiveState().equals(getValue());
        }
    }
}
