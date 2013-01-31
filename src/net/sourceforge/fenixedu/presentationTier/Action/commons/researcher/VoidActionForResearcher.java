package net.sourceforge.fenixedu.presentationTier.Action.commons.researcher;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "researcher", path = "/index", scope = "session")
@Forwards(value = { @Forward(name = "Success", path = "/viewCurriculum.do?method=prepare", tileProperties = @Tile(
		title = "private.academicadministrativeoffice.studentoperations.viewstudents")) })
public class VoidActionForResearcher extends net.sourceforge.fenixedu.presentationTier.Action.commons.VoidAction {
}