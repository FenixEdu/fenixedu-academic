package net.sourceforge.fenixedu.presentationTier.Action.gep.lists;

import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author - Pedro Amaral
 * 
 */

@Mapping(path = "/studentsListByCurricularCourse", module = "gep")
@Forwards({ @Forward(name = "chooseCurricularCourse", path = "/gep/lists/chooseCurricularCourses.jsp"),
	@Forward(name = "studentByCurricularCourse", path = "/gep/lists/studentsByCurricularCourses.jsp") })
public class StudentsListByCurricularCourseDA extends
	net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.lists.StudentsListByCurricularCourseDA {

    @Override
    protected TreeSet<Degree> getAdministratedDegrees() {
	return new TreeSet<Degree>(Degree.readAllByDegreeType(DegreeType.values()));
    }
}
