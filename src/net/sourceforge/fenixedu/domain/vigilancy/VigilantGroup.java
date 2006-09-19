package net.sourceforge.fenixedu.domain.vigilancy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.vigilancy.strategies.Strategy;
import net.sourceforge.fenixedu.domain.vigilancy.strategies.StrategyFactory;
import net.sourceforge.fenixedu.domain.vigilancy.strategies.StrategySugestion;

import org.joda.time.DateTime;

public class VigilantGroup extends VigilantGroup_Base {

    public VigilantGroup() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public StrategySugestion sugestVigilantsToConvoke(WrittenEvaluation writtenEvaluation) {

        String strategyName = this.getConvokeStrategy();
        StrategyFactory factory = StrategyFactory.getInstance();
        Strategy strategy = factory.getStrategy(strategyName);
        return (strategy != null) ? strategy.sugest(this.getVigilants(), writtenEvaluation) : null;

    }

    public void convokeVigilants(List<Vigilant> vigilants, WrittenEvaluation writtenEvaluation) {

        for (Vigilant vigilant : vigilants) {
            if (!vigilant.hasBeenConvokedForEvaluation(writtenEvaluation)) {
                vigilant.addConvokes(new Convoke(writtenEvaluation));
            }
        }
    }

    public HashMap<Vigilant, List<Convoke>> getConvokeMap() {

        HashMap<Vigilant, List<Convoke>> convokeMap = new HashMap<Vigilant, List<Convoke>>();
        List<Vigilant> vigilants = this.getVigilants();

        for (Vigilant vigilant : vigilants) {
            convokeMap.put(vigilant, vigilant.getConvokes());
        }

        return convokeMap;
    }

    public List<Person> getPersons() {
        List<Person> persons = new ArrayList<Person>();
        List<Vigilant> vigilants = this.getVigilants();
        for (Vigilant vigilant : vigilants) {
            persons.add(vigilant.getPerson());
        }

        return persons;
    }

    public List<Convoke> getConvokes() {
        List<Convoke> convokes = new ArrayList<Convoke>();
        for (ExecutionCourse course : this.getExecutionCourses()) {
            for (WrittenEvaluation evaluation : course.getWrittenEvaluations()) {
                convokes.addAll(evaluation.getConvokes());
            }
        }
        return convokes;
    }

    public List<Convoke> getConvokesAfterDate(DateTime date) {
        List<Convoke> convokesToReturn = new ArrayList<Convoke>();
        for (Convoke convoke : this.getConvokes()) {
            if (convoke.getBeginDateTime().isAfter(date) || convoke.getBeginDateTime().isEqual(date)) {
                convokesToReturn.add(convoke);
            }
        }
        return convokesToReturn;
    }

    public List<Convoke> getConvokesBeforeDate(DateTime date) {
        List<Convoke> convokesToReturn = new ArrayList<Convoke>();
        for (Convoke convoke : this.getConvokes()) {
            if (convoke.getBeginDateTime().isBefore(date)) {
                convokesToReturn.add(convoke);
            }
        }
        return convokesToReturn;
    }

    public List<Convoke> getConvokes(Vigilant vigilant) {
        List<Convoke> convokes = this.getConvokes();
        List<Convoke> vigilantConvokes = new ArrayList<Convoke>();
        for (Convoke convoke : convokes) {
            if (convoke.getVigilant().equals(vigilant)) {
                vigilantConvokes.add(convoke);
            }
        }
        return vigilantConvokes;
    }

    public List<Vigilant> removeGivenVigilantsFromGroup(List<Vigilant> vigilants) {

        List<Vigilant> vigilantsUnableToRemove = new ArrayList<Vigilant>();
        vigilants.retainAll(this.getVigilants());

        for (Vigilant vigilant : vigilants) {
            try {
                vigilant.delete();
            } catch (DomainException e) {
                vigilantsUnableToRemove.add(vigilant);
            }
        }

        return vigilantsUnableToRemove;
    }

    public List<UnavailablePeriod> getUnavailablePeriodsOfVigilantsInGroup() {
        List<UnavailablePeriod> unavailablePeriods = new ArrayList<UnavailablePeriod>();
        for (Vigilant vigilant : this.getVigilants()) {
            unavailablePeriods.addAll(vigilant.getUnavailablePeriods());
        }
        return unavailablePeriods;
    }

    public boolean canSpecifyUnavailablePeriodIn(DateTime date) {
        return (date.isAfter(this.getBeginOfFirstPeriodForUnavailablePeriods())
                && date.isBefore(this.getEndOfFirstPeriodForUnavailablePeriods()) || (date.isAfter(this
                .getBeginOfSecondPeriodForUnavailablePeriods()) && date.isBefore(this
                .getEndOfSecondPeriodForUnavailablePeriods())));

    }

    public List<Vigilant> getVigilantsWithIncompatiblePerson() {
        List<Vigilant> vigilants = new ArrayList<Vigilant>();
        for (Vigilant vigilant : this.getVigilants()) {
        	Person incompatiblePerson = vigilant.getIncompatiblePerson();
        	if (incompatiblePerson != null && !vigilants.contains(incompatiblePerson.getCurrentVigilant())) {
                vigilants.add(vigilant);
            }
        }
        return vigilants;
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
        if (this.getVigilants().isEmpty()) {
            removeExecutionYear();
            for (; this.hasAnyExecutionCourses(); this.getExecutionCourses().get(0)
                    .removeVigilantGroup())
                ;
            removeUnit();
            removeRootDomainObject();
            super.deleteDomainObject();
        } else {
            throw new DomainException("label.vigilancy.error.cannotDeleteGroupWithVigilants");
        }
    }

    public List<WrittenEvaluation> getAllAssocitedWrittenEvaluations() {
        List<ExecutionCourse> courses = this.getExecutionCourses();
        Set<WrittenEvaluation> evaluations = new HashSet<WrittenEvaluation>();
        for (ExecutionCourse course : courses) {
            evaluations.addAll(course.getWrittenEvaluations());
        }
        return new ArrayList<WrittenEvaluation>(evaluations);
    }

    public List<WrittenEvaluation> getWrittenEvaluations() {
        List<Convoke> convokes = this.getConvokes();
        return getEvaluationsFromConvokes(convokes);
    }

    public List<WrittenEvaluation> getWrittenEvaluationsAfterDate(DateTime date) {
        List<Convoke> convokes = this.getConvokesAfterDate(date);
        return getEvaluationsFromConvokes(convokes);
    }

    public List<WrittenEvaluation> getWrittenEvaluationsBeforeDate(DateTime date) {
        List<Convoke> convokes = this.getConvokesBeforeDate(date);
        return getEvaluationsFromConvokes(convokes);
    }

    private List<WrittenEvaluation> getEvaluationsFromConvokes(List<Convoke> convokes) {
        Set<WrittenEvaluation> evaluations = new HashSet<WrittenEvaluation>();

        for (Convoke convoke : convokes) {
            evaluations.add(convoke.getWrittenEvaluation());
        }
        return new ArrayList<WrittenEvaluation>(evaluations);
    }

}
