package net.sourceforge.fenixedu.presentationTier.Action.internationalRelatOffice.lists;

import java.util.Arrays;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/studentsListByDegree", module = "internationalRelatOffice")
@Forwards( { @Forward(name = "searchRegistrations", path = "/internationalRelatOffice/lists/searchRegistrationsByDegree.jsp") })
public class StudentListByDegreeDA extends
	net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.lists.StudentListByDegreeDA {

    @Override
    protected TreeSet<DegreeType> getAdministratedDegreeTypes() {
	return new TreeSet<DegreeType>(DegreeType.NOT_EMPTY_VALUES);
    }

    @Override
    protected TreeSet<Degree> getAdministratedDegrees() {
	return new TreeSet<Degree>(Degree.readAllByDegreeTypes(DegreeType.NOT_EMPTY_VALUES));
    }

    @Override
    protected TreeSet<CycleType> getAdministratedCycleTypes() {
	return new TreeSet<CycleType>(Arrays.asList(CycleType.values()));
    }
}