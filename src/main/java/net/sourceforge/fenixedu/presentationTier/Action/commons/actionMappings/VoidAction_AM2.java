package net.sourceforge.fenixedu.presentationTier.Action.commons.actionMappings;


import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Forward;

import net.sourceforge.fenixedu.presentationTier.Action.commons.VoidAction;


@Mapping(path = "/studentSection", module = "coordinator")
@Forwards(value = {
	@Forward(name = "Success", path = "/student/indexStudent.jsp")})
public class VoidAction_AM2 extends VoidAction {

}
