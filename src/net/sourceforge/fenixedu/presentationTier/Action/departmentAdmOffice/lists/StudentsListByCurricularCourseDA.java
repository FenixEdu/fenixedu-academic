/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.lists;

import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author - �ngela Almeida (argelina@ist.utl.pt)
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */

@Mapping(path = "/studentsListByCurricularCourse", module = "departmentAdmOffice")
@Forwards({
	@Forward(name = "chooseCurricularCourse", path = "/departmentAdmOffice/lists/chooseCurricularCourses.jsp", tileProperties = @Tile(title = "private.administrationofcreditsofdepartmentteachers.lists.studentsbycourse")),
	@Forward(name = "studentByCurricularCourse", path = "/departmentAdmOffice/lists/studentsByCurricularCourses.jsp", tileProperties = @Tile(title = "private.administrationofcreditsofdepartmentteachers.lists.studentsbycourse")) })
public class StudentsListByCurricularCourseDA extends
	net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.lists.StudentsListByCurricularCourseDA {

    @Override
    protected TreeSet<Degree> getAdministratedDegrees() {
	return new TreeSet<Degree>(AccessControl.getPerson().getEmployee().getCurrentDepartmentWorkingPlace().getDegrees());
    }
}
