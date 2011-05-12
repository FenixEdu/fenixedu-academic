package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.InquiryCoordinatorAnswer;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;
import dml.runtime.RelationAdapter;

public class Coordinator extends Coordinator_Base {

    static {
	Person.CoordinatorTeacher.addListener(new RelationAdapter<Person, Coordinator>() {

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
		    if (o1.getCoordinatorsCount() == 0 && !o1.hasAnyScientificCommissions()) {
			o1.removeRoleByType(RoleType.COORDINATOR);
		    }
		}
	    }
	});
    }

    protected Coordinator() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());

    }

    public Coordinator(final ExecutionDegree executionDegree, final Person person, final Boolean responsible) {
	this();

	for (final Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
	    if (coordinator.getPerson() == person) {
		throw new Error("error.person.already.is.coordinator.for.same.execution.degree");
	    }
	}

	setExecutionDegree(executionDegree);
	setPerson(person);
	setResponsible(responsible);
	// CoordinatorLog.createCoordinatorLog(new DateTime(),
	// OperationType.ADD, this);
    }

    public void delete() throws DomainException {

	checkRulesToDelete();
	removeExecutionDegree();
	removePerson();
	getExecutionDegreeCoursesReports().clear();
	getStudentInquiriesCourseResults().clear();

	removeRootDomainObject();
	super.deleteDomainObject();
    }

    private void checkRulesToDelete() {
	if (hasAnyExecutionDegreeCoursesReports()) {
	    for (CoordinatorExecutionDegreeCoursesReport report : getExecutionDegreeCoursesReports()) {
		if (!report.isEmpty())
		    throw new DomainException("error.Coordinator.cannot.delete.because.already.has.written.comments");
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

    @Service
    public static Coordinator createCoordinator(ExecutionDegree executionDegree, Person person, Boolean responsible) {
	return new Coordinator(executionDegree, person, responsible);
    }

    @Service
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

    @Service
    public void setAsResponsible() {
	this.setResponsible(Boolean.valueOf(true));
    }

    @Service
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
}
