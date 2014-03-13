package net.sourceforge.fenixedu.presentationTier.Action.credits.departmentAdmOffice;

import net.sourceforge.fenixedu.presentationTier.Action.credits.ManagePersonFunctionsDA;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/managePersonFunctionsShared", module = "departmentAdmOffice",
        functionality = DepartmentAdmOfficeViewTeacherCreditsDA.class)
public class DepartmentAdmOfficeManagePersonFunctionsDA extends ManagePersonFunctionsDA {

}
