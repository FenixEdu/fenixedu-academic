package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.RegistrationProtocol;
import dml.runtime.RelationAdapter;
import pt.ist.fenixWebFramework.services.Service;

public class Coordinator extends Coordinator_Base {
    
    static {
	Person.CoordinatorTeacher.addListener(new RelationAdapter<Person, Coordinator>() {
	
	    @Override
	    public void afterAdd(Person o1, Coordinator o2) {
		if(o1 != null && o2 != null){
		    if(!o1.hasRole(RoleType.COORDINATOR)){
			o1.addPersonRoleByRoleType(RoleType.COORDINATOR);
		    }
		}
	    }
	    
	    @Override
	    public void afterRemove(Person o1, Coordinator o2) {
		if(o1 != null && o2 != null){
		    if(o1.getCoordinatorsCount() == 0 && !o1.hasAnyScientificCommissions()){
			o1.removeRoleByType(RoleType.COORDINATOR);
		    }
		}
	    }
	});
    }

    public Coordinator() {
	super();

	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Coordinator(ExecutionDegree executionDegree, Person person, Boolean responsible) {
	this();

	setExecutionDegree(executionDegree);
	setPerson(person);
	setResponsible(responsible);
    }

    public void delete() throws DomainException {
	
	checkRulesToDelete();
	
	removeExecutionDegree();
	removePerson();
	removeRootDomainObject();
	super.deleteDomainObject();
    }
    
    private void checkRulesToDelete() {
	if(hasAnyExecutionDegreeCoursesReports() || hasAnyStudentInquiriesCourseResults()) {
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

    @Service
    public void setAsResponsible() {
	this.setResponsible(Boolean.valueOf(true));
    }

    @Service
    public void setAsNotResponsible() {
	this.setResponsible(Boolean.valueOf(false));
    }

}
