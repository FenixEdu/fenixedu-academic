package net.sourceforge.fenixedu.presentationTier.Action.teacher.researcher;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "researcher", path = "/thesisDocumentConfirmation", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "viewThesis", path = "/teacher/viewThesis.jsp", tileProperties = @Tile(
				title = "private.curriculum.dissertations.confirmationdocuments")),
		@Forward(name = "showThesisList", path = "/teacher/showThesisList.jsp", tileProperties = @Tile(
				title = "private.curriculum.dissertations.confirmationdocuments")) })
public class ThesisDocumentConfirmationDAForResearcher extends
		net.sourceforge.fenixedu.presentationTier.Action.teacher.ThesisDocumentConfirmationDA {
}