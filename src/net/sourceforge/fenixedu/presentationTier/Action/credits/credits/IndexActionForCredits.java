package net.sourceforge.fenixedu.presentationTier.Action.credits.credits;

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

@Mapping(module = "credits", path = "/index", scope = "session")
@Forwards(value = {
		@Forward(name = "creditsManager", path = "/credits/searchTeacher.jsp", tileProperties = @Tile(navLocal = "/commons/blank.jsp")),
		@Forward(name = "creditsManagerDepartment", path = "/credits/listTeachers.jsp") })
public class IndexActionForCredits extends net.sourceforge.fenixedu.presentationTier.Action.credits.IndexAction {
}