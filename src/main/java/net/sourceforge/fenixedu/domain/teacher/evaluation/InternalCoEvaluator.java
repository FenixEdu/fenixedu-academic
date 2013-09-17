package net.sourceforge.fenixedu.domain.teacher.evaluation;

import net.sourceforge.fenixedu.domain.Person;

public class InternalCoEvaluator extends InternalCoEvaluator_Base {

    public InternalCoEvaluator() {
        super();
    }

    public InternalCoEvaluator(final TeacherEvaluationProcess teacherEvaluationProcessFromEvaluee, final Person coEvaluatorPerson) {
        setTeacherEvaluationProcessFromEvaluee(teacherEvaluationProcessFromEvaluee);
        setPerson(coEvaluatorPerson);
    }

    @Override
    public void delete() {
        setPerson(null);
        super.delete();
    }

    @Override
    public String getDescription() {
        return getPerson().getName() + " (" + getPerson().getMostImportantAlias() + ")";
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
