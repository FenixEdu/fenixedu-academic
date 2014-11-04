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
package org.fenixedu.academic.domain.phd.candidacy.activities;

import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.caseHandling.PreConditionNotValidException;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdProgramCandidacyProcessState;
import org.fenixedu.academic.domain.phd.alert.AlertService;
import org.fenixedu.academic.domain.phd.candidacy.PhdProgramCandidacyProcess;
import org.fenixedu.academic.domain.phd.candidacy.PhdProgramCandidacyProcessStateBean;
import org.fenixedu.bennu.core.domain.User;

public class RequestRatifyCandidacy extends PhdProgramCandidacyProcessActivity {

    @Override
    protected void activityPreConditions(PhdProgramCandidacyProcess process, User userView) {
        if (!process.isInState(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION)) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, User userView, Object object) {
        final PhdProgramCandidacyProcessStateBean bean = (PhdProgramCandidacyProcessStateBean) object;

        if (!process.getIndividualProgramProcess().getPhdConfigurationIndividualProgramProcess().isMigratedProcess()
                && process.getCandidacyReviewDocuments().isEmpty()) {
            throw new DomainException(
                    "error.phd.candidacy.PhdProgramCandidacyProcess.RequestRatifyCandidacy.candidacy.review.document.is.required");
        }

        process.createState(PhdProgramCandidacyProcessState.WAITING_FOR_SCIENTIFIC_COUNCIL_RATIFICATION, userView.getPerson(),
                bean.getRemarks());

        if (bean.getGenerateAlert()) {
            AlertService.alertAcademicOffice(process.getIndividualProgramProcess(),
                    AcademicOperationType.VIEW_PHD_CANDIDACY_ALERTS, "message.phd.alert.candidacy.request.ratify.subject",
                    "message.phd.alert.candidacy.request.ratify.body");
        }

        return process;
    }

}
