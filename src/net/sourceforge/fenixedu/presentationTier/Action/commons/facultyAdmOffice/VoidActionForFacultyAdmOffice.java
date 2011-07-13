package net.sourceforge.fenixedu.presentationTier.Action.commons.facultyAdmOffice;

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

@Mapping(module = "facultyAdmOffice", path = "/index", attribute = "voidForm", formBean = "voidForm", scope = "session")
@Forwards(value = { @Forward(name = "Success", path = "/facultyAdmOffice/index.jsp") })
public class VoidActionForFacultyAdmOffice extends net.sourceforge.fenixedu.presentationTier.Action.commons.VoidAction {
}