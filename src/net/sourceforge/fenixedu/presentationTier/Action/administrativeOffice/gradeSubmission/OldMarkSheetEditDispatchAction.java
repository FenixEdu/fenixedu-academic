package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.gradeSubmission;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.MarkSheetType;
import net.sourceforge.fenixedu.domain.Teacher;

import org.apache.struts.action.ActionMessages;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/editOldMarkSheet", module = "academicAdminOffice", formBean = "markSheetManagementForm", input = "/markSheetManagement.do?method=prepareSearchMarkSheet")
@Forwards( { @Forward(name = "editMarkSheet", path = "/academicAdminOffice/gradeSubmission/oldMarkSheets/editMarkSheet.jsp"),
	@Forward(name = "searchMarkSheetFilled", path = "/oldMarkSheetManagement.do?method=prepareSearchMarkSheetFilled") })
// @Forward(name = "editArchiveInformation", path =
// "/academicAdminOffice/gradeSubmission/editMarkSheetArchiveInformation.jsp")
// })
public class OldMarkSheetEditDispatchAction extends MarkSheetEditDispatchAction {

    @Override
    protected void checkIfTeacherIsResponsibleOrCoordinator(CurricularCourse curricularCourse,
	    ExecutionSemester executionSemester, String teacherId, Teacher teacher, HttpServletRequest request,
	    MarkSheetType markSheetType, ActionMessages actionMessages) {

    }

    @Override
    protected void checkIfEvaluationDateIsInExamsPeriod(DegreeCurricularPlan degreeCurricularPlan,
	    ExecutionSemester executionSemester, Date evaluationDate, MarkSheetType markSheetType, HttpServletRequest request,
	    ActionMessages actionMessages) {
    }

}
