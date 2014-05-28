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
package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ApprovedLearningAgreementExecutedAction extends ApprovedLearningAgreementExecutedAction_Base {

    private ApprovedLearningAgreementExecutedAction() {
        super();
    }

    public ApprovedLearningAgreementExecutedAction(ApprovedLearningAgreementDocumentFile documentFile, ExecutedActionType type) {
        this();
        init(documentFile, type);
    }

    protected void init(ApprovedLearningAgreementDocumentFile documentFile, ExecutedActionType type) {
        super.init(type);

        if (documentFile == null) {
            throw new DomainException("error.erasmus.approved.learning.agreement.execution.action.document.file.is.null");
        }

        setApprovedLearningAgreement(documentFile);
    }

    public boolean isSentLearningAgreementAction() {
        return ExecutedActionType.SENT_APPROVED_LEARNING_AGREEMENT.equals(getType());
    }

    public boolean isViewedLearningAgreementAction() {
        return ExecutedActionType.VIEWED_APPROVED_LEARNING_AGREEMENT.equals(getType());
    }

    public boolean isSentEmailAcceptedStudent() {
        return ExecutedActionType.SENT_EMAIL_ACCEPTED_STUDENT.equals(getType());
    }

    @Deprecated
    public boolean hasApprovedLearningAgreement() {
        return getApprovedLearningAgreement() != null;
    }

}
