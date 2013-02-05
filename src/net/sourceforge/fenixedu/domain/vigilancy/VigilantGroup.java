package net.sourceforge.fenixedu.domain.vigilancy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.vigilancy.strategies.Strategy;
import net.sourceforge.fenixedu.domain.vigilancy.strategies.StrategyFactory;
import net.sourceforge.fenixedu.domain.vigilancy.strategies.StrategySugestion;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class VigilantGroup extends VigilantGroup_Base {

    public VigilantGroup() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
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

    private List<VigilantWrapper> findTeachersThatAreInGroupFor(List<ExecutionCourse> executionCourses) {
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
                if (teacher != null && teacher.teachesAny(writtenEvaluation.getAssociatedExecutionCourses())) {
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

    public List<VigilantWrapper> getVigilants() {
        return this.getVigilantWrappers();
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
        List<VigilantWrapper> vigilantWrappers = this.getVigilantWrappers();
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

    public List<Vigilancy> getVigilancies(VigilantWrapper vigilantWrapper) {
        List<Vigilancy> vigilantConvokes = new ArrayList<Vigilancy>();
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
        removeExecutionYear();
        getExecutionCourses().clear();
        getExamCoordinators().clear();
        removeUnit();
        removeRootDomainObject();
        for (VigilantWrapper vigilant : this.getVigilantWrappers()) {
            List<Vigilancy> vigilancies = vigilant.getVigilancies();
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
        List<ExecutionCourse> courses = this.getExecutionCourses();
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

    @Service
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
}
