package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.gradeSubmission;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.MarkSheetType;
import net.sourceforge.fenixedu.domain.Teacher;

import org.apache.struts.action.ActionMessages;

public class OldMarkSheetEditDispatchAction extends MarkSheetEditDispatchAction {

    @Override
    protected void checkIfTeacherIsResponsibleOrCoordinator(CurricularCourse curricularCourse,
	    ExecutionSemester executionSemester, Integer teacherNumber, Teacher teacher, HttpServletRequest request,
	    MarkSheetType markSheetType, ActionMessages actionMessages) {

    }

    @Override
    protected void checkIfEvaluationDateIsInExamsPeriod(DegreeCurricularPlan degreeCurricularPlan,
	    ExecutionSemester executionSemester, Date evaluationDate, MarkSheetType markSheetType, HttpServletRequest request,
	    ActionMessages actionMessages) {
    }

}
