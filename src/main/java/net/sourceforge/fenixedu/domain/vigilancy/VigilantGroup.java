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
package net.sourceforge.fenixedu.domain.vigilancy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.vigilancy.strategies.Strategy;
import net.sourceforge.fenixedu.domain.vigilancy.strategies.StrategyFactory;
import net.sourceforge.fenixedu.domain.vigilancy.strategies.StrategySugestion;

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
        for (VigilantWrapper vigilantWrapper : getVigilantWrappers()) {
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
        possibleVigilants.addAll(findTeachersThatAreInGroupFor(writtenEvaluation.getAssociatedExecutionCourses()));
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
        for (VigilantWrapper vigilantWrapper : this.getVigilantWrappers()) {
            if (vigilantWrapper.getConvokable()) {
                vigilantWrappersThatCanBeConvoked.add(vigilantWrapper);
            }
        }
        return vigilantWrappersThatCanBeConvoked;
    }

    public List<VigilantWrapper> getVigilantWrappersThatCantBeConvoked() {
        ArrayList<VigilantWrapper> vigilantWrappersThatCantBeConvoked = new ArrayList<VigilantWrapper>();
        for (VigilantWrapper vigilantWrapper : this.getVigilantWrappers()) {
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
        for (ExecutionCourse course : this.getExecutionCourses()) {
            for (WrittenEvaluation evaluation : course.getWrittenEvaluations()) {
                vigilancies.addAll(evaluation.getVigilancies());
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
        for (VigilantWrapper vigilantWrapper : this.getVigilantWrappers()) {
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
        for (VigilantWrapper vigilantWrapper : this.getVigilantWrappers()) {
            Person incompatiblePerson = vigilantWrapper.getPerson().getIncompatibleVigilantPerson();
            if (incompatiblePerson != null && !vigilantWrappers.contains(incompatiblePerson.getVigilantWrappers())) {
                vigilantWrappers.add(vigilantWrapper);
            }
        }
        return vigilantWrappers;
    }

    @Override
    public void addExecutionCourses(ExecutionCourse course) {
        if (course.hasVigilantGroup()) {
            throw new DomainException("vigilancy.error.executionCourseAlreadyInAGroup");
        } else {
            super.addExecutionCourses(course);
        }
    }

    public void delete() {
        setExecutionYear(null);
        getExecutionCourses().clear();
        getExamCoordinators().clear();
        setUnit(null);
        setRootDomainObject(null);
        for (VigilantWrapper vigilant : this.getVigilantWrappers()) {
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
        Collection<ExecutionCourse> courses = this.getExecutionCourses();
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
        for (VigilantWrapper wrapper : getVigilantWrappers()) {
            if (wrapper.getPerson() == person) {
                return wrapper;
            }
        }
        return null;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator> getExamCoordinators() {
        return getExamCoordinatorsSet();
    }

    @Deprecated
    public boolean hasAnyExamCoordinators() {
        return !getExamCoordinatorsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper> getVigilantWrappers() {
        return getVigilantWrappersSet();
    }

    @Deprecated
    public boolean hasAnyVigilantWrappers() {
        return !getVigilantWrappersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ExecutionCourse> getExecutionCourses() {
        return getExecutionCoursesSet();
    }

    @Deprecated
    public boolean hasAnyExecutionCourses() {
        return !getExecutionCoursesSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasPointsForTeacher() {
        return getPointsForTeacher() != null;
    }

    @Deprecated
    public boolean hasPointsForDismissed() {
        return getPointsForDismissed() != null;
    }

    @Deprecated
    public boolean hasContactEmail() {
        return getContactEmail() != null;
    }

    @Deprecated
    public boolean hasEndOfSecondPeriodForUnavailablePeriods() {
        return getEndOfSecondPeriodForUnavailablePeriods() != null;
    }

    @Deprecated
    public boolean hasConvokeStrategy() {
        return getConvokeStrategy() != null;
    }

    @Deprecated
    public boolean hasUnit() {
        return getUnit() != null;
    }

    @Deprecated
    public boolean hasPointsForDismissedTeacher() {
        return getPointsForDismissedTeacher() != null;
    }

    @Deprecated
    public boolean hasEndOfFirstPeriodForUnavailablePeriods() {
        return getEndOfFirstPeriodForUnavailablePeriods() != null;
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasPointsForMissing() {
        return getPointsForMissing() != null;
    }

    @Deprecated
    public boolean hasEmailSubjectPrefix() {
        return getEmailSubjectPrefix() != null;
    }

    @Deprecated
    public boolean hasBeginOfSecondPeriodForUnavailablePeriods() {
        return getBeginOfSecondPeriodForUnavailablePeriods() != null;
    }

    @Deprecated
    public boolean hasPointsForMissingTeacher() {
        return getPointsForMissingTeacher() != null;
    }

    @Deprecated
    public boolean hasPointsForConvoked() {
        return getPointsForConvoked() != null;
    }

    @Deprecated
    public boolean hasBeginOfFirstPeriodForUnavailablePeriods() {
        return getBeginOfFirstPeriodForUnavailablePeriods() != null;
    }

    @Deprecated
    public boolean hasRulesLink() {
        return getRulesLink() != null;
    }

    @Deprecated
    public boolean hasPointsForDisconvoked() {
        return getPointsForDisconvoked() != null;
    }

    @Deprecated
    public boolean hasExecutionYear() {
        return getExecutionYear() != null;
    }

}
