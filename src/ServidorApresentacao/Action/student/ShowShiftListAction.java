package ServidorApresentacao.Action.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoCourseExecutionAndListOfTypeLessonAndInfoShift;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShift;
import DataBeans.InfoShiftEnrolment;
import DataBeans.ShiftKey;
import DataBeans.TypeLessonAndInfoShift;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.TipoAula;

/**
 * @author João Mota
 */

public class ShowShiftListAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        HttpSession session = request.getSession(false);

        String indexes = request.getParameter("index");
        String[] indexij = indexes.split("-");
        Integer indexi = new Integer(indexij[0]);
        Integer indexj = new Integer(indexij[1]);

        InfoShiftEnrolment iSE = (InfoShiftEnrolment) session.getAttribute("infoShiftEnrolment");
        InfoExecutionCourse executionCourse = null;
        TipoAula lessonType = null;

        List infoEnrollmentWithShift = iSE.getInfoEnrolmentWithShift();
        executionCourse = ((InfoCourseExecutionAndListOfTypeLessonAndInfoShift) (infoEnrollmentWithShift
                .get(indexi.intValue()))).getInfoExecutionCourse();

        lessonType = ((TypeLessonAndInfoShift) ((InfoCourseExecutionAndListOfTypeLessonAndInfoShift) (iSE
                .getInfoEnrolmentWithShift().get(indexi.intValue()))).getTypeLessonsAndInfoShifts().get(
                indexj.intValue())).getTypeLesson();

        Object[] argsReadShiftsByType = { executionCourse, lessonType };

        List shiftsList = new ArrayList();

        try {
            shiftsList = (ArrayList) ServiceUtils.executeService(userView,
                    "ReadShiftsByTypeFromExecutionCourse", argsReadShiftsByType);
            if (!shiftsList.isEmpty()) {
                request.setAttribute("shiftsList", shiftsList);
            }

        } catch (Exception e) {
            e.printStackTrace(System.out);
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("unableToReadShifts", new ActionError("errors.unableToReadShifts"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        try {
            List vacancies = new ArrayList();
            Iterator iterator = shiftsList.iterator();
            while (iterator.hasNext()) {
                InfoShift element = (InfoShift) iterator.next();
                Object[] args = { new ShiftKey(element.getNome(), element.getInfoDisciplinaExecucao()) };
                List students = (ArrayList) ServiceUtils.executeService(userView, "LerAlunosDeTurno",
                        args);
                Integer vacancy = element.getLotacao();

                vacancy = new Integer(vacancy.intValue() - students.size());
                vacancies.add(vacancy);
            }

            if (!vacancies.isEmpty()) {
                request.setAttribute("vacancies", vacancies);
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("unableToReadVacancies", new ActionError("errors.unableToReadVacancies"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        return mapping.findForward("viewShiftsList");

    }

}