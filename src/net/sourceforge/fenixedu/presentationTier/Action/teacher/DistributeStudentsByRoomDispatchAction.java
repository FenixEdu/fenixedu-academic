/*
 * Created on 11/Jun/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.ExamRoomDistribution;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteExamExecutionCourses;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

/**
 * @author jpvl
 */
public class DistributeStudentsByRoomDispatchAction extends DispatchAction {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm distributionExam = (DynaActionForm) form;

        Integer examCode = new Integer((String) distributionExam.get("evaluationCode"));
        Integer executionCourseCode = new Integer((String) distributionExam.get("objectCode"));

        InfoSiteExamExecutionCourses infoSiteExamExecutionCourses = new InfoSiteExamExecutionCourses();
        Object[] args = { executionCourseCode, new InfoSiteCommon(), infoSiteExamExecutionCourses, null,
                examCode, null };

        TeacherAdministrationSiteView siteView = (TeacherAdministrationSiteView) ServiceUtils
                .executeService(SessionUtils.getUserView(request),
                        "TeacherAdministrationSiteComponentService", args);

        infoSiteExamExecutionCourses = (InfoSiteExamExecutionCourses) siteView.getComponent();
        InfoExam infoExam = infoSiteExamExecutionCourses.getInfoExam();
        List executionCourses = infoSiteExamExecutionCourses.getInfoExecutionCourses();

        Iterator iter = executionCourses.iterator();
        int attendStudents = 0;
        while (iter.hasNext()) {
            InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) iter.next();
            attendStudents += infoExecutionCourse.getNumberOfAttendingStudents().intValue();
        }

        /*
         * List infoExamList = infoSiteExam.getInfoExams(); InfoExam
         * infoExamFromList = null; for (int i = 0; i < infoExamList.size();
         * i++) { infoExamFromList = (InfoExam)infoExamList.get(i); if
         * (infoExamFromList.getIdInternal().equals(examCode)) {
         * Collections.sort( infoExamFromList.getAssociatedRooms(), new
         * ReverseComparator(new BeanComparator("capacidadeExame")));
         * 
         * request.setAttribute("infoExam", infoExamFromList); break; }
         */

        Collections.sort(infoExam.getAssociatedRooms(), new ReverseComparator(new BeanComparator(
                "capacidadeExame")));

        request.setAttribute("infoExam", infoExam);
        request.setAttribute("siteView", siteView);
        request.setAttribute("objectCode", executionCourseCode);
        request.setAttribute("evaluationCode", examCode);
        request.setAttribute("attendStudents", new Integer(attendStudents));

        ActionErrors actionErrors = checkEnrolmentDatesAndExamRooms(request, infoExam);
        if (actionErrors != null && actionErrors.size() > 0) {
            return mapping.getInputForward();
        }

        return mapping.findForward("show-rooms");
    }

    private ActionErrors checkEnrolmentDatesAndExamRooms(HttpServletRequest request,
            InfoExam infoExamFromList) {
        ActionErrors actionErrors = new ActionErrors();

        Calendar examDay = infoExamFromList.getDay();
        Calendar today = Calendar.getInstance();
        Calendar endEnrollmentDay = infoExamFromList.getEnrollmentEndDay();

        if (today.after(examDay)) {
            actionErrors.add("error", new ActionError("error.out.of.period.enrollment.period"));
            saveErrors(request, actionErrors);
            return actionErrors;
        }

        if (endEnrollmentDay != null) {
            Calendar endHourDay = infoExamFromList.getEnrollmentEndTime();

            endEnrollmentDay.set(Calendar.HOUR_OF_DAY, 0);
            endEnrollmentDay.set(Calendar.MINUTE, 0);
            endEnrollmentDay.roll(Calendar.HOUR_OF_DAY, endHourDay.get(Calendar.HOUR_OF_DAY));
            endEnrollmentDay.roll(Calendar.MINUTE, endHourDay.get(Calendar.MINUTE));

            if (today.before(endEnrollmentDay)) {
                actionErrors.add("error", new ActionError("error.out.of.period.enrollment.period"));
                saveErrors(request, actionErrors);
                return actionErrors;
            }
        }

        //		if there are no rooms associated with this evaluation, this is an
        // error
        if (infoExamFromList.getAssociatedRooms() == null
                || infoExamFromList.getAssociatedRooms().size() == 0) {
            actionErrors.add("error", new ActionError("error.no.roms.associated"));
            saveErrors(request, actionErrors);
        }
        return actionErrors;
    }

    public ActionForward distribute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm distributionExam = (DynaActionForm) form;
        Integer examCode = new Integer((String) distributionExam.get("evaluationCode"));
        Integer executionCourseCode = new Integer((String) distributionExam.get("objectCode"));
        Boolean enrolledStudents = new Boolean((String) distributionExam.get("enroll"));

        Integer[] rooms = (Integer[]) distributionExam.get("rooms");
        Object[] args = { executionCourseCode, examCode, Arrays.asList(rooms), Boolean.FALSE,
                enrolledStudents };
        try {
            ServiceUtils.executeService(SessionUtils.getUserView(request), "ExamRoomDistribution", args);

        } catch (InvalidArgumentsServiceException ex) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError actionError = null;
            actionError = new ActionError("error.not.enough.room.space");

            actionErrors.add("error", actionError);
            saveErrors(request, actionErrors);
            return prepare(mapping, form, request, response);

        } catch (FenixServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError actionError = null;
            if (e.getErrorType() == ExamRoomDistribution.OUT_OF_ENROLLMENT_PERIOD) {
                actionError = new ActionError("error.out.of.period.enrollment.period");
            } else {
                throw e;
            }
            actionErrors.add("error", actionError);
            saveErrors(request, actionErrors);
        }
        request.setAttribute("objectCode", executionCourseCode);
        request.setAttribute("evaluationCode", examCode);
        return mapping.findForward("show-distribution");
    }

}