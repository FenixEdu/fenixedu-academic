package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.department;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "academicAdministration", path = "/manageDepartmentDegrees",
        input = "/manageDepartmentDegrees.do?method=prepare", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "manageDepartmentDegrees",
        path = "/academicAdministration/department/manageDepartmentDegrees.jsp") })
public class DepartmentDegreesDAForAcademicAdministration extends
        net.sourceforge.fenixedu.presentationTier.Action.manager.DepartmentDegreesDA {
}