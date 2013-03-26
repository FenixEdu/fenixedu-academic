package net.sourceforge.fenixedu.presentationTier.Action.department.departmentAdmOffice;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentAdmOffice", path = "/sendEmailToDepartmentGroups", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "compose-mail", path = "sendMail"), @Forward(name = "problem", path = "sendMail"),
        @Forward(name = "success", path = "sendMail") })
public class SendEmailToDepartmentGroupsForDepartmentAdmOffice extends
        net.sourceforge.fenixedu.presentationTier.Action.department.SendEmailToDepartmentGroups {
}