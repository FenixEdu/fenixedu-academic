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
package org.fenixedu.academic.domain.student.registrationStates;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.util.workflow.IState;
import org.fenixedu.academic.domain.util.workflow.StateBean;
import org.fenixedu.academic.domain.util.workflow.StateMachine;
import org.fenixedu.academic.dto.student.RegistrationStateBean;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.EnrolmentAction;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public abstract class RegistrationState extends RegistrationState_Base implements IState {

    static {
        getRelationRegistrationStateRegistration().addListener(new RelationAdapter<RegistrationState, Registration>() {

            @Override
            public void afterAdd(RegistrationState state, Registration registration) {
                super.afterAdd(state, registration);

                if (registration != null && state != null) {
                    new RegistrationStateLog(state, EnrolmentAction.ENROL, AccessControl.getPerson());
                }
            }

            @Override
            public void beforeRemove(RegistrationState state, Registration registration) {
                super.beforeRemove(state, registration);

                if (registration != null && state != null) {
                    new RegistrationStateLog(state, EnrolmentAction.UNENROL, AccessControl.getPerson());
                }
            }

        });
    }

    public static Comparator<RegistrationState> DATE_COMPARATOR = new Comparator<RegistrationState>() {
        @Override
        public int compare(RegistrationState leftState, RegistrationState rightState) {
            int comparationResult = leftState.getStateDate().compareTo(rightState.getStateDate());
            return (comparationResult == 0) ? leftState.getExternalId().compareTo(rightState.getExternalId()) : comparationResult;
        }
    };

    public static Comparator<RegistrationState> EXECUTION_INTERVAL_AND_DATE_COMPARATOR =
            Comparator.comparing(RegistrationState::getExecutionInterval, ExecutionInterval.COMPARATOR_BY_BEGIN_DATE)
                    .thenComparing(DATE_COMPARATOR);

    public RegistrationState() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    private static RegistrationState createState(Registration registration, Person person, DateTime dateTime,
            RegistrationStateType stateType) {

        RegistrationState newState = null;
        switch (stateType) {
        case REGISTERED:
            newState = new RegisteredState(registration, person, dateTime);
            break;
        case CANCELED:
            newState = new CanceledState(registration, person, dateTime);
            break;
        case CONCLUDED:
            newState = new ConcludedState(registration, person, dateTime);
            break;
        case FLUNKED:
            newState = new FlunkedState(registration, person, dateTime);
            break;
        case INTERRUPTED:
            newState = new InterruptedState(registration, person, dateTime);
            break;
        case SCHOOLPARTCONCLUDED:
            newState = new SchoolPartConcludedState(registration, person, dateTime);
            break;
        case STUDYPLANCONCLUDED:
            newState = new StudyPlanConcludedState(registration, person, dateTime);
            break;
        case INTERNAL_ABANDON:
            newState = new InternalAbandonState(registration, person, dateTime);
            break;
        case EXTERNAL_ABANDON:
            newState = new ExternalAbandonState(registration, person, dateTime);
            break;
        case MOBILITY:
            newState = new MobilityState(registration, person, dateTime);
            break;
        case TRANSITION:
            newState = new TransitionalState(registration, person, dateTime);
            break;
        case TRANSITED:
            newState = new TransitedState(registration, person, dateTime);
            break;
        case INACTIVE:
            newState = new InactiveState(registration, person, dateTime);
            break;
        }

        return newState;
    }

    protected void init(Registration registration, Person responsiblePerson, DateTime stateDate) {
        setStateDate(stateDate != null ? stateDate : new DateTime());
        setRegistration(registration);
        setResponsiblePerson(responsiblePerson != null ? responsiblePerson : AccessControl.getPerson());
    }

    protected void init(Registration registration) {
        init(registration, null, null);
    }

    @Override
    final public IState nextState() {
        return nextState(new StateBean(defaultNextStateType().toString()));
    }

    protected RegistrationStateType defaultNextStateType() {
        throw new DomainException("error.no.default.nextState.defined");
    }

    @Override
    public IState nextState(final StateBean bean) {
        return createState(getRegistration(), bean.getResponsible(), bean.getStateDateTime(),
                RegistrationStateType.valueOf(bean.getNextState()));
    }

    @Override
    final public void checkConditionsToForward() {
        checkConditionsToForward(new RegistrationStateBean(defaultNextStateType()));
    }

    @Override
    public void checkConditionsToForward(final StateBean bean) {
        checkCurriculumLinesForStateDate(bean);
    }

    private void checkCurriculumLinesForStateDate(final StateBean bean) {
        final ExecutionYear year = ((RegistrationStateBean) bean).getExecutionInterval().getExecutionYear();
        final RegistrationStateType nextStateType = RegistrationStateType.valueOf(bean.getNextState());

        if (nextStateType.canHaveCurriculumLinesOnCreation()) {
            return;
        }

        if (getRegistration().hasAnyEnroledEnrolments(year)) {
            throw new DomainException("RegisteredState.error.registration.has.enroled.enrolments.for.execution.year",
                    year.getName());
        }
    }

    @Override
    public Set<String> getValidNextStates() {
        return Collections.emptySet();
    }

    public abstract RegistrationStateType getStateType();

    public ExecutionYear getExecutionYear() {
        return getExecutionInterval().getExecutionYear();
    }

    public void delete() {
        RegistrationState nextState = getNext();
        RegistrationState previousState = getPrevious();
        if (nextState != null && previousState != null
                && !previousState.getValidNextStates().contains(nextState.getStateType().name())) {
            throw new DomainException("error.cannot.delete.registrationState.incoherentState: "
                    + previousState.getStateType().name() + " -> " + nextState.getStateType().name());
        }
        deleteWithoutCheckRules();
    }

    public void deleteWithoutCheckRules() {
        final Registration registration = getRegistration();
        try {
            String responsablePersonName;
            if (getResponsiblePerson() != null) {
                responsablePersonName = getResponsiblePerson().getPresentationName();
            } else {
                responsablePersonName = "-";
            }

            org.fenixedu.academic.domain.student.RegistrationStateLog.createRegistrationStateLog(getRegistration(),
                    Bundle.MESSAGING, "log.registration.registrationstate.removed", getStateType().getDescription(),
                    getRemarks());
            setExecutionInterval(null);
            setRegistration(null);
            setResponsiblePerson(null);
            setRootDomainObject(null);
            super.deleteDomainObject();
        } finally {
            registration.getStudent().updateStudentRole();
        }
    }

    public RegistrationState getNext() {
        final List<RegistrationState> sortedStates = getRegistration().getRegistrationStatesSet().stream()
                .sorted(EXECUTION_INTERVAL_AND_DATE_COMPARATOR).collect(Collectors.toList());
        int indexOfThis = sortedStates.indexOf(this);
        return sortedStates.size() > indexOfThis + 1 ? sortedStates.get(indexOfThis + 1) : null;
    }

    public RegistrationState getPrevious() {
        final List<RegistrationState> sortedStates = getRegistration().getRegistrationStatesSet().stream()
                .sorted(EXECUTION_INTERVAL_AND_DATE_COMPARATOR).collect(Collectors.toList());
        int indexOfThis = sortedStates.indexOf(this);
        return indexOfThis > 0 ? sortedStates.get(indexOfThis - 1) : null;
    }

    public DateTime getEndDate() {
        RegistrationState state = getNext();
        return (state != null) ? state.getStateDate() : null;
    }

    public void setStateDate(YearMonthDay yearMonthDay) {
        super.setStateDate(yearMonthDay.toDateTimeAtMidnight());
    }

    public static RegistrationState createRegistrationState(Registration registration, Person responsible, DateTime creation,
            RegistrationStateType stateType, ExecutionInterval executionInterval) {
        RegistrationStateBean bean = new RegistrationStateBean(registration);
        bean.setResponsible(responsible);
        bean.setStateDateTime(creation);
        bean.setStateType(stateType);
        bean.setExecutionInterval(executionInterval);
        return createRegistrationState(bean);
    }

    public static RegistrationState createRegistrationState(RegistrationStateBean bean) {
        RegistrationState createdState = null;

        final Registration registration = bean.getRegistration();
        final RegistrationState previousState = registration.getStateInDate(bean.getStateDateTime());
        if (previousState == null) {
            createdState = RegistrationState.createState(registration, null, bean.getStateDateTime(), bean.getStateType());
        } else {
            createdState = (RegistrationState) StateMachine.execute(previousState, bean);
        }
        createdState.setRemarks(bean.getRemarks());
        createdState.setExecutionInterval(bean.getExecutionInterval());
        registration.getStudent().updateStudentRole();

        final RegistrationState nextState = createdState.getNext();
        if (nextState != null && !createdState.getValidNextStates().contains(nextState.getStateType().name())) {
            throw new DomainException("error.cannot.add.registrationState.incoherentState");
        }
        org.fenixedu.academic.domain.student.RegistrationStateLog.createRegistrationStateLog(registration, Bundle.MESSAGING,
                "log.registration.registrationstate.added", bean.getStateType().getDescription(), bean.getRemarks());
        return createdState;
    }

    public boolean isActive() {
        return getStateType().isActive();
    }

}
