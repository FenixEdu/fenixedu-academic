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

import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.seminar.PublicPresentationSeminarProcessStateType;
import org.fenixedu.academic.util.predicates.InlinePredicate;
import org.fenixedu.academic.util.predicates.OrPredicate;
import org.fenixedu.academic.util.predicates.PredicateContainer;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public enum PhdSeminarPredicateContainer implements PredicateContainer<PhdIndividualProgramProcess> {

    SEMINAR_PROCESS_STARTED {
        @Override
        public Predicate<PhdIndividualProgramProcess> getPredicate() {
            final PhdSeminarPredicate waitingComissionConstitutionPredicate =
                    new PhdSeminarPredicate(PublicPresentationSeminarProcessStateType.WAITING_FOR_COMMISSION_CONSTITUTION);
            final PhdSeminarPredicate waitingComissionValidationPredicate =
                    new PhdSeminarPredicate(PublicPresentationSeminarProcessStateType.COMMISSION_WAITING_FOR_VALIDATION);
            final PhdSeminarPredicate comissionvalidatedPredicate =
                    new PhdSeminarPredicate(PublicPresentationSeminarProcessStateType.COMMISSION_VALIDATED);
            final PhdSeminarPredicate presentationScheduledPredicate =
                    new PhdSeminarPredicate(PublicPresentationSeminarProcessStateType.PUBLIC_PRESENTATION_DATE_SCHEDULED);

            return new OrPredicate<PhdIndividualProgramProcess>() {
                {
                    add(waitingComissionConstitutionPredicate);
                    add(waitingComissionValidationPredicate);
                    add(comissionvalidatedPredicate);
                    add(presentationScheduledPredicate);
                }
            };
        }
    },

    AFTER_FIRST_SEMINAR_REUNION {
        @Override
        public Predicate<PhdIndividualProgramProcess> getPredicate() {

            return new PhdSeminarPredicate(PublicPresentationSeminarProcessStateType.REPORT_WAITING_FOR_VALIDATION) {
                @Override
                public boolean test(PhdIndividualProgramProcess process) {
                    return process.getThesisProcess() == null && super.checkState(process)
                            && process.getSeminarProcess().hasState(getValue());
                }
            };
        }
    };

    private static class PhdSeminarPredicate extends
            InlinePredicate<PhdIndividualProgramProcess, PublicPresentationSeminarProcessStateType> {
        public PhdSeminarPredicate(PublicPresentationSeminarProcessStateType seminarState) {
            super(seminarState);
        }

        @Override
        public boolean test(PhdIndividualProgramProcess process) {
            return checkState(process) && checkValue(process);
        }

        private boolean checkState(PhdIndividualProgramProcess process) {
            return process.getActiveState().isActive() && process.getSeminarProcess() != null;
        }

        private boolean checkValue(PhdIndividualProgramProcess process) {
            return process.getSeminarProcess().getActiveState().equals(getValue());
        }
    }

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString("resources.PhdResources", PhdSeminarPredicateContainer.class.getName() + "." + name());
    }
}
