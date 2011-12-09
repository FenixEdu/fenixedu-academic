package net.sourceforge.fenixedu.domain.student;

import pt.ist.fenixframework.plugins.remote.domain.RemoteHost;

public class RemoteRegistration extends RemoteRegistration_Base {

    public RemoteRegistration() {
	super();
    }

    public static String readAllStudentInfo(RemoteHost host) {
	return host.readRemoteStaticMethod("net.sourceforge.fenixedu.domain.student.Registration", "readAllStudentInfo", null);
    }

    public static String readAllStudentsInfoForJobBank(RemoteHost host) {
	return host.readRemoteStaticMethod("net.sourceforge.fenixedu.domain.student.Registration",
		"readAllStudentsInfoForJobBank", null);
    }

}
