package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.department;

import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AcademicAdministrationApplication.AcademicAdminDCPApp;
import net.sourceforge.fenixedu.presentationTier.Action.manager.DepartmentDegreesDA;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = AcademicAdminDCPApp.class, path = "department-degrees", titleKey = "label.manage.department.degrees",
        accessGroup = "academic(MANAGE_DEGREE_CURRICULAR_PLANS)")
@Mapping(module = "academicAdministration", path = "/manageDepartmentDegrees",
        input = "/manageDepartmentDegrees.do?method=prepare")
@Forwards(@Forward(name = "manageDepartmentDegrees", path = "/academicAdministration/department/manageDepartmentDegrees.jsp"))
public class DepartmentDegreesDAForAcademicAdministration extends DepartmentDegreesDA {
}