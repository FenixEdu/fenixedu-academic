package net.sourceforge.fenixedu.domain.student;

import jvstm.cps.ConsistencyPredicate;
import net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice;

import org.joda.time.DateTime;

public class StudentDataShareAuthorization extends StudentDataShareAuthorization_Base {
    public StudentDataShareAuthorization(Student student, StudentPersonalDataAuthorizationChoice authorization) {
	super();
	setStudent(student);
	setAuthorizationChoice(authorization);
	setSince(new DateTime());
    }

    @ConsistencyPredicate
    public final boolean isDateNotInTheFuture() {
	return !getSince().isAfterNow();
    }
}
