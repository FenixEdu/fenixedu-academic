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
package org.fenixedu.academic.domain.candidacy.workflow.form;

import java.util.Collections;
import java.util.List;

import org.fenixedu.academic.domain.util.workflow.Form;
import org.fenixedu.academic.util.LabelFormatter;
import org.fenixedu.academic.util.StudentPersonalDataAuthorizationChoice;

public class InquiryAboutYieldingPersonalDataForm extends Form {

    private StudentPersonalDataAuthorizationChoice personalDataAuthorizationChoice;
    private boolean personalDataAuthorizationForStudentsAssociation = true;

    public InquiryAboutYieldingPersonalDataForm() {
        super();
    }

    public InquiryAboutYieldingPersonalDataForm(final StudentPersonalDataAuthorizationChoice personalDataAuthorizationChoice) {
        this();
        this.personalDataAuthorizationChoice = personalDataAuthorizationChoice;
    }

    public StudentPersonalDataAuthorizationChoice getPersonalDataAuthorizationChoice() {
        return personalDataAuthorizationChoice;
    }

    public void setPersonalDataAuthorizationChoice(StudentPersonalDataAuthorizationChoice personalDataAuthorizationChoice) {
        this.personalDataAuthorizationChoice = personalDataAuthorizationChoice;
    }

    public void setPersonalDataAuthorizationForStudentsAssociation(boolean personalDataAuthorizationForStudentsAssociation) {
        this.personalDataAuthorizationForStudentsAssociation = personalDataAuthorizationForStudentsAssociation;
    }

    public boolean isPersonalDataAuthorizationForStudentsAssociation() {
        return personalDataAuthorizationForStudentsAssociation;
    }

    public StudentPersonalDataAuthorizationChoice getPersonalDataAuthorizationForStudentsAssociation() {
        return StudentPersonalDataAuthorizationChoice
                .getPersonalDataAuthorizationForStudentsAssociationType(isPersonalDataAuthorizationForStudentsAssociation());
    }

    @Override
    public List<LabelFormatter> validate() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public String getFormName() {
        return "label.candidacy.workflow.inquiryAboutYieldingPersonalDataForm";
    }
}