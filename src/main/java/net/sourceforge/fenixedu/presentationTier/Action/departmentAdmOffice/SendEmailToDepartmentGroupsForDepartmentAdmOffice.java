package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import net.sourceforge.fenixedu.presentationTier.Action.department.SendEmailToDepartmentGroups;
import net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.DepartmentAdmOfficeApp.DepartmentAdmOfficeMessagingApp;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = DepartmentAdmOfficeMessagingApp.class, path = "send-email-to-department-groups",
        titleKey = "label.sendEmailToGroups")
@Mapping(module = "departmentAdmOffice", path = "/sendEmailToDepartmentGroups")
public class SendEmailToDepartmentGroupsForDepartmentAdmOffice extends SendEmailToDepartmentGroups {
}