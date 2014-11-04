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
package org.fenixedu.academic.domain.phd.thesis.activities;

import org.fenixedu.academic.domain.caseHandling.PreConditionNotValidException;
import org.fenixedu.academic.domain.phd.PhdProgramDocumentUploadBean;
import org.fenixedu.academic.domain.phd.PhdThesisReportFeedbackDocument;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcess;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcessBean;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcessStateType;
import org.fenixedu.academic.domain.phd.thesis.ThesisJuryElement;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.bennu.core.domain.User;

public class JuryReporterFeedbackUpload extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, User userView) {

        if (!process.isJuryValidated()) {
            throw new PreConditionNotValidException();
        }

        if (!process.hasState(PhdThesisProcessStateType.WAITING_FOR_JURY_REPORTER_FEEDBACK)) {
            throw new PreConditionNotValidException();
        }

        if (process.isAllowedToManageProcess(userView)) {
            return;
        }

        if (!process.isParticipant(userView.getPerson())) {
            throw new PreConditionNotValidException();
        }

        final ThesisJuryElement element = process.getThesisJuryElement(userView.getPerson());
        if (element == null || !element.getReporter().booleanValue()) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, User userView, Object object) {
        final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;

        for (final PhdProgramDocumentUploadBean documentBean : bean.getDocuments()) {
            if (documentBean.hasAnyInformation()) {
                new PhdThesisReportFeedbackDocument(bean.getJuryElement(), documentBean.getRemarks(),
                        documentBean.getFileContent(), documentBean.getFilename(), AccessControl.getPerson());
            }
        }

        return process;
    }

}