package net.sourceforge.fenixedu.presentationTier.Action.commons.teacher;

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

@Mapping(module = "teacher", path = "/voidAction", attribute = "voidForm", formBean = "voidForm", scope = "request")
@Forwards(value = { @Forward(name = "Success", path = "/teacherInformation.do?method=prepareEdit&page=0") })
public class VoidActionForTeacher extends net.sourceforge.fenixedu.presentationTier.Action.commons.VoidAction {
}