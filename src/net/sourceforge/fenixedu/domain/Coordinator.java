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

    public boolean isResponsible() {
        return getResponsible().booleanValue();
    }

    public boolean isCoordinatorOfExecutionDegreeContaining(final ExecutionCourse executionCourse) {
        final ExecutionDegree executionDegree = getExecutionDegree();
        if (executionCourse.isLecturedIn(executionDegree.getExecutionYear())) {
            final ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
            final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
            for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
                if (curricularCourse.getDegreeCurricularPlan() == degreeCurricularPlan
                        && curricularCourse.hasActiveScopesInExecutionPeriod(executionPeriod)) {
                    return true;
                }
            }
        }
        return false;
    }

}
