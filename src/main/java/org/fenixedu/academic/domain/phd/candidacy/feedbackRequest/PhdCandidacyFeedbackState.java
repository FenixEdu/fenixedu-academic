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
package org.fenixedu.academic.domain.phd.candidacy.feedbackRequest;

import java.util.List;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.exceptions.PhdDomainOperationException;
import org.joda.time.DateTime;

public class PhdCandidacyFeedbackState extends PhdCandidacyFeedbackState_Base {

    private PhdCandidacyFeedbackState() {
        super();
    }

    protected PhdCandidacyFeedbackState(final PhdCandidacyFeedbackRequestProcess process,
            final PhdCandidacyFeedbackStateType type, final Person person, final String remarks, final DateTime stateDate) {
        this();
        init(process, type, person, remarks, stateDate);
    }

    protected void init(final Person person, final String remarks, DateTime stateDate) {
        throw new RuntimeException("invoke other init");
    }

    private void init(PhdCandidacyFeedbackRequestProcess process, PhdCandidacyFeedbackStateType type, Person person,
            String remarks, final DateTime stateDate) {
        check(process, type);
        setProcess(process);
        super.init(person, remarks, stateDate, type);
        setType(type);
    }

    private void check(PhdCandidacyFeedbackRequestProcess process, PhdCandidacyFeedbackStateType type) {
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

    private void checkType(final PhdCandidacyFeedbackRequestProcess process, final PhdCandidacyFeedbackStateType type) {
        final PhdCandidacyFeedbackStateType currentType = process.getActiveState();
        if (currentType != null && currentType.equals(type)) {
            throw new PhdDomainOperationException("error.PhdCandidacyProcessState.equals.previous.state", type.getLocalizedName());
        }
    }

    @Override
    protected void disconnect() {
        setProcess(null);
        super.disconnect();
    }

    static public PhdCandidacyFeedbackState create(PhdCandidacyFeedbackRequestProcess process, PhdCandidacyFeedbackStateType type) {
        final PhdCandidacyFeedbackState result = new PhdCandidacyFeedbackState();

        result.check(process, type);
        result.setProcess(process);
        result.setType(type);

        return result;
    }

    @Override
    public boolean isLast() {
        return getProcess().getMostRecentState() == this;
    }

    public PhdCandidacyFeedbackState createWithInferredStateDate(final PhdCandidacyFeedbackRequestProcess process,
            final PhdCandidacyFeedbackStateType type, final Person person, final String remarks) {
        return createWithGivenStateDate(process, type, person, remarks, new DateTime());
    }

    public PhdCandidacyFeedbackState createWithGivenStateDate(final PhdCandidacyFeedbackRequestProcess process,
            final PhdCandidacyFeedbackStateType type, final Person person, final String remarks, final DateTime stateDate) {
        List<PhdCandidacyFeedbackStateType> possibleNextStates = PhdCandidacyFeedbackStateType.getPossibleNextStates(type);

        if (!possibleNextStates.contains(type)) {
            String description = buildExpectedStatesDescription(possibleNextStates);

            throw new PhdDomainOperationException("error.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackState.invalid.state",
                    type.getLocalizedName(), description);
        }

        return new PhdCandidacyFeedbackState(process, type, person, remarks, stateDate);
    }

}
