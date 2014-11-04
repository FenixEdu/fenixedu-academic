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
package net.sourceforge.fenixedu.domain.candidacy.workflow.form;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.util.workflow.Form;
import net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

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