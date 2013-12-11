package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.bennu.core.domain.Bennu;

public class WrittenEvaluationEnrolment extends WrittenEvaluationEnrolment_Base {

    public WrittenEvaluationEnrolment() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public WrittenEvaluationEnrolment(WrittenEvaluation writtenEvaluation, Registration registration) {
        this();
        this.setWrittenEvaluation(writtenEvaluation);
        this.setStudent(registration);
    }

    public WrittenEvaluationEnrolment(WrittenEvaluation writtenEvaluation, Registration registration, AllocatableSpace room) {
        this();
        this.setWrittenEvaluation(writtenEvaluation);
        this.setStudent(registration);
        this.setRoom(room);
    }

    public void delete() {
        if (this.getRoom() != null) {
            throw new DomainException("error.notAuthorizedUnEnrollment");
        }

        this.setWrittenEvaluation(null);
        this.setStudent(null);

        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public boolean isForExecutionPeriod(final ExecutionSemester executionSemester) {
        for (final ExecutionCourse executionCourse : getWrittenEvaluation().getAssociatedExecutionCourses()) {
            if (executionCourse.getExecutionPeriod() == executionSemester) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    public boolean hasStudent() {
        return getStudent() != null;
    }

    @Deprecated
    public boolean hasWrittenEvaluation() {
        return getWrittenEvaluation() != null;
    }

    @Deprecated
    public boolean hasRoom() {
        return getRoom() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

}
