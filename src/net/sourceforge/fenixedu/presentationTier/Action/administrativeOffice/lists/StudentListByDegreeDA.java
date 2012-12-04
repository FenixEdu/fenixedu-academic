package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.lists;

import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/studentsListByDegree", module = "academicAdminOffice")
@Forwards({ @Forward(name = "searchRegistrations", path = "/academicAdminOffice/lists/searchRegistrationsByDegree.jsp", tileProperties = @Tile(title = "private.academicadministrativeoffice.lists.studentsbydegree")) })
public class StudentListByDegreeDA extends
	net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.lists.StudentListByDegreeDA {

    @Override
    protected TreeSet<DegreeType> getAdministratedDegreeTypes() {
	return new TreeSet<DegreeType>(getAdministrativeOffice().getAdministratedDegreeTypes());
    }

    @Override
    protected TreeSet<Degree> getAdministratedDegrees() {
	return new TreeSet<Degree>(getAdministrativeOffice().getAdministratedDegrees());
    }

    @Override
    protected TreeSet<CycleType> getAdministratedCycleTypes() {
	return new TreeSet<CycleType>(getAdministrativeOffice().getAdministratedCycleTypes());
    }

    private AdministrativeOffice getAdministrativeOffice() {
	return AccessControl.getPerson().getEmployee().getAdministrativeOffice();
    }
}