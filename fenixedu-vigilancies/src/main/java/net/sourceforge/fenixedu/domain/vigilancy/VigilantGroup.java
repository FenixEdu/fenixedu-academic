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

import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.Employee;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.WrittenEvaluation;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.vigilancy.strategies.Strategy;
import org.fenixedu.academic.domain.vigilancy.strategies.StrategyFactory;
import org.fenixedu.academic.domain.vigilancy.strategies.StrategySugestion;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class VigilantGroup extends VigilantGroup_Base {

    public VigilantGroup() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public double getPointsAverage() {
        double sumOfPoints = 0;
        for (VigilantWrapper v : this.getVigilantWrappersThatCanBeConvoked()) {
            sumOfPoints += v.getPoints();
        }
        return ((this.getVigilantWrappersThatCanBeConvoked().isEmpty()) ? 0 : sumOfPoints
                / this.getVigilantWrappersThatCanBeConvoked().size());
    }

    public VigilantWrapper hasPerson(Person person) {
        for (VigilantWrapper vigilantWrapper : getVigilantWrappersSet()) {
            if (vigilantWrapper.getPerson().equals(person)) {
                return vigilantWrapper;
            }
        }
        return null;
    }

    public StrategySugestion sugestVigilantsToConvoke(WrittenEvaluation writtenEvaluation) {

        String strategyName = this.getConvokeStrategy();
        StrategyFactory factory = StrategyFactory.getInstance();
        Strategy strategy = factory.getStrategy(strategyName);
        List<VigilantWrapper> possibleVigilants = new ArrayList<VigilantWrapper>(this.getVigilantWrappersThatCanBeConvoked());
        possibleVigilants.addAll(findTeachersThatAreInGroupFor(writtenEvaluation.getAssociatedExecutionCoursesSet()));
        return (strategy != null) ? strategy.sugest(possibleVigilants, writtenEvaluation) : null;

    }

    private List<VigilantWrapper> findTeachersThatAreInGroupFor(Collection<ExecutionCourse> executionCourses) {
        List<VigilantWrapper> teachers = new ArrayList<VigilantWrapper>();
        for (VigilantWrapper vigilantWrapper : this.getVigilantWrappersThatCantBeConvoked()) {
            Teacher teacher = vigilantWrapper.getTeacher();
            if (teacher != null && teacher.teachesAny(executionCourses, this.getExecutionYear())) {
                teachers.add(vigilantWrapper);
            }
        }
        return teachers;
    }

    public void convokeVigilants(List<VigilantWrapper> vigilants, WrittenEvaluation writtenEvaluation) {

        for (VigilantWrapper vigilant : vigilants) {
            if (!vigilant.hasBeenConvokedForEvaluation(writtenEvaluation)) {
                Teacher teacher = vigilant.getTeacher();
                if (teacher != null && teacher.teachesAny(writtenEvaluation.getAssociatedExecutionCoursesSet())) {
                    vigilant.addVigilancies(new OwnCourseVigilancy(writtenEvaluation));
                } else {
                    vigilant.addVigilancies(new OtherCourseVigilancy(writtenEvaluation));
                }
            } else {
                Vigilancy vigilancy = vigilant.getVigilancyFor(writtenEvaluation);
                if (!vigilancy.isActive()) {
                    vigilancy.setActive(true);
                }
            }
        }
    }

    public Collection<VigilantWrapper> getVigilants() {
        return this.getVigilantWrappersSet();
    }

    public int getVigilantsCount() {
        return this.getVigilants().size();
    }

    public List<VigilantWrapper> getVigilantWrappersThatCanBeConvoked() {
        ArrayList<VigilantWrapper> vigilantWrappersThatCanBeConvoked = new ArrayList<VigilantWrapper>();
        for (VigilantWrapper vigilantWrapper : this.getVigilantWrappersSet()) {
            if (vigilantWrapper.getConvokable()) {
                vigilantWrappersThatCanBeConvoked.add(vigilantWrapper);
            }
        }
        return vigilantWrappersThatCanBeConvoked;
    }

    public List<VigilantWrapper> getVigilantWrappersThatCantBeConvoked() {
        ArrayList<VigilantWrapper> vigilantWrappersThatCantBeConvoked = new ArrayList<VigilantWrapper>();
        for (VigilantWrapper vigilantWrapper : this.getVigilantWrappersSet()) {
            if (!vigilantWrapper.getConvokable()) {
                vigilantWrappersThatCantBeConvoked.add(vigilantWrapper);
            }
        }
        return vigilantWrappersThatCantBeConvoked;
    }

    public List<Person> getPersons() {
        List<Person> persons = new ArrayList<Person>();
        Collection<VigilantWrapper> vigilantWrappers = this.getVigilantWrappersSet();
        for (VigilantWrapper vigilantWrapper : vigilantWrappers) {
            persons.add(vigilantWrapper.getPerson());
        }

        return persons;
    }

    public List<Vigilancy> getVigilancies() {
        List<Vigilancy> vigilancies = new ArrayList<Vigilancy>();
        for (ExecutionCourse course : this.getExecutionCoursesSet()) {
            for (WrittenEvaluation evaluation : course.getWrittenEvaluations()) {
                vigilancies.addAll(evaluation.getVigilanciesSet());
            }
        }
        return vigilancies;
    }

    public List<Vigilancy> getConvokesAfterDate(DateTime date) {
        List<Vigilancy> convokesToReturn = new ArrayList<Vigilancy>();
        for (Vigilancy convoke : this.getVigilancies()) {
            if (convoke.getBeginDateTime().isAfter(date) || convoke.getBeginDateTime().isEqual(date)) {
                convokesToReturn.add(convoke);
            }
        }
        return convokesToReturn;
    }

    public List<Vigilancy> getConvokesBeforeDate(DateTime date) {
        List<Vigilancy> convokesToReturn = new ArrayList<Vigilancy>();
        for (Vigilancy convoke : this.getVigilancies()) {
            if (convoke.getBeginDateTime().isBefore(date)) {
                convokesToReturn.add(convoke);
            }
        }
        return convokesToReturn;
    }

    public Set<Vigilancy> getVigilancies(VigilantWrapper vigilantWrapper) {
        Set<Vigilancy> vigilantConvokes = new HashSet<Vigilancy>();
        for (Vigilancy convoke : this.getVigilancies()) {
            if (vigilantWrapper == convoke.getVigilantWrapper()) {
                vigilantConvokes.add(convoke);
            }
        }
        return vigilantConvokes;
    }

    public List<UnavailablePeriod> getUnavailablePeriodsOfVigilantsInGroup() {
        List<UnavailablePeriod> unavailablePeriods = new ArrayList<UnavailablePeriod>();
        for (VigilantWrapper vigilantWrapper : this.getVigilantWrappersSet()) {
            unavailablePeriods.addAll(vigilantWrapper.getUnavailablePeriods());
        }
        return unavailablePeriods;
    }

    public boolean canSpecifyUnavailablePeriodIn(DateTime date) {
        return (date.isAfter(this.getBeginOfFirstPeriodForUnavailablePeriods())
                && date.isBefore(this.getEndOfFirstPeriodForUnavailablePeriods()) || (date.isAfter(this
                .getBeginOfSecondPeriodForUnavailablePeriods()) && date
                .isBefore(this.getEndOfSecondPeriodForUnavailablePeriods())));

    }

    public List<VigilantWrapper> getVigilantWrappersWithIncompatiblePerson() {
        List<VigilantWrapper> vigilantWrappers = new ArrayList<VigilantWrapper>();
        for (VigilantWrapper vigilantWrapper : this.getVigilantWrappersSet()) {
            Person incompatiblePerson = Vigilancy.getIncompatibleVigilantPerson(vigilantWrapper.getPerson());
            if (incompatiblePerson != null && !vigilantWrappers.contains(incompatiblePerson.getVigilantWrappersSet())) {
                vigilantWrappers.add(vigilantWrapper);
            }
        }
        return vigilantWrappers;
    }

    @Override
    public void addExecutionCourses(ExecutionCourse course) {
        if (course.getVigilantGroup() != null) {
            throw new DomainException("vigilancy.error.executionCourseAlreadyInAGroup");
        } else {
            super.addExecutionCourses(course);
        }
    }

    public void delete() {
        setExecutionYear(null);
        getExecutionCoursesSet().clear();
        getExamCoordinatorsSet().clear();
        setUnit(null);
        setRootDomainObject(null);
        for (VigilantWrapper vigilant : this.getVigilantWrappersSet()) {
            Collection<Vigilancy> vigilancies = vigilant.getVigilanciesSet();
            for (Vigilancy vigilancy : vigilancies) {
                if (vigilancy.isActive()) {
                    throw new DomainException("label.vigilancy.error.cannotDeleteGroupWithVigilants");
                }
            }
            removeVigilantWrappers(vigilant);
        }
        super.deleteDomainObject();
    }

    public List<WrittenEvaluation> getAllAssociatedWrittenEvaluations() {
        Collection<ExecutionCourse> courses = this.getExecutionCoursesSet();
        Set<WrittenEvaluation> evaluations = new HashSet<WrittenEvaluation>();
        for (ExecutionCourse course : courses) {
            evaluations.addAll(course.getWrittenEvaluations());
        }
        return new ArrayList<WrittenEvaluation>(evaluations);
    }

    public List<WrittenEvaluation> getWrittenEvaluationsAfterDate(DateTime date) {
        List<Vigilancy> convokes = this.getConvokesAfterDate(date);
        return getEvaluationsFromConvokes(convokes);
    }

    public List<WrittenEvaluation> getWrittenEvaluationsBeforeDate(DateTime date) {
        List<Vigilancy> convokes = this.getConvokesBeforeDate(date);
        return getEvaluationsFromConvokes(convokes);
    }

    private List<WrittenEvaluation> getEvaluationsFromConvokes(List<Vigilancy> convokes) {
        Set<WrittenEvaluation> evaluations = new HashSet<WrittenEvaluation>();

        for (Vigilancy convoke : convokes) {
            evaluations.add(convoke.getWrittenEvaluation());
        }
        return new ArrayList<WrittenEvaluation>(evaluations);
    }

    @Atomic
    public void copyPointsFromVigilantGroup(VigilantGroup previousGroup) {
        this.setPointsForTeacher(previousGroup.getPointsForTeacher());
        this.setPointsForConvoked(previousGroup.getPointsForConvoked());
        this.setPointsForDisconvoked(previousGroup.getPointsForDisconvoked());
        this.setPointsForDismissed(previousGroup.getPointsForDismissed());
        this.setPointsForDismissedTeacher(previousGroup.getPointsForDismissedTeacher());
        this.setPointsForMissing(previousGroup.getPointsForMissing());
        this.setPointsForMissingTeacher(previousGroup.getPointsForMissingTeacher());
    }

    public VigilantWrapper getVigilantWrapperFor(Person person) {
        for (VigilantWrapper wrapper : getVigilantWrappersSet()) {
            if (wrapper.getPerson() == person) {
                return wrapper;
            }
        }
        return null;
    }

    public static List<VigilantGroup> getVigilantGroupsForGivenExecutionYear(Unit unit, ExecutionYear executionYear) {
        List<VigilantGroup> groups = new ArrayList<VigilantGroup>();
        for (Unit subUnit : unit.getSubUnits()) {
            groups.addAll(getVigilantGroupsForGivenExecutionYear(subUnit, executionYear));
        }
        for (VigilantGroup group : unit.getVigilantGroupsSet()) {
            if (group.getExecutionYear().equals(executionYear)) {
                groups.add(group);
            }
        }
        return groups;
    }

    public static List<VigilantGroup> getVisibleVigilantGroups(Person person, ExecutionYear executionYear) {
        final Set<VigilantGroup> groups = new HashSet<VigilantGroup>();

        final Employee employee = person.getEmployee();
        if (employee != null) {
            final Department department =
                    employee.getLastDepartmentWorkingPlace(executionYear.getBeginDateYearMonthDay(),
                            executionYear.getEndDateYearMonthDay());
            if (department != null) {
                groups.addAll(getVigilantGroupsForGivenExecutionYear(department.getDepartmentUnit(), executionYear));
            }
        } else {
            for (final VigilantWrapper vigilantWrapper : VigilantWrapper
                    .getVigilantWrapperForExecutionYear(person, executionYear)) {
                groups.add(vigilantWrapper.getVigilantGroup());
            }
        }

        return new ArrayList<VigilantGroup>(groups);
    }

    public static List<VigilantGroup> getVigilantGroupsForExecutionYear(Person person, ExecutionYear executionYear) {
        final List<VigilantGroup> groups = new ArrayList<VigilantGroup>();
        for (final VigilantWrapper wrapper : person.getVigilantWrappersSet()) {
            final VigilantGroup group = wrapper.getVigilantGroup();
            if (group.getExecutionYear().equals(executionYear)) {
                groups.add(group);
            }
        }
        return groups;
    }

    public static Set<VigilantGroup> getAssociatedVigilantGroups(WrittenEvaluation writtenEvaluation) {
        Set<VigilantGroup> groups = new HashSet<VigilantGroup>();
        for (ExecutionCourse course : writtenEvaluation.getAssociatedExecutionCoursesSet()) {
            if (course.getVigilantGroup() != null) {
                groups.add(course.getVigilantGroup());
            }
        }
        return groups;
    }

}
