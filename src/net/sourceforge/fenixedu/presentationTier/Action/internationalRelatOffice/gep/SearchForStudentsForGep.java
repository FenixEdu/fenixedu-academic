package net.sourceforge.fenixedu.presentationTier.Action.internationalRelatOffice.gep;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "gep", path = "/students", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "viewStudentDetails", path = "/internationalRelatOffice/viewStudentDetails.jsp"),
		@Forward(name = "search", path = "/internationalRelatOffice/searchStudents.jsp") })
public class SearchForStudentsForGep extends net.sourceforge.fenixedu.presentationTier.Action.internationalRelatOffice.SearchForStudents {
}