package net.sourceforge.fenixedu.presentationTier.Action.commons.departmentMember;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentMember", path = "/index", scope = "session")
@Forwards(value = { @Forward(name = "Success", path = "/departmentMember/index.jsp") })
public class VoidActionForDepartmentMember extends net.sourceforge.fenixedu.presentationTier.Action.commons.VoidAction {
}