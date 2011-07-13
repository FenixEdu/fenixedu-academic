package net.sourceforge.fenixedu.presentationTier.Action.manager.operator;

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

@Mapping(module = "operator", path = "/studentsManagement", input = "/studentsManagement.do?method=show&page=0", attribute = "studentCurricularPlanForm", formBean = "studentCurricularPlanForm", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "createStudentCurricularPlan", path = "/manager/createStudentCurricularPlan.jsp"),
		@Forward(name = "transferEnrollments", path = "/manager/transferEnrollments.jsp"),
		@Forward(name = "show", path = "/manager/studentCurricularPlan.jsp") })
@Exceptions(value = {
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException.class, key = "exception.student.does.not.exist", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException.class, key = "student.curricular.plan.already.exists", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
		@ExceptionHandling(type = net.sourceforge.fenixedu.domain.exceptions.DomainException.class, key = "error.enrolmentEvaluation.cannot.be.deleted", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class ManageStudentCurricularPlanDAForOperator extends net.sourceforge.fenixedu.presentationTier.Action.manager.ManageStudentCurricularPlanDA {
}