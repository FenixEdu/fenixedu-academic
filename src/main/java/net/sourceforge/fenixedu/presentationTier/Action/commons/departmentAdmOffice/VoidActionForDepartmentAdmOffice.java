package net.sourceforge.fenixedu.presentationTier.Action.commons.departmentAdmOffice;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentAdmOffice", path = "/index", scope = "session")
@Forwards(value = { @Forward(name = "Success", path = "/departmentAdmOffice/index.jsp") })
public class VoidActionForDepartmentAdmOffice extends net.sourceforge.fenixedu.presentationTier.Action.commons.VoidAction {
}