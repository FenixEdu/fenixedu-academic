package net.sourceforge.fenixedu.domain.student;

public class RemoteStudent extends RemoteStudent_Base {

    public RemoteStudent() {
	super();
    }

    public String readAllStudentInfoForJobBank() {
	return readRemoteMethod("readAllStudentInfoForJobBank", null);
    }
}
