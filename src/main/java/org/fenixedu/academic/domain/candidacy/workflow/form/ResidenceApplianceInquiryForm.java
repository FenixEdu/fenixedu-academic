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

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.util.workflow.Form;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;

public class ResidenceApplianceInquiryForm extends Form {

    private boolean isToApplyForResidence = false;

    private String notesAboutApplianceForResidence;

    public ResidenceApplianceInquiryForm() {
        super();
    }

    public boolean isToApplyForResidence() {
        return isToApplyForResidence;
    }

    public void setToApplyForResidence(boolean isToApplyForResidence) {
        this.isToApplyForResidence = isToApplyForResidence;
    }

    public String getNotesAboutApplianceForResidence() {
        return notesAboutApplianceForResidence;
    }

    public void setNotesAboutApplianceForResidence(String notesAboutApplianceForResidence) {
        this.notesAboutApplianceForResidence = notesAboutApplianceForResidence;
    }

    @Override
    public List<LabelFormatter> validate() {
        if (!StringUtils.isEmpty(this.notesAboutApplianceForResidence) && !isToApplyForResidence) {
            return Collections.singletonList(new LabelFormatter().appendLabel(
                    "error.candidacy.workflow.ResidenceApplianceInquiryForm.notes.can.only.be.filled.in.case.of.appliance",
                    Bundle.CANDIDATE));
        }

        return Collections.emptyList();
    }

    @Override
    public String getFormName() {
        return "label.candidacy.workflow.residenceApplianceForm";
    }

    @Override
    public String getFormDescription() {
        return "label.candidacy.workflow.residenceApplianceForm.description";
    }
}