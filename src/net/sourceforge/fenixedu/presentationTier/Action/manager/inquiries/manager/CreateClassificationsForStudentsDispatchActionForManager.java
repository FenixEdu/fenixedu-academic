package net.sourceforge.fenixedu.presentationTier.Action.manager.inquiries.manager;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(
		module = "manager",
		path = "/createClassificationsForStudents",
		attribute = "createClassificationsForm",
		formBean = "createClassificationsForm",
		scope = "request",
		parameter = "method")
@Forwards(value = { @Forward(
		name = "chooseDegreeCurricularPlan",
		path = "/manager/student/classifications/chooseDegreeCurricularPlan.jsp") })
public class CreateClassificationsForStudentsDispatchActionForManager extends
		net.sourceforge.fenixedu.presentationTier.Action.manager.inquiries.CreateClassificationsForStudentsDispatchAction {
}