package net.sourceforge.fenixedu.domain;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class ExternalTeacherAuthorization extends ExternalTeacherAuthorization_Base {

    public ExternalTeacherAuthorization() {
	super();
    }

    @Service
    public void revoke() {
	this.setActive(false);
	this.setRevoker(AccessControl.getPerson());
	this.setUnactiveTime(new DateTime());
    }

    public static Set<ExternalTeacherAuthorization> getExternalTeacherAuthorizationSet(ExecutionSemester executionSemester) {
	Set<ExternalTeacherAuthorization> teacherAuthorizations = new HashSet<ExternalTeacherAuthorization>();
	for (TeacherAuthorization ta : RootDomainObject.getInstance().getTeacherAuthorizationSet()) {
	    if (ta instanceof ExternalTeacherAuthorization) {
		ExternalTeacherAuthorization externalTeacherAuthorization = (ExternalTeacherAuthorization) ta;
		if (externalTeacherAuthorization.getActive()
			&& externalTeacherAuthorization.getExecutionSemester().equals(executionSemester)) {
		    teacherAuthorizations.add(externalTeacherAuthorization);
		}
	    }
	}
	return teacherAuthorizations;
    }
}
