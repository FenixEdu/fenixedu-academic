package net.sourceforge.fenixedu.domain.teacher.evaluation;

public class ExternalCoEvaluator extends ExternalCoEvaluator_Base {

    public ExternalCoEvaluator() {
        super();
    }

    public ExternalCoEvaluator(final TeacherEvaluationProcess teacherEvaluationProcessFromEvaluee, final String coevaluatorString) {
        setTeacherEvaluationProcessFromEvaluee(teacherEvaluationProcessFromEvaluee);
        setName(coevaluatorString);
    }

    @Override
    public String getDescription() {
        return getName();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

}
