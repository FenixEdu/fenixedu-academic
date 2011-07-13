package net.sourceforge.fenixedu.presentationTier.Action.manager.inquiries.operator;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "operator", path = "/createClassificationsForStudents", attribute = "createClassificationsForm", formBean = "createClassificationsForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "chooseDegreeCurricularPlan", path = "/manager/student/classifications/chooseDegreeCurricularPlan.jsp") })
public class CreateClassificationsForStudentsDispatchActionForOperator extends net.sourceforge.fenixedu.presentationTier.Action.manager.inquiries.CreateClassificationsForStudentsDispatchAction {
}