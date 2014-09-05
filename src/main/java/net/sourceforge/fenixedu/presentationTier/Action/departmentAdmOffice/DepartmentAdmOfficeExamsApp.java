package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import org.fenixedu.bennu.portal.StrutsApplication;

@StrutsApplication(bundle = "VigilancyResources", path = "exam-coordination",
        titleKey = "label.navheader.person.examCoordinator", hint = "Department Admin Office",
        accessGroup = "role(DEPARTMENT_ADMINISTRATIVE_OFFICE)")
public class DepartmentAdmOfficeExamsApp {
}