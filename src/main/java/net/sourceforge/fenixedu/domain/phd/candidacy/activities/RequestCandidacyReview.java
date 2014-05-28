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
package net.sourceforge.fenixedu.domain.phd.candidacy.activities;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.domain.Installation;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService.AlertMessage;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessStateBean;

import org.fenixedu.bennu.core.domain.User;

public class RequestCandidacyReview extends PhdProgramCandidacyProcessActivity {

    static final private List<PhdProgramCandidacyProcessState> PREVIOUS_STATE = Arrays.asList(

    PhdProgramCandidacyProcessState.PRE_CANDIDATE,

    PhdProgramCandidacyProcessState.STAND_BY_WITH_MISSING_INFORMATION,

    PhdProgramCandidacyProcessState.STAND_BY_WITH_COMPLETE_INFORMATION,

    PhdProgramCandidacyProcessState.REJECTED,

    PhdProgramCandidacyProcessState.WAITING_FOR_SCIENTIFIC_COUNCIL_RATIFICATION);

    @Override
    protected void activityPreConditions(PhdProgramCandidacyProcess process, User userView) {
        if (PREVIOUS_STATE.contains(process.getActiveState())) {
            return;
        }
        throw new PreConditionNotValidException();
    }

    @Override
    protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, User userView, Object object) {

        final PhdIndividualProgramProcess mainProcess = process.getIndividualProgramProcess();
        if (!mainProcess.hasPhdProgram()) {
            throw new DomainException("error.phd.candidacy.PhdProgramCandidacyProcess.RequestCandidacyReview.invalid.phd.program");
        }

        final PhdProgramCandidacyProcessStateBean bean = (PhdProgramCandidacyProcessStateBean) object;
        process.createState(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION, userView.getPerson(),
                bean.getRemarks());

        if (bean.getGenerateAlert()) {
            AlertService.alertCoordinators(mainProcess, subject(), body(mainProcess));
        }

        return process;
    }

    private AlertMessage subject() {
        return AlertMessage.create("message.phd.alert.candidacy.review.subject");
    }

    private AlertMessage body(final PhdIndividualProgramProcess process) {
        return AlertMessage.create("message.phd.alert.candidacy.review.body").args(process.getProcessNumber(),
                process.getPerson().getName(), Installation.getInstance().getInstituitionalEmailAddress("suporte"));
    }

}
