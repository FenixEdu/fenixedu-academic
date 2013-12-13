package net.sourceforge.fenixedu.domain.teacher.evaluation;

import pt.ist.bennu.core.domain.Bennu;

public abstract class TeacherEvaluationCoEvaluator extends TeacherEvaluationCoEvaluator_Base {

    public TeacherEvaluationCoEvaluator() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        setTeacherEvaluationProcessFromEvaluee(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public abstract String getDescription();

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasTeacherEvaluationProcessFromEvaluee() {
        return getTeacherEvaluationProcessFromEvaluee() != null;
    }

}
