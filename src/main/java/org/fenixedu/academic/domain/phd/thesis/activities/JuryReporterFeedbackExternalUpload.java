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

import org.fenixedu.academic.domain.phd.PhdProgramDocumentUploadBean;
import org.fenixedu.academic.domain.phd.PhdThesisReportFeedbackDocument;
import org.fenixedu.academic.domain.phd.access.ExternalAccessPhdActivity;
import org.fenixedu.academic.domain.phd.access.PhdExternalOperationBean;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcess;
import org.fenixedu.bennu.core.domain.User;

public class JuryReporterFeedbackExternalUpload extends ExternalAccessPhdActivity<PhdThesisProcess> {

    @Override
    public void checkPreConditions(PhdThesisProcess process, User userView) {
        // chekc if already has documents ....
    }

    @Override
    protected PhdThesisProcess internalExecuteActivity(PhdThesisProcess process, User userView, PhdExternalOperationBean bean) {

        final PhdProgramDocumentUploadBean documentBean = bean.getDocumentBean();
        if (documentBean.hasAnyInformation()) {
            new PhdThesisReportFeedbackDocument(bean.getParticipant().getThesisJuryElement(process), documentBean.getRemarks(),
                    documentBean.getFileContent(), documentBean.getFilename(), null);
        }

        return process;
    }

}