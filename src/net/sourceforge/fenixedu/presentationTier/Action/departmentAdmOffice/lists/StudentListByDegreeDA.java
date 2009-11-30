package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.lists;

import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/studentsListByDegree", module = "departmentAdmOffice")
@Forwards( { @Forward(name = "searchRegistrations", path = "/departmentAdmOffice/lists/searchRegistrationsByDegree.jsp") })
public class StudentListByDegreeDA extends
	net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.lists.StudentListByDegreeDA {

    @Override
    protected TreeSet<DegreeType> getAdministratedDegreeTypes() {
	TreeSet<DegreeType> administratedDegreeTypes = new TreeSet<DegreeType>();
	for (Degree degree : AccessControl.getPerson().getEmployee().getCurrentDepartmentWorkingPlace().getDegrees()) {
	    administratedDegreeTypes.add(degree.getDegreeType());
	}
	return administratedDegreeTypes;
    }

    @Override
    protected TreeSet<Degree> getAdministratedDegrees() {
	return new TreeSet<Degree>(AccessControl.getPerson().getEmployee().getCurrentDepartmentWorkingPlace().getDegrees());
    }

    @Override
    protected TreeSet<CycleType> getAdministratedCycleTypes() {
	TreeSet<CycleType> administratedCycles = new TreeSet<CycleType>();
	for (Degree degree : AccessControl.getPerson().getEmployee().getCurrentDepartmentWorkingPlace().getDegrees()) {
	    administratedCycles.addAll(degree.getDegreeType().getCycleTypes());
	}
	return administratedCycles;
    }
}