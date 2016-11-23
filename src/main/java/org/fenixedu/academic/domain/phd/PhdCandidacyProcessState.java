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
package org.fenixedu.academic.domain.phd;

import java.util.List;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.candidacy.AdmittedCandidacySituation;
import org.fenixedu.academic.domain.candidacy.CandidacySituation;
import org.fenixedu.academic.domain.candidacy.NotAdmittedCandidacySituation;
import org.fenixedu.academic.domain.candidacy.PreCandidacySituation;
import org.fenixedu.academic.domain.candidacy.StandByCandidacySituation;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.candidacy.PHDProgramCandidacy;
import org.fenixedu.academic.domain.phd.candidacy.PhdProgramCandidacyProcess;
import org.fenixedu.academic.domain.phd.exceptions.PhdDomainOperationException;
import org.joda.time.DateTime;

public class PhdCandidacyProcessState extends PhdCandidacyProcessState_Base {

    private PhdCandidacyProcessState() {
        super();
    }

    protected PhdCandidacyProcessState(final PhdProgramCandidacyProcess process, final PhdProgramCandidacyProcessState type,
            final Person person, final String remarks, final DateTime stateDate) {
        this();
        init(process, type, person, remarks, stateDate);

        updateSituationOnPHDCandidacy();
    }

    public void updateSituationOnPHDCandidacy() {
        PhdProgramCandidacyProcess process = getProcess();

        if (this.getStateDate() == null) {
            throw new DomainException("state.date.null");
        }

        PHDProgramCandidacy candidacy = process.getCandidacy();
        CandidacySituation situation = null;

        switch (this.getType()) {
        case PRE_CANDIDATE:
            situation = new PreCandidacySituation(candidacy);
            break;
        case STAND_BY_WITH_MISSING_INFORMATION:
        case STAND_BY_WITH_COMPLETE_INFORMATION:
            situation = new StandByCandidacySituation(candidacy);
            break;
        case CONCLUDED:
            situation = new AdmittedCandidacySituation(candidacy);
            break;
        case REJECTED:
            situation = new NotAdmittedCandidacySituation(candidacy);
            break;
        default:
        }

        if (situation != null) {
            situation.setSituationDate(this.getStateDate());
        }
    }

    protected void init(final Person person, final String remarks, DateTime stateDate) {
        throw new RuntimeException("invoke other init");
    }

    protected void init(PhdProgramCandidacyProcess process, PhdProgramCandidacyProcessState type, Person person, String remarks,
            final DateTime stateDate) {
        check(process, type);
        setProcess(process);
        super.init(person, remarks, stateDate, type);

        setType(type);
    }

    private void check(PhdProgramCandidacyProcess process, PhdProgramCandidacyProcessState type) {
        String[] args = {};
        if (process == null) {
            throw new DomainException("error.PhdCandidacyProcessState.invalid.process", args);
        }
        String[] args1 = {};
        if (type == null) {
            throw new DomainException("error.PhdCandidacyProcessState.invalid.type", args1);
        }
        checkType(process, type);
    }

    private void checkType(final PhdProgramCandidacyProcess process, final PhdProgramCandidacyProcessState type) {
        final PhdProgramCandidacyProcessState currentType = process.getActiveState();
        if (currentType != null && currentType.equals(type)) {
            throw new DomainException("error.PhdCandidacyProcessState.equals.previous.state", type.getLocalizedName());
        }
    }

    @Override
    protected void disconnect() {
        setProcess(null);
        super.disconnect();
    }

    static public PhdCandidacyProcessState create(PhdProgramCandidacyProcess process, PhdProgramCandidacyProcessState type) {
        final PhdCandidacyProcessState result = new PhdCandidacyProcessState();

        result.check(process, type);
        result.setProcess(process);
        result.setType(type);

        return result;
    }

    @Override
    public boolean isLast() {
        return getProcess().getMostRecentState() == this;
    }

    public static PhdCandidacyProcessState createStateWithInferredStateDate(final PhdProgramCandidacyProcess process,
            final PhdProgramCandidacyProcessState type, final Person person, final String remarks) {

        DateTime stateDate = null;

        PhdCandidacyProcessState mostRecentState = process.getMostRecentState();

        switch (type) {
        case PRE_CANDIDATE:
        case STAND_BY_WITH_MISSING_INFORMATION:
        case STAND_BY_WITH_COMPLETE_INFORMATION:
        case PENDING_FOR_COORDINATOR_OPINION:
        case WAITING_FOR_SCIENTIFIC_COUNCIL_RATIFICATION:
            if (mostRecentState != null) {
                stateDate = mostRecentState.getStateDate().plusMinutes(1);
            } else {
                if (process.getCandidacyDate() == null) {
                    throw new PhdDomainOperationException("error.phd.PhdCandidacyProcessState.candidacyDate.required");
                }

                stateDate = process.getCandidacyDate().toDateTimeAtStartOfDay();
            }

            break;
        case RATIFIED_BY_SCIENTIFIC_COUNCIL:
            if (process.getWhenRatified() == null) {
                throw new PhdDomainOperationException("error.phd.PhdCandidacyProcessState.whenRatified.required");
            }

            stateDate = process.getWhenRatified().toDateTimeAtStartOfDay();
            break;
        case CONCLUDED:
            if (process.getWhenStartedStudies() == null) {
                throw new PhdDomainOperationException("error.phd.PhdCandidacyProcessState.whenStartedStudies.required");
            }

            stateDate = process.getWhenStartedStudies().toDateTimeAtStartOfDay();
            break;
        case REJECTED:
            stateDate = mostRecentState.getStateDate().plusMinutes(1);
            break;
        default:
            throw new DomainException("I cant handle this");
        }

        return createStateWithGivenStateDate(process, type, person, remarks, stateDate);
    }

    public static PhdCandidacyProcessState createStateWithGivenStateDate(final PhdProgramCandidacyProcess process,
            final PhdProgramCandidacyProcessState type, final Person person, final String remarks, final DateTime stateDate) {
        List<PhdProgramCandidacyProcessState> nextPossibleStates = PhdProgramCandidacyProcessState.getPossibleNextStates(process);

        if (!nextPossibleStates.contains(type)) {
            String description = buildExpectedStatesDescription(nextPossibleStates);

            throw new PhdDomainOperationException("error.phd.candidacy.PhdProgramCandidacyProcess.invalid.state",
                    type.getLocalizedName(), description);
        }

        return new PhdCandidacyProcessState(process, type, person, remarks, stateDate);
    }

}
