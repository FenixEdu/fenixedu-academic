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
package net.sourceforge.fenixedu.domain.phd.seminar;

import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;

import org.joda.time.DateTime;

public class PublicPresentationSeminarState extends PublicPresentationSeminarState_Base {

    private PublicPresentationSeminarState() {
        super();
    }

    PublicPresentationSeminarState(final PublicPresentationSeminarProcess process,
            final PublicPresentationSeminarProcessStateType type, final Person person, final String remarks,
            final DateTime stateDate) {
        this();
        String[] args = {};
        if (process == null) {
            throw new DomainException("error.PublicPresentationSeminarState.invalid.process", args);
        }
        String[] args1 = {};
        if (type == null) {
            throw new DomainException("error.PublicPresentationSeminarState.invalid.type", args1);
        }

        checkType(process, type);
        setProcess(process);

        super.init(person, remarks, stateDate, type);

        setType(type);
    }

    protected void init(final Person person, final String remarks, DateTime stateDate) {
        throw new RuntimeException("invoke other init");
    }

    private void checkType(final PublicPresentationSeminarProcess process, final PublicPresentationSeminarProcessStateType type) {
        final PublicPresentationSeminarProcessStateType currentType = process.getActiveState();
        if (currentType != null && currentType.equals(type)) {
            throw new PhdDomainOperationException("error.PublicPresentationSeminarState.equals.previous.state",
                    type.getLocalizedName());
        }
    }

    @Override
    protected void disconnect() {
        setProcess(null);
        super.disconnect();
    }

    @Override
    public boolean isLast() {
        return getProcess().getMostRecentState() == this;
    }

    public static PublicPresentationSeminarState createWithInferredStateDate(final PublicPresentationSeminarProcess process,
            final PublicPresentationSeminarProcessStateType type, final Person person, final String remarks) {
        DateTime stateDate = null;

        PublicPresentationSeminarState mostRecentState = process.getMostRecentState();

        switch (type) {
        case WAITING_FOR_COMMISSION_CONSTITUTION:
            if (process.getPresentationRequestDate() == null) {
                throw new PhdDomainOperationException(
                        "error.phd.seminar.PublicPresentationSeminarState.presentationRequestDate.required");
            }

            stateDate = process.getPresentationRequestDate().toDateTimeAtStartOfDay();
            break;
        case COMMISSION_WAITING_FOR_VALIDATION:
            stateDate = mostRecentState.getStateDate().plusMinutes(1);
            break;
        case COMMISSION_VALIDATED:
            stateDate = mostRecentState.getStateDate().plusMinutes(1);
            break;
        case PUBLIC_PRESENTATION_DATE_SCHEDULED:
            stateDate = mostRecentState.getStateDate().plusMinutes(1);
            break;
        case REPORT_WAITING_FOR_VALIDATION:
            if (process.getMostRecentStateByType(PublicPresentationSeminarProcessStateType.REPORT_WAITING_FOR_VALIDATION) != null) {
                stateDate = mostRecentState.getStateDate().plusMinutes(1);
                break;
            }

            if (process.getPresentationDate() == null) {
                throw new PhdDomainOperationException(
                        "error.phd.seminar.PublicPresentationSeminarState.presentationDate.required");
            }

            stateDate = process.getPresentationDate().toDateTimeAtStartOfDay();
            break;
        case REPORT_VALIDATED:
            stateDate = mostRecentState.getStateDate().plusMinutes(1);
            break;
        case EXEMPTED:
            stateDate = new DateTime();
            break;
        }

        return createWithGivenStateDate(process, type, person, remarks, stateDate);
    }

    public static PublicPresentationSeminarState createWithGivenStateDate(final PublicPresentationSeminarProcess process,
            final PublicPresentationSeminarProcessStateType type, final Person person, final String remarks,
            final DateTime stateDate) {
        List<PublicPresentationSeminarProcessStateType> possibleNextStates =
                PublicPresentationSeminarProcessStateType.getPossibleNextStates(process);

        if (!possibleNextStates.contains(type)) {
            String expectedStatesDescription = buildExpectedStatesDescription(possibleNextStates);
            throw new PhdDomainOperationException("error.phd.seminar.PublicPresentationSeminarState.invalid.next.state",
                    type.getLocalizedName(), expectedStatesDescription);
        }

        return new PublicPresentationSeminarState(process, type, person, remarks, stateDate);
    }

    @Deprecated
    public boolean hasType() {
        return getType() != null;
    }

    @Deprecated
    public boolean hasProcess() {
        return getProcess() != null;
    }

}
