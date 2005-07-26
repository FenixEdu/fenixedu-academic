package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * @author Tânia Pousão
 * 
 */
public class FinalEvaluation extends FinalEvaluation_Base {

    public FinalEvaluation() {
        this.setOjbConcreteClass(FinalEvaluation.class.getName());
    }

    public boolean deleteFrom(IExecutionCourse executionCourse) {
        removeMarks();
        return remove(executionCourse);
    }

    private boolean remove(IExecutionCourse executionCourse) {
        if (this.getAssociatedExecutionCoursesCount() == 1
                && this.getAssociatedExecutionCourses().contains(executionCourse)
                && this.getEvaluationExecutionCoursesCount() == 1) {
            final IEvaluationExecutionCourse evaluationExecutionCourse = this
                    .getEvaluationExecutionCourses(0);
            if (evaluationExecutionCourse.getExecutionCourse().equals(executionCourse)) {
                this.getEvaluationExecutionCourses().remove(evaluationExecutionCourse);
                this.getAssociatedExecutionCourses().remove(executionCourse);
                return true;
            }
        }
        return false;
    }

    private void removeMarks() {
        if (this.getMarksCount() > 0) {
            throw new DomainException("error.existing.marks");
        }
    }

}