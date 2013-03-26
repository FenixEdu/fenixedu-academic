package net.sourceforge.fenixedu.domain.teacher.evaluation;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public abstract class TeacherEvaluationCoEvaluator extends TeacherEvaluationCoEvaluator_Base {

    public TeacherEvaluationCoEvaluator() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        removeTeacherEvaluationProcessFromEvaluee();
        removeRootDomainObject();
        deleteDomainObject();
    }

    public abstract String getDescription();

}
