package net.sourceforge.fenixedu.presentationTier.Action.department.departmentMember;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentMember", path = "/sendEmailToDepartmentGroups", scope = "session", parameter = "method")
public class SendEmailToDepartmentGroupsForDepartmentMember extends
		net.sourceforge.fenixedu.presentationTier.Action.department.SendEmailToDepartmentGroups {
}