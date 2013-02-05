package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.credits;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.credits.CreditLineDTO;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ShowTeacherCreditsDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(module = "scientificCouncil", path = "/showFullTeacherCreditsSheet", attribute = "teacherCreditsSheetForm",
        formBean = "teacherCreditsSheetForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "show-teacher-credits", path = "/scientificCouncil/credits/showTeacherCredits.jsp"),
        @Forward(name = "teacher-not-found", path = "/showAllTeacherCreditsResume.do?method=showTeacherCreditsResume&page=0") })
public class ScientificCouncilShowTeacherCreditsDispatchAction extends ShowTeacherCreditsDispatchAction {

    public ActionForward showTeacherCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException,
            ParseException {

        InfoTeacherCredits infoTeacherCredits = new InfoTeacherCredits(form, request);
        Teacher teacher = infoTeacherCredits.getTeacher();
        ExecutionSemester executionSemester = infoTeacherCredits.getExecutionSemester();

        if (teacher == null) {
            request.setAttribute("teacherNotFound", "teacherNotFound");
            return mapping.findForward("teacher-not-found");
        }
        if (teacher.hasTeacherCredits(executionSemester)
                && teacher.getTeacherCredits(executionSemester).getTeacherCreditsState().isCloseState()) {
            request.setAttribute("simulateCalc", "true");
        }
        getAllTeacherCredits(request, executionSemester, teacher);
        return mapping.findForward("show-teacher-credits");
    }

    public ActionForward simulateCalcTeacherCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws ParseException {
        InfoTeacherCredits infoTeacherCredits = new InfoTeacherCredits(form, request);
        Teacher teacher = infoTeacherCredits.getTeacher();
        ExecutionSemester executionSemester = infoTeacherCredits.getExecutionSemester();
        getRequestAllTeacherCredits(request, executionSemester, teacher);
        CreditLineDTO creditLineDTO = simulateCalcCreditLine(teacher, executionSemester);
        request.setAttribute("simulateCalc", "false");
        request.setAttribute("creditLineDTO", creditLineDTO);
        return mapping.findForward("show-teacher-credits");
    }

    public ActionForward editTeacherCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException,
            ParseException {
        InfoTeacherCredits infoTeacherCredits = new InfoTeacherCredits(form, request);
        Teacher teacher = infoTeacherCredits.getTeacher();
        ExecutionSemester executionSemester = infoTeacherCredits.getExecutionSemester();
        teacher.getTeacherCredits(executionSemester).editTeacherCredits(executionSemester);
        getAllTeacherCredits(request, executionSemester, teacher);
        request.setAttribute("simulateCalc", "true");
        return mapping.findForward("show-teacher-credits");
    }

    private ExecutionSemester getExecutionSemesterFromRequestOrForm(HttpServletRequest request, DynaActionForm teacherCreditsForm) {
        ExecutionSemester executionSemester =
                rootDomainObject.readExecutionSemesterByOID((Integer) teacherCreditsForm.get("executionPeriodId"));
        if (executionSemester != null) {
            return executionSemester;
        }
        return rootDomainObject.readExecutionSemesterByOID(getIntegerFromRequest(request, "executionPeriodId"));
    }

    private class InfoTeacherCredits {
        private final Teacher teacher;
        private final ExecutionSemester executionSemester;

        public InfoTeacherCredits(ActionForm form, HttpServletRequest request) {
            DynaActionForm teacherCreditsForm = (DynaActionForm) form;
            executionSemester = getExecutionSemesterFromRequestOrForm(request, teacherCreditsForm);
            teacher = AbstractDomainObject.fromExternalId((String) teacherCreditsForm.get("teacherId"));
        }

        public Teacher getTeacher() {
            return teacher;
        }

        public ExecutionSemester getExecutionSemester() {
            return executionSemester;
        }
    }
}
