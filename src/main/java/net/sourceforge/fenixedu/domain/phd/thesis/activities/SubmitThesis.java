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
/**
 * 
 */
package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService.AlertMessage;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;

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
                if (!phdProgram.hasDegree()) {
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