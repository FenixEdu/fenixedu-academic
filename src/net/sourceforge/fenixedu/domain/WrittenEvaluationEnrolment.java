package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Room;

public class WrittenEvaluationEnrolment extends WrittenEvaluationEnrolment_Base {

    public WrittenEvaluationEnrolment() {
    }

    public WrittenEvaluationEnrolment(WrittenEvaluation writtenEvaluation, Student student) {
        this.setWrittenEvaluation(writtenEvaluation);
        this.setStudent(student);
    }

    public WrittenEvaluationEnrolment(WrittenEvaluation writtenEvaluation, Student student, Room room) {
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

    public boolean isForExecutionPeriod(final ExecutionPeriod executionPeriod) {
        for (final ExecutionCourse executionCourse : this.getWrittenEvaluation()
                .getAssociatedExecutionCourses()) {
            if (executionCourse.getExecutionPeriod() == executionPeriod) {
                return true;
            }
        }
        return false;
    }

}
