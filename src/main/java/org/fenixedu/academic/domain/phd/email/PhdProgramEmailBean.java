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
package org.fenixedu.academic.domain.phd.email;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdProgram;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.util.MultiLanguageString;
import org.fenixedu.academic.util.predicates.AndPredicate;
import org.fenixedu.academic.util.predicates.InlinePredicate;
import org.joda.time.DateTime;

public class PhdProgramEmailBean extends PhdEmailBean {

    private static final long serialVersionUID = 1L;

    private PhdProgram phdProgram;
    private List<PhdIndividualProgramProcess> selectedElements;
    private boolean showProgramsChoice = true;

    public PhdProgramEmailBean() {

    }

    public PhdProgramEmailBean(PhdProgram phdProgram) {
        this.phdProgram = phdProgram;
    }

    public PhdProgramEmailBean(PhdProgramEmail email) {
        this.subject = email.getFormattedSubject().getContent(MultiLanguageString.pt);
        this.message = email.getFormattedBody().getContent(MultiLanguageString.pt);
        this.bccs = email.getBccs();
        this.creationDate = email.getWhenCreated();
        this.creator = email.getPerson();
    }

    public PhdProgram getPhdProgram() {
        return phdProgram;
    }

    public void setPhdProgram(PhdProgram phdProgram) {
        this.phdProgram = phdProgram;
    }

    public List<PhdIndividualProgramProcess> getSelectedElements() {
        if (selectedElements != null) {
            return selectedElements;
        }

        return new ArrayList<PhdIndividualProgramProcess>();
    }

    public void setSelectedElements(List<PhdIndividualProgramProcess> selectedElements) {
        this.selectedElements = selectedElements;
    }

    public AndPredicate<PhdIndividualProgramProcess> getManagedPhdProgramsPredicate() {
        final AndPredicate<PhdIndividualProgramProcess> result = new AndPredicate<PhdIndividualProgramProcess>();
        if (getPhdProgram() != null) {
            result.add(new InlinePredicate<PhdIndividualProgramProcess, PhdProgram>(getPhdProgram()) {

                @Override
                public boolean test(PhdIndividualProgramProcess toEval) {
                    if (toEval.getPhdProgram() != null) {
                        return getValue().equals(toEval.getPhdProgram());
                    } else if (toEval.getPhdProgramFocusArea() != null) {
                        return !CollectionUtils.intersection(Collections.singleton(getValue()),
                                toEval.getPhdProgramFocusArea().getPhdProgramsSet()).isEmpty();
                    } else {
                        return false;
                    }
                }
            });
        }

        return result;
    }

    public void updateBean() {
        setCreationDate(new DateTime());
        setCreator(AccessControl.getPerson());
    }

    public String getBccsWithSelectedParticipants() {
        String bccs = getBccs() == null ? null : getBccs().replace(" ", "");

        if (!StringUtils.isEmpty(bccs)) {
            bccs += ",";
        }

        for (PhdIndividualProgramProcess process : getSelectedElements()) {
            if (process.getPerson().getEmailForSendingEmails() != null) {
                bccs += process.getPerson().getEmailForSendingEmails();
                bccs += ",";
            }
        }

        if (bccs.endsWith(",")) {
            bccs = bccs.substring(0, bccs.length() - 1);
        }

        return bccs;
    }

    public boolean isShowProgramsChoice() {
        return showProgramsChoice;
    }

    public void setShowProgramsChoice(boolean showProgramsChoice) {
        this.showProgramsChoice = showProgramsChoice;
    }

}
