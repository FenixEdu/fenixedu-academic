package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.lists;

import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/studentsListByDegree", module = "departmentAdmOffice")
@Forwards({ @Forward(name = "searchRegistrations", path = "/departmentAdmOffice/lists/searchRegistrationsByDegree.jsp", tileProperties = @Tile(title = "private.administrationofcreditsofdepartmentteachers.lists.studentsbydegree")) })
public class StudentListByDegreeDA extends
	net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.lists.StudentListByDegreeDA {

    @Override
    protected TreeSet<DegreeType> getAdministratedDegreeTypes() {
	return new TreeSet<DegreeType>(getDepartment().getDegreeTypes());
    }

    @Override
    protected TreeSet<Degree> getAdministratedDegrees() {
	return new TreeSet<Degree>(getDepartment().getDegrees());
    }

    @Override
    protected TreeSet<CycleType> getAdministratedCycleTypes() {
	return new TreeSet<CycleType>(getDepartment().getCycleTypes());
    }

    private Department getDepartment() {
	return AccessControl.getPerson().getEmployee().getCurrentDepartmentWorkingPlace();
    }
}