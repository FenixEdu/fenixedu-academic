package net.sourceforge.fenixedu.domain.student;

import jvstm.cps.ConsistencyPredicate;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice;

import org.joda.time.DateTime;

public class StudentDataShareAuthorization extends StudentDataShareAuthorization_Base {

	public StudentDataShareAuthorization() {
		super();
	}

	public StudentDataShareAuthorization(Student student, StudentPersonalDataAuthorizationChoice authorization) {
		this();
		init(student, authorization);
	}

	protected void init(Student student, StudentPersonalDataAuthorizationChoice authorization) {
		setStudent(student);
		setAuthorizationChoice(authorization);
		setSince(new DateTime());
	}

	@ConsistencyPredicate
	public final boolean isDateNotInTheFuture() {
		return !getSince().isAfterNow();
	}

	@Override
	protected RootDomainObject getRootDomainObject() {
		return getStudent().getRootDomainObject();
	}

	public boolean isStudentDataShareAuthorization() {
		return true;
	}
}
