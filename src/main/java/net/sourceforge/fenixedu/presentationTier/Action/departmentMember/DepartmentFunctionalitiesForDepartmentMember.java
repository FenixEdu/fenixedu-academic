package net.sourceforge.fenixedu.presentationTier.Action.departmentMember;

import net.sourceforge.fenixedu.presentationTier.Action.department.DepartmentFunctionalities;
import net.sourceforge.fenixedu.presentationTier.Action.departmentMember.DepartmentMemberApp.DepartmentMemberMessagingApp;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = DepartmentMemberMessagingApp.class, path = "manage-files", titleKey = "label.manageFiles")
@Mapping(module = "departmentMember", path = "/departmentFunctionalities")
public class DepartmentFunctionalitiesForDepartmentMember extends DepartmentFunctionalities {

}
