package net.sourceforge.fenixedu.presentationTier.Action.commons.student.gep;

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

@Mapping(module = "gep", path = "/viewCurriculum", input = "/viewStudentCurriculum.do?method=prepareView&page=0", attribute = "studentCurricularPlanAndEnrollmentsSelectionForm", formBean = "studentCurricularPlanAndEnrollmentsSelectionForm", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "ShowStudentCurriculum", path = "/internationalRelatOffice/displayStudentCurriculum_bd.jsp"),
		@Forward(name = "ShowStudentCurricularPlans", path = "df.page.showStudentCurricularPlans"),
		@Forward(name = "NotAuthorized", path = "df.page.notAuthorized") })
public class CurriculumDispatchActionForGep extends net.sourceforge.fenixedu.presentationTier.Action.commons.student.CurriculumDispatchAction {
}