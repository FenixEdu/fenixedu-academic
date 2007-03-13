package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.injectionCode.Checked;

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

    @Override
    @Checked("CoordinationPredicates.responsible")
    public void setThesisCoordinator(Boolean thesisCoordinator) {
        super.setThesisCoordinator(thesisCoordinator);
    }
    
}
