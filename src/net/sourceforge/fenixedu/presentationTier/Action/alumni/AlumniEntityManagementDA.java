package net.sourceforge.fenixedu.presentationTier.Action.alumni;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

public abstract class AlumniEntityManagementDA extends FenixDispatchAction {

    public AlumniEntityManagementDA() {
	super();
    }

    protected Alumni getAlumni(HttpServletRequest request) throws Exception {
        Student alumniStudent = getLoggedPerson(request).getStudent();
        if (alumniStudent.hasAlumni()) {
            return alumniStudent.getAlumni();
        } else {
            return (Alumni) executeService("RegisterAlumniData", alumniStudent);
        }
    }

}