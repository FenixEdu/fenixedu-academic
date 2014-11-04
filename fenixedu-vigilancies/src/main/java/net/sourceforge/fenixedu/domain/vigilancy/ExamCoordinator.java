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
package org.fenixedu.academic.domain.vigilancy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.WrittenEvaluation;
import org.fenixedu.academic.domain.organizationalStructure.Unit;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class ExamCoordinator extends ExamCoordinator_Base {

    public ExamCoordinator() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    private ExamCoordinator(Person person) {
        this();
        this.setPerson(person);
    }

    public ExamCoordinator(Person person, ExecutionYear executionYear, Unit unit) {
        this(person);
        this.setExecutionYear(executionYear);
        this.setUnit(unit);
        this.setAllowedToCreateGroups(true);
    }

    public String getEmail() {
        return this.getPerson().getEmail();
    }

    public List<Vigilancy> getConvokesThatCanManage() {
        Collection<VigilantGroup> groups = this.getVigilantGroupsSet();
        Set<Vigilancy> convokes = new HashSet<Vigilancy>();

        for (VigilantGroup group : groups) {
            convokes.addAll(group.getVigilancies());
        }

        return new ArrayList<Vigilancy>(convokes);
    }

    public List<UnavailablePeriod> getUnavailablePeriodsThatCanManage() {
        Set<UnavailablePeriod> unavailablePeriods = new HashSet<UnavailablePeriod>();
        for (VigilantGroup group : this.getVigilantGroupsSet()) {
            unavailablePeriods.addAll(group.getUnavailablePeriodsOfVigilantsInGroup());
        }
        return new ArrayList<UnavailablePeriod>(unavailablePeriods);
    }

    public List<VigilantWrapper> getVigilantsThatCanManage() {
        List<VigilantWrapper> vigilants = new ArrayList<VigilantWrapper>();
        Collection<VigilantGroup> groups = this.getVigilantGroupsSet();

        for (VigilantGroup group : groups) {
            vigilants.addAll(group.getVigilantWrappersSet());
        }

        return new ArrayList<VigilantWrapper>(vigilants);
    }

    public Boolean isAllowedToCreateGroups() {
        return this.getAllowedToCreateGroups();
    }

    public boolean managesGivenVigilantGroup(VigilantGroup group) {
        Collection<VigilantGroup> groups = this.getVigilantGroupsSet();
        return groups.contains(group);
    }

    public List<ExecutionCourse> getAssociatedExecutionCourses() {
        List<ExecutionCourse> courses = new ArrayList<ExecutionCourse>();
        for (VigilantGroup group : this.getVigilantGroupsSet()) {
            courses.addAll(group.getExecutionCoursesSet());
        }
        return courses;
    }

    public List<WrittenEvaluation> getAssociatedWrittenEvaluations() {
        Collection<VigilantGroup> groups = this.getVigilantGroupsSet();
        Set<WrittenEvaluation> evaluations = new HashSet<WrittenEvaluation>();
        for (VigilantGroup group : groups) {
            evaluations.addAll(group.getAllAssociatedWrittenEvaluations());
        }
        return new ArrayList<WrittenEvaluation>(evaluations);
    }

    public List<WrittenEvaluation> getAssociatedWrittenEvaluationsAfterDate(DateTime date) {
        Collection<VigilantGroup> groups = this.getVigilantGroupsSet();
        Set<WrittenEvaluation> evaluations = new HashSet<WrittenEvaluation>();
        for (VigilantGroup group : groups) {
            evaluations.addAll(group.getWrittenEvaluationsAfterDate(date));
        }
        return new ArrayList<WrittenEvaluation>(evaluations);
    }

    public List<WrittenEvaluation> getAssociatedWrittenEvaluationsBeforeDate(DateTime date) {
        Collection<VigilantGroup> groups = this.getVigilantGroupsSet();
        Set<WrittenEvaluation> evaluations = new HashSet<WrittenEvaluation>();
        for (VigilantGroup group : groups) {
            evaluations.addAll(group.getWrittenEvaluationsBeforeDate(date));
        }
        return new ArrayList<WrittenEvaluation>(evaluations);
    }

    public void delete() {
        setUnit(null);
        setRootDomainObject(null);
        setPerson(null);
        setExecutionYear(null);
        super.deleteDomainObject();
    }

    public static List<ExamCoordinator> getExamCoordinatorsForGivenYear(Unit unit, ExecutionYear executionYear) {
        List<ExamCoordinator> examCoordinators = new ArrayList<ExamCoordinator>();
        for (ExamCoordinator coordinator : unit.getExamCoordinatorsSet()) {
            if (coordinator.getExecutionYear().equals(executionYear)) {
                examCoordinators.add(coordinator);
            }
        }
        return examCoordinators;
    }

    public static ExamCoordinator getExamCoordinatorForGivenExecutionYear(Person person, ExecutionYear executionYear) {
        final Collection<ExamCoordinator> examCoordinators = person.getExamCoordinatorsSet();
        for (final ExamCoordinator examCoordinator : examCoordinators) {
            if (examCoordinator.getExecutionYear().equals(executionYear)) {
                return examCoordinator;
            }
        }
        return null;
    }

    public static ExamCoordinator getCurrentExamCoordinator(Person person) {
        return getExamCoordinatorForGivenExecutionYear(person, ExecutionYear.readCurrentExecutionYear());
    }

}
