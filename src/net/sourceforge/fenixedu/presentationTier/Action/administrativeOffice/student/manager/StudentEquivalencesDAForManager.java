package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.manager;

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

@Mapping(module = "manager", path = "/studentEquivalences", attribute = "studentDismissalForm", formBean = "studentDismissalForm", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "visualizeRegistration", path = "/bolonhaStudentEnrolment.do?method=showAllStudentCurricularPlans"),
		@Forward(name = "chooseNotNeedToEnrol", path = "/manager/bolonha/dismissal/chooseEquivalenceNotNeedToEnrol.jsp"),
		@Forward(name = "chooseEquivalents", path = "/manager/bolonha/dismissal/chooseEquivalenceEquivalents.jsp"),
		@Forward(name = "chooseExternalCurricularCourse", path = "studentDismissal.chooseExternalCurricularCourse"),
		@Forward(name = "confirmCreateDismissals", path = "/manager/bolonha/dismissal/confirmCreateEquivalence.jsp"),
		@Forward(name = "manage", path = "/manager/bolonha/dismissal/managementDismissals.jsp"),
		@Forward(name = "prepareCreateExternalEnrolment", path = "/manager/bolonha/dismissal/createExternalEnrolment.jsp"),
		@Forward(name = "chooseDismissalEnrolments", path = "/manager/bolonha/dismissal/chooseEquivalenceEnrolments.jsp") })
public class StudentEquivalencesDAForManager extends net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.StudentEquivalencesDA {
}