package net.sourceforge.fenixedu.presentationTier.Action.credits.departmentAdmOffice;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(
		module = "departmentAdmOffice",
		path = "/creditsManagementIndex",
		attribute = "executionPeriodForm",
		formBean = "executionPeriodForm",
		scope = "session")
@Forwards(value = { @Forward(name = "successfull-read", path = "creditsManagementIndex") })
public class IndexActionForDepartmentAdmOffice extends net.sourceforge.fenixedu.presentationTier.Action.credits.IndexAction {
}