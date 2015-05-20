/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.phd;

import java.util.function.Predicate;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcessState;
import org.fenixedu.academic.util.predicates.InlinePredicate;
import org.fenixedu.academic.util.predicates.OrPredicate;
import org.fenixedu.academic.util.predicates.PredicateContainer;
import org.fenixedu.bennu.core.i18n.BundleUtil;

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
                public boolean test(PhdIndividualProgramProcess process) {
                    return super.test(process) && process.getConclusionYear().equals(ExecutionYear.readCurrentExecutionYear());
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
        public boolean test(PhdIndividualProgramProcess process) {
            return checkState(process) && checkValue(process);
        }

        private boolean checkState(PhdIndividualProgramProcess process) {
            return !process.getActiveState().isPhdActive();
        }

        private boolean checkValue(PhdIndividualProgramProcess process) {
            return process.getActiveState().equals(getValue());
        }
    }

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString("resources.PhdResources", PhdInactivePredicateContainer.class.getName() + "." + name());
    }
}
