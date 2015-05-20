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

import static org.fenixedu.academic.domain.phd.thesis.PhdThesisProcessStateType.JURY_VALIDATED;
import static org.fenixedu.academic.domain.phd.thesis.PhdThesisProcessStateType.JURY_WAITING_FOR_VALIDATION;
import static org.fenixedu.academic.domain.phd.thesis.PhdThesisProcessStateType.NEW;
import static org.fenixedu.academic.domain.phd.thesis.PhdThesisProcessStateType.THESIS_DISCUSSION_DATE_SCHECULED;
import static org.fenixedu.academic.domain.phd.thesis.PhdThesisProcessStateType.WAITING_FOR_JURY_CONSTITUTION;
import static org.fenixedu.academic.domain.phd.thesis.PhdThesisProcessStateType.WAITING_FOR_JURY_REPORTER_FEEDBACK;
import static org.fenixedu.academic.domain.phd.thesis.PhdThesisProcessStateType.WAITING_FOR_THESIS_DISCUSSION_DATE_SCHEDULING;
import static org.fenixedu.academic.domain.phd.thesis.PhdThesisProcessStateType.WAITING_FOR_THESIS_MEETING_SCHEDULING;

import java.util.function.Predicate;

import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcessStateType;
import org.fenixedu.academic.util.predicates.InlinePredicate;
import org.fenixedu.academic.util.predicates.OrPredicate;
import org.fenixedu.academic.util.predicates.PredicateContainer;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public enum PhdThesisPredicateContainer implements PredicateContainer<PhdIndividualProgramProcess> {

    PROVISIONAL_THESIS_DELIVERED {
        @Override
        public Predicate<PhdIndividualProgramProcess> getPredicate() {

            return new OrPredicate<PhdIndividualProgramProcess>() {
                {

                    // can remove some states if we want to distinguish each
                    // situation
                    add(new PhdThesisPredicate(NEW));
                    add(new PhdThesisPredicate(WAITING_FOR_JURY_CONSTITUTION));
                    add(new PhdThesisPredicate(JURY_WAITING_FOR_VALIDATION));
                    add(new PhdThesisPredicate(JURY_VALIDATED));

                    add(new PhdThesisPredicate(WAITING_FOR_JURY_REPORTER_FEEDBACK));
                    add(new PhdThesisPredicate(WAITING_FOR_THESIS_MEETING_SCHEDULING));
                    add(new PhdThesisPredicate(WAITING_FOR_THESIS_DISCUSSION_DATE_SCHEDULING));
                }
            };
        }
    },

    DISCUSSION_SCHEDULED {
        @Override
        public Predicate<PhdIndividualProgramProcess> getPredicate() {
            return new PhdThesisPredicate(THESIS_DISCUSSION_DATE_SCHECULED);
        }
    };

    private static class PhdThesisPredicate extends InlinePredicate<PhdIndividualProgramProcess, PhdThesisProcessStateType> {
        public PhdThesisPredicate(PhdThesisProcessStateType thesisState) {
            super(thesisState);
        }

        @Override
        public boolean test(PhdIndividualProgramProcess process) {
            return checkState(process) && checkValue(process);
        }

        private boolean checkState(PhdIndividualProgramProcess process) {
            return process.getActiveState().isActive() && process.getThesisProcess() != null;
        }

        private boolean checkValue(PhdIndividualProgramProcess process) {
            return process.getThesisProcess().getActiveState().equals(getValue());
        }
    }

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString("resources.PhdResources", PhdThesisPredicateContainer.class.getName() + "." + name());
    }
}
