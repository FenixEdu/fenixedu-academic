package ServidorApresentacao.Action.masterDegree.administrativeOffice.marksManagement;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoEnrolment;
import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.InfoSiteEnrolmentEvaluation;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.InfoTeacher;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.Data;
import Util.EnrolmentEvaluationType;
import Util.FormataData;

/**
 * 
 * @author Fernanda Quitério
 * 30/06/2003
 * 
 */

public class ChangeMarkDispatchAction extends DispatchAction
{
    InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = null;

    public ActionForward prepareChangeMark(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        HttpSession session = request.getSession(false);

        String executionYear = getFromRequest("executionYear", request);
        String degree = getFromRequest("degree", request);
        String curricularCourse = getFromRequest("curricularCourse", request);
        Integer curricularCourseCode = new Integer(getFromRequest("curricularCourseCode", request));

        request.setAttribute("executionYear", executionYear);
        request.setAttribute("curricularCourseCode", curricularCourseCode);
        request.setAttribute("courseID", curricularCourseCode);
        request.setAttribute("curricularCourse", curricularCourse);
        request.setAttribute("degree", degree);
        request.setAttribute("jspTitle", getFromRequest("jspTitle", request));

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Object args[] = { userView, curricularCourseCode, executionYear };
        GestorServicos serviceManager = GestorServicos.manager();
        List listEnrolmentEvaluation = null;
        try
        {
            listEnrolmentEvaluation =
                (List) serviceManager.executar(userView, "ReadStudentMarksListByCurricularCourse", args);
        } catch (NotAuthorizedException e)
        {
            return mapping.findForward("NotAuthorized");
        } catch (NonExistingServiceException e)
        {
            ActionErrors errors = new ActionErrors();
            errors.add("nonExisting", new ActionError("error.exception.noStudents"));
            saveErrors(request, errors);
            return mapping.findForward("NoStudents");
        }

        if (listEnrolmentEvaluation.size() == 0)
        {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("StudentNotEnroled", new ActionError("error.students.Mark.NotAvailable"));
            saveErrors(request, actionErrors);
            return mapping.findForward("NoStudents");
        }

        return mapping.findForward("editStudentNumber");
    }

    public ActionForward chooseStudentMarks(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        HttpSession session = request.getSession(false);
        String executionYear = getFromRequest("executionYear", request);
        String degree = getFromRequest("degree", request);
        String curricularCourse = getFromRequest("curricularCourse", request);
        Integer curricularCourseCode = Integer.valueOf(getFromRequest("curricularCourseCode", request));
        Integer studentNumber = null;
        request.setAttribute("executionYear", executionYear);
        request.setAttribute("degree", degree);
        request.setAttribute("curricularCourse", curricularCourse);
        request.setAttribute("curricularCourseCode", curricularCourseCode);
        request.setAttribute("courseID", curricularCourseCode);
        request.setAttribute("jspTitle", getFromRequest("jspTitle", request));
        try
        {
            studentNumber = Integer.valueOf(getFromRequest("studentNumber", request));
        } catch (NumberFormatException e)
        {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("StudentNumberRequired", new ActionError("error.studentNumber.required"));
            saveErrors(request, actionErrors);
            return mapping.findForward("editStudentNumber");

        }
        // Get mark student List
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        GestorServicos serviceManager = GestorServicos.manager();

        List infoSiteEnrolmentEvaluations = null;

        request.setAttribute("executionYear", executionYear);
        request.setAttribute("degree", degree);
        request.setAttribute("curricularCourse", curricularCourse);
        request.setAttribute("curricularCourseCode", curricularCourseCode);
        request.setAttribute("courseID", curricularCourseCode);
        request.setAttribute("jspTitle", getFromRequest("jspTitle", request));
        request.setAttribute("studentNumber", studentNumber);

        if (getFromRequest("showMarks", request) != null)
        {
            request.setAttribute("showMarks", "showMarks");
        }

        try
        {
            Object args[] = { curricularCourseCode, studentNumber, executionYear };
            infoSiteEnrolmentEvaluations =
                (List) serviceManager.executar(userView, "ReadStudentsAndMarksByCurricularCourse", args);
        } catch (ExistingServiceException e)
        {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("StudentNotExist", new ActionError("error.student.notExist"));
            saveErrors(request, actionErrors);
            return mapping.findForward("editStudentNumber");

        }

        if (infoSiteEnrolmentEvaluations.size() == 0)
        {

            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add(
                "StudentNotEnroled",
                new ActionError(
                    "error.student.Enrolment.invalid",
                    String.valueOf(studentNumber),
                    curricularCourse));
            saveErrors(request, actionErrors);
            return mapping.findForward("chooseCurricularCourse");
        }

        if (((InfoSiteEnrolmentEvaluation) infoSiteEnrolmentEvaluations.get(0))
            .getEnrolmentEvaluations()
            .size()
            == 0)
        {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add(
                "StudentNotEnroled",
                new ActionError("error.student.Mark.NotAvailable", String.valueOf(studentNumber)));
            saveErrors(request, actionErrors);
            if (getFromRequest("showMarks", request) != null)
            {
                return mapping.findForward("showMarkNotAvailable");
            }
            return mapping.findForward("editStudentNumber");
        }

        request.setAttribute("studentNumber", studentNumber);
        request.setAttribute("jspTitle", getFromRequest("jspTitle", request));
        request.setAttribute("executionYear", executionYear);
        request.setAttribute("degree", degree);
        request.setAttribute("curricularCourse", curricularCourse);
        request.setAttribute("curricularCourseCode", curricularCourseCode);
        request.setAttribute("courseID", curricularCourseCode);
        request.setAttribute(
            "name",
            (
                (InfoEnrolmentEvaluation) (((InfoSiteEnrolmentEvaluation) infoSiteEnrolmentEvaluations
                    .get(0))
                .getEnrolmentEvaluations())
                .get(0))
                .getInfoEnrolment()
                .getInfoStudentCurricularPlan()
                .getInfoStudent()
                .getInfoPerson()
                .getNome());

        InfoEnrolment infoEnrolmentTemp =
            ((InfoEnrolmentEvaluation) ((InfoSiteEnrolmentEvaluation) infoSiteEnrolmentEvaluations
                .get(0))
                .getEnrolmentEvaluations()
                .get(0))
                .getInfoEnrolment();

        InfoEnrolmentEvaluation newEnrolmentEvaluation = null;
        try
        {
            Object args[] = { userView, infoEnrolmentTemp.getIdInternal()};
            newEnrolmentEvaluation =
                (InfoEnrolmentEvaluation) serviceManager.executar(
                    userView,
                    "ReadInfoEnrolmentEvaluationByEvaluationOID",
                    args);
        } catch (ExistingServiceException e)
        {
            throw new ExistingActionException(e);
        }

        Locale locale = new Locale("pt", "PT");

        String examDay =
            FormataData.getDay(
                DateFormat.getDateInstance(DateFormat.SHORT, locale).format(
                    newEnrolmentEvaluation.getExamDate()));
        String examMonth =
            FormataData.getMonth(
                DateFormat.getDateInstance(DateFormat.SHORT, locale).format(
                    newEnrolmentEvaluation.getExamDate()));
        String examYear =
            FormataData.getYear(
                DateFormat.getDateInstance(DateFormat.SHORT, locale).format(
                    newEnrolmentEvaluation.getExamDate()));
        String gradeAvailableDay =
            FormataData.getDay(
                DateFormat.getDateInstance(DateFormat.SHORT, locale).format(
                    newEnrolmentEvaluation.getGradeAvailableDate()));
        String gradeAvailableMonth =
            FormataData.getMonth(
                DateFormat.getDateInstance(DateFormat.SHORT, locale).format(
                    newEnrolmentEvaluation.getGradeAvailableDate()));
        String gradeAvailableYear =
            FormataData.getYear(
                DateFormat.getDateInstance(DateFormat.SHORT, locale).format(
                    newEnrolmentEvaluation.getGradeAvailableDate()));

        DynaActionForm studentNumberForm = (DynaActionForm) form;

        studentNumberForm.set("examDateYear", examYear);
        int month = Integer.valueOf(examMonth).intValue() - 1;
        studentNumberForm.set("examDateMonth", String.valueOf(month));
        studentNumberForm.set("examDateDay", new Integer(Integer.parseInt(examDay)).toString());

        studentNumberForm.set("gradeAvailableDateYear", gradeAvailableYear);
        month = Integer.valueOf(gradeAvailableMonth).intValue() - 1;
        studentNumberForm.set("gradeAvailableDateMonth", String.valueOf(month));
        studentNumberForm.set(
            "gradeAvailableDateDay",
            new Integer(Integer.parseInt(gradeAvailableDay)).toString());
        studentNumberForm.set("grade", newEnrolmentEvaluation.getGrade());
        studentNumberForm.set("observation", newEnrolmentEvaluation.getObservation());
        studentNumberForm.set(
            "enrolmentEvaluationType",
            String.valueOf(newEnrolmentEvaluation.getEnrolmentEvaluationType().getType()));
        studentNumberForm.set("studentNumber", studentNumber);
        //		responsible teacher
        InfoTeacher infoTeacher = null;

        try
        {
            Object args[] = { newEnrolmentEvaluation.getInfoPersonResponsibleForGrade().getUsername()};
            infoTeacher = (InfoTeacher) serviceManager.executar(userView, "ReadTeacherByUserName", args);
        } catch (ExistingServiceException e)
        {
            throw new ExistingActionException(e);
        }
        studentNumberForm.set("teacherNumber", String.valueOf(infoTeacher.getTeacherNumber()));
        request.setAttribute(
            SessionConstants.ENROLMENT_EVALUATION_TYPE_LIST,
            new EnrolmentEvaluationType().toArrayList());
        request.setAttribute(SessionConstants.MONTH_DAYS_KEY, Data.getMonthDays());
        request.setAttribute(SessionConstants.MONTH_LIST_KEY, Data.getMonths());
        request.setAttribute(SessionConstants.YEARS_KEY, Data.getYears());

        request.setAttribute("lastEnrolmentEavluation", newEnrolmentEvaluation);
        request.setAttribute("infoSiteEnrolmentEvaluation", infoSiteEnrolmentEvaluations);
        return mapping.findForward("studentMarks");
    }

    public ActionForward studentMarkChanged(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        HttpSession session = request.getSession(false);

        DynaActionForm studentNumberForm = (DynaActionForm) form;
        // get input
        String executionYear = getFromRequest("executionYear", request);
        String degree = getFromRequest("degree", request);
        String curricularCourse = getFromRequest("curricularCourse", request);
        Integer curricularCourseCode = new Integer(getFromRequest("curricularCourseCode", request));

        request.setAttribute("executionYear", executionYear);
        request.setAttribute("degree", degree);
        request.setAttribute("curricularCourse", curricularCourse);
        request.setAttribute("curricularCourseCode", curricularCourseCode);
        request.setAttribute("courseID", curricularCourseCode);
        request.setAttribute("jspTitle", getFromRequest("jspTitle", request));
        request.setAttribute("studentNumber", getFromRequest("studentNumber", request));

        Integer enrolmentEvaluationCode = Integer.valueOf(getFromRequest("teacherCode", request));
        String grade = getFromRequest("grade", request);

        Integer evaluation = Integer.valueOf(getFromRequest("enrolmentEvaluationType", request));

        EnrolmentEvaluationType enrolmentEvaluationType = new EnrolmentEvaluationType(evaluation);

        Integer teacherNumber = null;

        try
        {
            teacherNumber = Integer.valueOf(getFromRequest("teacherNumber", request));
        } catch (NumberFormatException e)
        {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("TeacharNumberRequired", new ActionError("error.teacherNumber.required"));
            saveErrors(request, actionErrors);
            return mapping.findForward("chooseStudentMarks");

        }

        String observation = getFromRequest("observation", request);
        Integer studentNumber = Integer.valueOf(getFromRequest("studentNumber", request));
        Calendar examDate = Calendar.getInstance();

        String examMonth = getFromRequest("examDateMonth", request);
        String examYear = getFromRequest("examDateYear", request);
        String examDay = getFromRequest("examDateDay", request);

        String gradeAvailableDateMonth = getFromRequest("gradeAvailableDateMonth", request);
        String gradeAvailableDateYear = getFromRequest("gradeAvailableDateYear", request);
        String gradeAvailableDateDay = getFromRequest("gradeAvailableDateDay", request);
        Integer day = null;
        Integer month = null;
        Integer year = null;

        InfoEnrolmentEvaluation infoEnrolmentEvaluation = new InfoEnrolmentEvaluation();
        InfoTeacher infoTeacher = new InfoTeacher();

        if ((examDay == null)
            || (examMonth == null)
            || (examYear == null)
            || (examDay.length() == 0)
            || (examMonth.length() == 0)
            || (examYear.length() == 0))
        {
            //				infoEnrolmentEvaluation.setExamDate(null);
            throw new FenixActionException("error.data.exame.inválida");
        } else
        {
            day = new Integer((String) studentNumberForm.get("examDateDay"));
            month = new Integer((String) studentNumberForm.get("examDateMonth"));
            year = new Integer((String) studentNumberForm.get("examDateYear"));

            examDate.set(year.intValue(), month.intValue(), day.intValue());
            infoEnrolmentEvaluation.setExamDate(examDate.getTime());
        }

        day = null;
        month = null;
        year = null;
        examDate = Calendar.getInstance();

        if ((gradeAvailableDateDay == null)
            || (gradeAvailableDateMonth == null)
            || (gradeAvailableDateYear == null)
            || (gradeAvailableDateDay.length() == 0)
            || (gradeAvailableDateMonth.length() == 0)
            || (gradeAvailableDateYear.length() == 0))
        {
            //				infoEnrolmentEvaluation.setGradeAvailableDate(null);
            throw new FenixActionException("error.data.lançamento.inválida");
        } else
        {
            day = new Integer((String) studentNumberForm.get("gradeAvailableDateDay"));
            month = new Integer((String) studentNumberForm.get("gradeAvailableDateMonth"));
            year = new Integer((String) studentNumberForm.get("gradeAvailableDateYear"));

            examDate.set(year.intValue(), month.intValue(), day.intValue());
            infoEnrolmentEvaluation.setGradeAvailableDate(examDate.getTime());
        }
        infoTeacher.setTeacherNumber(teacherNumber);

        InfoStudent infoStudent = new InfoStudent();
        infoStudent.setNumber(studentNumber);

        InfoStudentCurricularPlan infoStudentCurricularPlan = new InfoStudentCurricularPlan();
        infoStudentCurricularPlan.setInfoStudent(infoStudent);

        InfoEnrolment infoEnrolment = new InfoEnrolment();
        infoEnrolment.setInfoStudentCurricularPlan(infoStudentCurricularPlan);

        infoEnrolmentEvaluation.setEnrolmentEvaluationType(enrolmentEvaluationType);

        infoEnrolmentEvaluation.setGrade(grade);
        //			infoEnrolmentEvaluation.setExamDate(examDate);
        //			infoEnrolmentEvaluation.setGradeAvailableDate(gradeAvailableDate);
        infoEnrolmentEvaluation.setObservation(observation);
        infoEnrolmentEvaluation.setInfoEnrolment(infoEnrolment);

        request.setAttribute("executionYear", executionYear);
        request.setAttribute("degree", degree);
        request.setAttribute("curricularCourse", curricularCourse);
        request.setAttribute("curricularCourseCode", curricularCourseCode);
        request.setAttribute("courseID", curricularCourseCode);
        request.setAttribute("studentNumber", studentNumber);
        request.setAttribute("jspTitle", getFromRequest("jspTitle", request));

        List evaluationsWithError = null;
        try
        {
            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
            GestorServicos serviceManager = GestorServicos.manager();
            Object args[] =
                {
                    curricularCourseCode,
                    enrolmentEvaluationCode,
                    infoEnrolmentEvaluation,
                    infoTeacher.getTeacherNumber(),
                    userView };
            evaluationsWithError =
                (List) serviceManager.executar(userView, "AlterStudentEnrolmentEvaluation", args);
        } catch (NonExistingServiceException e)
        {
            throw new NonExistingActionException(teacherNumber.toString(), e);
        }

        //			check for invalid marks
        ActionErrors actionErrors = null;
        actionErrors = checkForErrors(evaluationsWithError);
        if (actionErrors != null)
        {

            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        request.setAttribute("Label.MarkChange", "Registo  Alterado");
        request.setAttribute("executionYear", executionYear);
        request.setAttribute("degree", degree);
        request.setAttribute("curricularCourse", curricularCourse);
        request.setAttribute("curricularCourseCode", curricularCourseCode);
        request.setAttribute("courseID", curricularCourseCode);
        request.setAttribute("studentNumber", studentNumber);
        request.setAttribute("jspTitle", getFromRequest("jspTitle", request));

        return mapping.findForward("changeStudentMark");

    }

    private String getFromRequest(String parameter, HttpServletRequest request)
    {
        String parameterString = request.getParameter(parameter);
        if (parameterString == null)
        {
            parameterString = (String) request.getAttribute(parameter);
        }
        return parameterString;
    }

    private ActionErrors checkForErrors(List evaluationsWithError)
    {
        ActionErrors actionErrors = null;

        if (evaluationsWithError != null && evaluationsWithError.size() > 0)
        {
            actionErrors = new ActionErrors();
            Iterator iterator = evaluationsWithError.listIterator();
            while (iterator.hasNext())
            {
                InfoEnrolmentEvaluation infoEnrolmentEvaluation =
                    (InfoEnrolmentEvaluation) iterator.next();

                actionErrors.add(
                    "invalidGrade",
                    new ActionError(
                        "errors.invalidMark",
                        infoEnrolmentEvaluation.getGrade(),
                        String.valueOf(
                            infoEnrolmentEvaluation
                                .getInfoEnrolment()
                                .getInfoStudentCurricularPlan()
                                .getInfoStudent()
                                .getNumber()
                                .intValue())));
            }
        }
        return actionErrors;
    }
}
