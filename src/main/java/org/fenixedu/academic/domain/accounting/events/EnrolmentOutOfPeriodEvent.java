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
package org.fenixedu.academic.domain.accounting.events;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accounting.Account;
import org.fenixedu.academic.domain.accounting.AccountType;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.PostingRule;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class EnrolmentOutOfPeriodEvent extends EnrolmentOutOfPeriodEvent_Base {

    static {
        StudentCurricularPlan.getRelationEnrolmentOutOfPeriodEventStudentCurricularPlan().addListener(
                new RelationAdapter<StudentCurricularPlan, EnrolmentOutOfPeriodEvent>() {
                    @Override
                    public void beforeAdd(StudentCurricularPlan studentCurricularPlan,
                            EnrolmentOutOfPeriodEvent enrolmentOutOfPeriodEvent) {
                        if (studentCurricularPlan != null && enrolmentOutOfPeriodEvent != null) {
                            final Registration registration = studentCurricularPlan.getRegistration();
                            if (registration.containsEnrolmentOutOfPeriodEventFor(enrolmentOutOfPeriodEvent.getExecutionPeriod())) {
                                throw new DomainException(
                                        "error.accounting.events.EnrolmentOutOfPeriodEvent.registration.already.contains.enrolment.out.of.period.event.for.period",
                                        studentCurricularPlan.getRegistration().getStudent().getNumber().toString(),
                                        studentCurricularPlan.getRegistration().getDegree().getPresentationName(),
                                        enrolmentOutOfPeriodEvent.getExecutionPeriod().getExecutionYear().getYear(),
                                        enrolmentOutOfPeriodEvent.getExecutionPeriod().getSemester().toString());
                            }

                        }
                    }

                });

    }

    protected EnrolmentOutOfPeriodEvent() {
        super();
    }

    public EnrolmentOutOfPeriodEvent(final AdministrativeOffice administrativeOffice, final Person person,
            final StudentCurricularPlan studentCurricularPlan, final ExecutionSemester executionSemester,
            final Integer numberOfDelayDays) {
        this();
        init(administrativeOffice, person, studentCurricularPlan, executionSemester, numberOfDelayDays);
    }

    private void init(AdministrativeOffice administrativeOffice, Person person, StudentCurricularPlan studentCurricularPlan,
            ExecutionSemester executionSemester, Integer numberOfDelayDays) {
        checkParameters(administrativeOffice, studentCurricularPlan, executionSemester, numberOfDelayDays);
        super.init(administrativeOffice, EventType.ENROLMENT_OUT_OF_PERIOD, person);
        super.setExecutionPeriod(executionSemester);
        super.setStudentCurricularPlan(studentCurricularPlan);
        super.setNumberOfDelayDays(numberOfDelayDays);
    }

    private void checkParameters(AdministrativeOffice administrativeOffice, StudentCurricularPlan studentCurricularPlan,
            ExecutionSemester executionSemester, Integer numberOfDelayDays) {

        if (administrativeOffice == null) {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.accounting.events.EnrolmentOutOfPeriodEvent.administrativeOffice.cannot.be.null");
        }

        if (studentCurricularPlan == null) {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.accounting.events.EnrolmentOutOfPeriodEvent.studentCurricularPlan.cannot.be.null");
        }

        if (executionSemester == null) {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.accounting.events.EnrolmentOutOfPeriodEvent.executionPeriod.cannot.be.null");
        }

        if (numberOfDelayDays == null) {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.accounting.events.EnrolmentOutOfPeriodEvent.numberOfDelayDays.cannot.be.null");
        }

    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        final LabelFormatter labelFormatter = new LabelFormatter().appendLabel(entryType.name(), Bundle.ENUMERATION);
        addCommonDescription(labelFormatter);

        return labelFormatter;
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = super.getDescription();
        addCommonDescription(labelFormatter);

        return labelFormatter;
    }

    private void addCommonDescription(final LabelFormatter labelFormatter) {
        labelFormatter.appendLabel(" (");
        labelFormatter.appendLabel(getDegree().getDegreeType().getName().getContent());
        labelFormatter.appendLabel(" - ");
        labelFormatter.appendLabel(getDegree().getNameFor(getExecutionPeriod().getExecutionYear()).getContent());
        labelFormatter.appendLabel(" - ");
        labelFormatter.appendLabel(getExecutionPeriod().getSemester().toString());
        labelFormatter.appendLabel("label.semester", Bundle.APPLICATION);
        labelFormatter.appendLabel("  " + getExecutionPeriod().getYear());
        labelFormatter.appendLabel(")");
    }

    private Degree getDegree() {
        return getStudentCurricularPlan().getDegree();
    }

    @Override
    protected Account getFromAccount() {
        return getPerson().getAccountBy(AccountType.EXTERNAL);
    }

    @Override
    public PostingRule getPostingRule() {
        return getAdministrativeOffice().getServiceAgreementTemplate().findPostingRuleByEventTypeAndDate(getEventType(),
                getWhenOccured());
    }

    @Override
    public Account getToAccount() {
        return getAdministrativeOffice().getUnit().getAccountBy(AccountType.INTERNAL);
    }

    @Override
    public boolean isEnrolmentOutOfPeriod() {
        return true;
    }

}
