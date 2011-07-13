package net.sourceforge.fenixedu.presentationTier.Action.teacher.teacher;

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

@Mapping(module = "teacher", path = "/thesisDocumentConfirmation", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "viewThesis", path = "/teacher/viewThesis.jsp", tileProperties = @Tile(navLocal = "/teacher/commons/navigationBarIndex.jsp")),
		@Forward(name = "viewOperationsThesis", path = "/student/thesis/viewOperationsThesis.jsp", tileProperties = @Tile(navLocal = "/teacher/commons/navigationBarIndex.jsp")),
		@Forward(name = "showThesisList", path = "/teacher/showThesisList.jsp", tileProperties = @Tile(navLocal = "/teacher/commons/navigationBarIndex.jsp")) })
public class ThesisDocumentConfirmationDAForTeacher extends net.sourceforge.fenixedu.presentationTier.Action.teacher.ThesisDocumentConfirmationDA {
}