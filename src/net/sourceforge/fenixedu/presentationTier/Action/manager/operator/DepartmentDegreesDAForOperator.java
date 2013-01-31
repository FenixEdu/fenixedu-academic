package net.sourceforge.fenixedu.presentationTier.Action.manager.operator;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(
		module = "operator",
		path = "/manageDepartmentDegrees",
		input = "/manageDepartmentDegrees.do?method=prepare",
		scope = "request",
		parameter = "method")
@Forwards(value = { @Forward(name = "manageDepartmentDegrees", path = "/manager/manageDepartmentDegrees.jsp") })
public class DepartmentDegreesDAForOperator extends net.sourceforge.fenixedu.presentationTier.Action.manager.DepartmentDegreesDA {
}