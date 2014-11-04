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
/**
 * 
 */
package org.fenixedu.academic.domain.phd.thesis.activities;

import org.fenixedu.academic.domain.caseHandling.PreConditionNotValidException;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramDocumentType;
import org.fenixedu.academic.domain.phd.PhdProgram;
import org.fenixedu.academic.domain.phd.PhdProgramDocumentUploadBean;
import org.fenixedu.academic.domain.phd.alert.AlertService;
import org.fenixedu.academic.domain.phd.alert.AlertService.AlertMessage;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcess;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcessBean;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcessStateType;
import org.fenixedu.bennu.core.domain.User;

public class SubmitThesis extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, User userView) {

        if (!process.isJuryValidated()) {
            throw new PreConditionNotValidException();
        }

        if (!process.hasState(PhdThesisProcessStateType.THESIS_DISCUSSION_DATE_SCHECULED)) {
            throw new PreConditionNotValidException();
        }

        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, User userView, Object object) {
        final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;

        for (final PhdProgramDocumentUploadBean each : bean.getDocuments()) {
            if (each.hasAnyInformation()) {

                process.addDocument(each, userView.getPerson());

                PhdProgram phdProgram = process.getIndividualProgramProcess().getPhdProgram();
                if (phdProgram.getDegree() == null) {
                    continue;
                }

                if (!isThesisFinalDocument(each)) {
                    throw new DomainException("error.SubmitThesis.unexpected.document");
                }

                if (bean.isToNotify()) {
                    notifyAllElements(process, bean);
                }

            }
        }

        if (!process.hasState(PhdThesisProcessStateType.WAITING_FOR_THESIS_RATIFICATION)) {
            process.createState(PhdThesisProcessStateType.WAITING_FOR_THESIS_RATIFICATION, userView.getPerson(),
                    bean.getRemarks());
        }

        return process;

    }

    private boolean isThesisFinalDocument(final PhdProgramDocumentUploadBean each) {
        return each.getType() == PhdIndividualProgramDocumentType.FINAL_THESIS;
    }

    private void notifyAllElements(final PhdThesisProcess process, final PhdThesisProcessBean bean) {

        final AlertMessage subject = AlertMessage.create("message.phd.thesis.submit.subject");
        final AlertMessage body = AlertMessage.create("message.phd.thesis.submit.body");

        AlertService.alertResponsibleCoordinators(process.getIndividualProgramProcess(), subject, body);
        AlertService.alertGuiders(process.getIndividualProgramProcess(), subject, body);
        AlertService.alertStudent(process.getIndividualProgramProcess(), subject, body);

    }

}