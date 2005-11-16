package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class WrittenEvaluationEnrolment extends WrittenEvaluationEnrolment_Base {

    public WrittenEvaluationEnrolment() {
    }

    public WrittenEvaluationEnrolment(IWrittenEvaluation writtenEvaluation, IStudent student) {
        this.setWrittenEvaluation(writtenEvaluation);
        this.setStudent(student);
    }

    public WrittenEvaluationEnrolment(IWrittenEvaluation writtenEvaluation, IStudent student, IRoom room) {
        this.setWrittenEvaluation(writtenEvaluation);
        this.setStudent(student);
        this.setRoom(room);
    }

    public void delete() {
        if (this.getRoom() != null) {
            throw new DomainException("error.notAuthorizedUnEnrollment");
        }

        this.setWrittenEvaluation(null);
        this.setStudent(null);

        super.deleteDomainObject();
    }

    public boolean isForExecutionPeriod(final IExecutionPeriod executionPeriod) {
        for (final IExecutionCourse executionCourse : this.getWrittenEvaluation()
                .getAssociatedExecutionCourses()) {
            if (executionCourse.getExecutionPeriod() == executionPeriod) {
                return true;
            }
        }
        return false;
    }

}
