package net.sourceforge.fenixedu.domain;

public class Coordinator extends Coordinator_Base {

	public Coordinator() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}
    
    public Coordinator(ExecutionDegree executionDegree, Teacher teacher, Boolean responsible) {
        this();
        setExecutionDegree(executionDegree);
        setTeacher(teacher);
        setResponsible(responsible);
    }
	
	public void delete() {
        removeExecutionDegree();
        removeTeacher();
		removeRootDomainObject();
        super.deleteDomainObject();
	}

}
