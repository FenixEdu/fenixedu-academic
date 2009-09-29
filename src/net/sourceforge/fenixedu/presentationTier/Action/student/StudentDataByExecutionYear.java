package net.sourceforge.fenixedu.presentationTier.Action.student;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.StudentDataByExecutionYearManagement;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/studentDataByExecutionYear", module = "student")
@Forwards( { @Forward(name = "showStudentData", path = "/student/personalDataAuthorization/studentDataByExecutionYear.jsp") })
public class StudentDataByExecutionYear extends StudentDataByExecutionYearManagement {
    @Override
    public Student getStudent(final HttpServletRequest request) {
	return AccessControl.getPerson().getStudent();
    }
}
