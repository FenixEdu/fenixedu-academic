package net.sourceforge.fenixedu.domain;


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

}
