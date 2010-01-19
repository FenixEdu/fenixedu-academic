package net.sourceforge.fenixedu.domain;

import pt.ist.fenixWebFramework.services.Service;

public class Coordinator extends Coordinator_Base {

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

    public void delete() {
	removeExecutionDegree();
	removePerson();
	removeRootDomainObject();
	super.deleteDomainObject();
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
