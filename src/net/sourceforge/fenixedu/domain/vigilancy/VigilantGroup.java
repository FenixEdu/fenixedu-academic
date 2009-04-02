package net.sourceforge.fenixedu.domain.vigilancy;

import java.util.ArrayList;
import java.util.HashMap;
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
	for (Vigilant v : this.getVigilantsThatCanBeConvoked()) {
	    sumOfPoints += v.getPoints();
	}
	return ((this.getVigilantsThatCanBeConvoked().isEmpty()) ? 0 : sumOfPoints / this.getVigilantsThatCanBeConvoked().size());
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

    public VigilantBound getBounds(Vigilant vigilant) {
	for (VigilantBound bound : this.getBounds()) {
	    if (bound.getVigilant().equals(vigilant)) {
		return bound;
	    }
	}
	return null;
    }

    public List<Vigilant> getVigilants() {
	List<Vigilant> vigilants = new ArrayList<Vigilant>();
	for (VigilantBound bound : this.getBounds()) {
	    vigilants.add(bound.getVigilant());
	}
	return vigilants;
    }

    public void addVigilants(Vigilant vigilant) {
	if (!vigilantAlreadyHasBoundWithGroup(vigilant)) {
	    VigilantBound bound = new VigilantBound(vigilant, this);
	    this.addBounds(bound);
	}
    }

    public void removeVigilants(Vigilant vigilant) {
	for (VigilantBound bound : this.getBounds()) {
	    if (bound.getVigilant().equals(vigilant)) {
		bound.delete();
		return;
	    }
	}
    }

    public boolean vigilantAlreadyHasBoundWithGroup(Vigilant vigilant) {
	for (VigilantBound bound : this.getBounds()) {
	    if (bound.getVigilant().equals(vigilant)) {
		return Boolean.TRUE;
	    }
	}
	return Boolean.FALSE;
    }

    public int getVigilantsCount() {
	return this.getVigilants().size();
    }

    private List<Vigilant> getVigilantsWithACertainBound(Boolean bool) {
	List<Vigilant> vigilants = new ArrayList<Vigilant>();
	for (VigilantBound bound : this.getBounds()) {
	    if (bound.getConvokable() == bool) {
		vigilants.add(bound.getVigilant());
	    }
	}
	return vigilants;
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

    public List<Vigilant> getVigilantsThatCanBeConvoked() {
	return this.getVigilantsWithACertainBound(Boolean.TRUE);
    }

    public List<Vigilant> getVigilantsThatCantBeConvoked() {
	return this.getVigilantsWithACertainBound(Boolean.FALSE);
    }

    public List<VigilantBound> getBoundsWhereVigilantsAreNotConvokable() {
	List<VigilantBound> vigilantBounds = new ArrayList<VigilantBound>();
	for (VigilantBound bound : this.getBounds()) {
	    if (!bound.getConvokable()) {
		vigilantBounds.add(bound);
	    }
	}
	return vigilantBounds;
    }

    public HashMap<Vigilant, List<Vigilancy>> getConvokeMap() {

	HashMap<Vigilant, List<Vigilancy>> convokeMap = new HashMap<Vigilant, List<Vigilancy>>();
	List<Vigilant> vigilants = this.getVigilants();

	for (Vigilant vigilant : vigilants) {
	    convokeMap.put(vigilant, vigilant.getVigilancies());
	}

	return convokeMap;
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

    public List<Vigilancy> getVigilancies(Vigilant vigilant) {
	List<Vigilancy> vigilantConvokes = new ArrayList<Vigilancy>();
	for (Vigilancy convoke : this.getVigilancies()) {
	    if (convoke.getVigilant().equals(vigilant)) {
		vigilantConvokes.add(convoke);
	    }
	}
	return vigilantConvokes;
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
	for (VigilantWrapper vigilantWrapper : this.getVigilantWrappers()) {
	    unavailablePeriods.addAll(vigilantWrapper.getPerson().getUnavailablePeriods());
	}
	return unavailablePeriods;
    }

    public boolean canSpecifyUnavailablePeriodIn(DateTime date) {
	return (date.isAfter(this.getBeginOfFirstPeriodForUnavailablePeriods())
		&& date.isBefore(this.getEndOfFirstPeriodForUnavailablePeriods()) || (date.isAfter(this
		.getBeginOfSecondPeriodForUnavailablePeriods()) && date
		.isBefore(this.getEndOfSecondPeriodForUnavailablePeriods())));

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
	if (this.getBounds().isEmpty()) {
	    removeExecutionYear();
	    getExecutionCourses().clear();
	    getExamCoordinators().clear();
	    removeUnit();
	    removeRootDomainObject();
	    super.deleteDomainObject();
	} else {
	    throw new DomainException("label.vigilancy.error.cannotDeleteGroupWithVigilants");
	}
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
