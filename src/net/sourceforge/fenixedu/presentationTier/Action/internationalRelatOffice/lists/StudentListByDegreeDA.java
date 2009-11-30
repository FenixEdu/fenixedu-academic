package net.sourceforge.fenixedu.presentationTier.Action.internationalRelatOffice.lists;

import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/studentsListByDegree", module = "internationalRelatOffice")
@Forwards( { @Forward(name = "searchRegistrations", path = "/internationalRelatOffice/lists/searchRegistrationsByDegree.jsp") })
public class StudentListByDegreeDA extends
	net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.lists.StudentListByDegreeDA {

    @Override
    protected TreeSet<DegreeType> getAdministratedDegreeTypes() {
	TreeSet<DegreeType> administratedDegreeTypes = new TreeSet<DegreeType>();
	for (DegreeType degreetype : DegreeType.NOT_EMPTY_VALUES) {
	    administratedDegreeTypes.add(degreetype);
	}
	return administratedDegreeTypes;
    }

    @Override
    protected TreeSet<Degree> getAdministratedDegrees() {
	TreeSet<Degree> administratedDegrees = new TreeSet<Degree>();
	for (DegreeType degreetype : DegreeType.values()) {
	    administratedDegrees.addAll(Degree.readAllByDegreeType(degreetype));
	}
	return administratedDegrees;
    }

    @Override
    protected TreeSet<CycleType> getAdministratedCycleTypes() {
	TreeSet<CycleType> administratedCycles = new TreeSet<CycleType>();
	for (CycleType cycle : CycleType.values()) {
	    administratedCycles.add(cycle);
	}
	return administratedCycles;
    }
}