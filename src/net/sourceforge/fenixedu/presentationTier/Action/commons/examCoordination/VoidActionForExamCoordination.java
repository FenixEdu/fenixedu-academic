package net.sourceforge.fenixedu.presentationTier.Action.commons.examCoordination;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "examCoordination", path = "/index", scope = "session")
@Forwards(value = { @Forward(
		name = "Success",
		path = "/vigilancy/vigilantGroupManagement.do?method=checkExamCoordinatorPermissions") })
public class VoidActionForExamCoordination extends net.sourceforge.fenixedu.presentationTier.Action.commons.VoidAction {
}