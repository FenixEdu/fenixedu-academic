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
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.InquiryCoordinatorAnswer;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class Coordinator extends Coordinator_Base {

    static {
        Person.getRelationCoordinatorTeacher().addListener(new RelationAdapter<Person, Coordinator>() {

            @Override
            public void afterAdd(Person o1, Coordinator o2) {
                if (o1 != null && o2 != null) {
                    if (!o1.hasRole(RoleType.COORDINATOR)) {
                        o1.addPersonRoleByRoleType(RoleType.COORDINATOR);
                    }
                }
            }

            @Override
            public void afterRemove(Person o1, Coordinator o2) {
                if (o1 != null && o2 != null) {
                    if (o1.getCoordinatorsSet().size() == 0 && !o1.hasAnyScientificCommissions()) {
                        o1.removeRoleByType(RoleType.COORDINATOR);
                    }
                }
            }
        });
    }

    private Coordinator() {
        super();
        setRootDomainObject(Bennu.getInstance());

    }

    private Coordinator(final ExecutionDegree executionDegree, final Person person, final Boolean responsible) {
        this();

        for (final Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
            if (coordinator.getPerson() == person) {
                throw new Error("error.person.already.is.coordinator.for.same.execution.degree");
            }
        }

        setExecutionDegree(executionDegree);
        person.addPersonRoleByRoleType(RoleType.COORDINATOR);
        setPerson(person);
        setResponsible(responsible);
        // CoordinatorLog.createCoordinatorLog(new DateTime(),
        // OperationType.ADD, this);
    }

    public void delete() throws DomainException {

        checkRulesToDelete();
        setExecutionDegree(null);
        setPerson(null);
        getExecutionDegreeCoursesReports().clear();
        getStudentInquiriesCourseResults().clear();

        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    private void checkRulesToDelete() {
        if (hasAnyExecutionDegreeCoursesReports()) {
            for (CoordinatorExecutionDegreeCoursesReport report : getExecutionDegreeCoursesReports()) {
                if (!report.isEmpty()) {
                    throw new DomainException("error.Coordinator.cannot.delete.because.already.has.written.comments");
                }
            }
        }

        if (hasAnyStudentInquiriesCourseResults()) {
            throw new DomainException("error.Coordinator.cannot.delete.because.already.has.written.comments");
        }
    }

    public boolean isResponsible() {
        return getResponsible().booleanValue();
    }

    public Teacher getTeacher() {
        return getPerson().getTeacher();
    }

    @Atomic
    public static Coordinator createCoordinator(ExecutionDegree executionDegree, Person person, Boolean responsible) {

        CoordinationTeamLog.createLog(executionDegree.getDegree(), executionDegree.getExecutionYear(),
                Bundle.MESSAGING, "log.degree.coordinationteam.addmember", person.getPresentationName(),
                executionDegree.getPresentationName());

        return new Coordinator(executionDegree, person, responsible);
    }

    @Atomic
    public void removeCoordinator() {
        this.delete();
    }

    /**
     * Method to create coordinator logs for adding responsibility
     * 
     * @param personAddingResponsible
     */
    public void setAsResponsible(Person personAddingResponsible) {

    }

    @Atomic
    public void setAsResponsible() {
        this.setResponsible(Boolean.valueOf(true));
    }

    @Atomic
    public void setAsNotResponsible() {
        this.setResponsible(Boolean.valueOf(false));
    }

    /**
     * Method to apply a certain operation on coordinator
     * 
     * @param operationType
     * @param personMakingAction
     * @param arguments
     */
    public void makeAction(OperationType operationType, Person personMakingAction) {
        if (operationType.compareTo(OperationType.CHANGERESPONSIBLE_FALSE) == 0) {
            CoordinatorLog.createCoordinatorLog(new DateTime(), OperationType.CHANGERESPONSIBLE_FALSE, personMakingAction, this);
            setAsNotResponsible();
        } else if (operationType.compareTo(OperationType.CHANGERESPONSIBLE_TRUE) == 0) {
            CoordinatorLog.createCoordinatorLog(new DateTime(), OperationType.CHANGERESPONSIBLE_TRUE, personMakingAction, this);
            this.setAsResponsible();
        } else if (operationType.compareTo(OperationType.REMOVE) == 0) {
            checkRulesToDelete();
            CoordinatorLog.createCoordinatorLog(new DateTime(), OperationType.REMOVE, personMakingAction, this);
            this.removeCoordinator();
        }
    }

    public static Coordinator makeCreation(Person personMakingAction, ExecutionDegree executionDegree, Person person,
            Boolean responsible) {
        Coordinator coordinator = createCoordinator(executionDegree, person, responsible);
        CoordinatorLog.createCoordinatorLog(new DateTime(), OperationType.ADD, personMakingAction, coordinator);
        return coordinator;
    }

    public InquiryCoordinatorAnswer getInquiryCoordinatorAnswer(ExecutionSemester executionSemester) {
        for (InquiryCoordinatorAnswer inquiryCoordinatorAnswer : getInquiryCoordinatorAnswers()) {
            if (inquiryCoordinatorAnswer.getExecutionSemester() == executionSemester) {
                return inquiryCoordinatorAnswer;
            }
        }
        return null;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CoordinatorExecutionDegreeCoursesReport> getExecutionDegreeCoursesReports() {
        return getExecutionDegreeCoursesReportsSet();
    }

    @Deprecated
    public boolean hasAnyExecutionDegreeCoursesReports() {
        return !getExecutionDegreeCoursesReportsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesCourseResult> getStudentInquiriesCourseResults() {
        return getStudentInquiriesCourseResultsSet();
    }

    @Deprecated
    public boolean hasAnyStudentInquiriesCourseResults() {
        return !getStudentInquiriesCourseResultsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryCoordinatorAnswer> getInquiryCoordinatorAnswers() {
        return getInquiryCoordinatorAnswersSet();
    }

    @Deprecated
    public boolean hasAnyInquiryCoordinatorAnswers() {
        return !getInquiryCoordinatorAnswersSet().isEmpty();
    }

    @Deprecated
    public boolean hasResponsible() {
        return getResponsible() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasExecutionDegree() {
        return getExecutionDegree() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
