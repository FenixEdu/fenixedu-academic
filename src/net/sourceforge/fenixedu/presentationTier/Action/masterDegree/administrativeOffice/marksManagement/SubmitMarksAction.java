package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author Fernanda Quitério 01/07/2003
 * 
 */
public class SubmitMarksAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Integer curricularCourseCode = new Integer(MarksManagementDispatchAction.getFromRequest(
                "courseId", request));
        MarksManagementDispatchAction.getFromRequest("objectCode", request);
        MarksManagementDispatchAction.getFromRequest("degreeId", request);

        // Get students List
        Object args[] = { curricularCourseCode, null };
        IUserView userView = SessionUtils.getUserView(request);
        InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = null;
        try {
            infoSiteEnrolmentEvaluation = (InfoSiteEnrolmentEvaluation) ServiceManagerServiceFactory
                    .executeService(userView, "ReadStudentsAndMarksByCurricularCourse", args);
        } catch (NonExistingServiceException e) {
            sendErrors(request, "nonExisting", "message.masterDegree.notfound.students");
            return mapping.findForward("ShowMarksManagementMenu");
        } catch (ExistingServiceException e) {
            sendErrors(request, "existing", "message.masterDegree.evaluation.alreadyConfirmed");
            return mapping.findForward("ShowMarksManagementMenu");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        Collections.sort(infoSiteEnrolmentEvaluation.getEnrolmentEvaluations(), new BeanComparator(
                "infoEnrolment.infoStudentCurricularPlan.infoStudent.number"));

        setForm(form, infoSiteEnrolmentEvaluation);

        request.setAttribute("infoSiteEnrolmentEvaluation", infoSiteEnrolmentEvaluation);

        return mapping.findForward("MarksSubmission");
    }

    private void setForm(ActionForm form, InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation) {
        DynaValidatorForm submitMarksForm = (DynaValidatorForm) form;
        InfoTeacher infoTeacher = infoSiteEnrolmentEvaluation.getInfoTeacher();
        if (infoTeacher != null && infoTeacher.getTeacherNumber() != null) {
            // fill in teacher number in case it exists
            submitMarksForm.set("teacherNumber", infoTeacher.getTeacherNumber().toString());
        }
        if (infoSiteEnrolmentEvaluation.getLastEvaluationDate() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.setLenient(false);
            calendar.setTime(infoSiteEnrolmentEvaluation.getLastEvaluationDate());

            submitMarksForm.set("day", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            submitMarksForm.set("month", String.valueOf(calendar.get(Calendar.MONTH) + 1));
            submitMarksForm.set("year", String.valueOf(calendar.get(Calendar.YEAR)));
        }
    }

    private void sendErrors(HttpServletRequest request, String arg0, String arg1) {
        ActionErrors errors = new ActionErrors();
        errors.add(arg0, new ActionError(arg1));
        saveErrors(request, errors);
    }

    public ActionForward submit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        MarksManagementDispatchAction.getFromRequest("courseId", request);
        MarksManagementDispatchAction.getFromRequest("objectCode", request);
        MarksManagementDispatchAction.getFromRequest("degreeId", request);

        List evaluations = new ArrayList();
        Integer sizeList = new Integer(MarksManagementDispatchAction.getFromRequest("sizeList", request));

        // transform form into list with student's code and students's grade
        for (int i = 0; i < sizeList.intValue(); i++) {
            InfoEnrolmentEvaluation infoEnrolmentEvaluation = getFinalEvaluation(request, i);
            if (infoEnrolmentEvaluation != null) {
                evaluations.add(infoEnrolmentEvaluation);
            }
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        // get information from form
        DynaValidatorForm marksForm = (DynaValidatorForm) form;
        Integer teacherNumber = new Integer((String) marksForm.get("teacherNumber"));
        int year = new Integer((String) marksForm.get("year")).intValue();
        int month = new Integer((String) marksForm.get("month")).intValue();
        int day = new Integer((String) marksForm.get("day")).intValue();
        calendar.set(year, month - 1, day);
        Date evaluationDate = calendar.getTime();

        if (evaluationDate.after(Calendar.getInstance().getTime())) {
            sendErrors(request, "nonExisting", "message.masterDegree.evaluation.invalidDate");
            return mapping.findForward("ShowMarksManagementMenu");
        }

        // Insert final evaluation
        IUserView userView = SessionUtils.getUserView(request);
        Object args[] = { evaluations, teacherNumber, evaluationDate, userView };
        List evaluationsWithError = null;
        try {
            evaluationsWithError = (List) ServiceManagerServiceFactory.executeService(userView,
                    "InsertStudentsFinalEvaluation", args);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException(teacherNumber.toString(), e);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e);
        }

        // check for invalid marks
        ActionErrors actionErrors = null;
        actionErrors = checkForErrors(evaluationsWithError);
        if (actionErrors != null) {
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        return mapping.findForward("ShowMarksManagementMenu");
    }

    private InfoEnrolmentEvaluation getFinalEvaluation(HttpServletRequest request, int index) {
        Integer studentCode = null;
        Integer enrolmentCode = null;
        Integer evaluationId = null;
        String evaluation = request.getParameter("enrolmentEvaluation[" + index + "].grade");
        if (request.getParameter("enrolmentEvaluation[" + index + "].studentCode") != null) {
            studentCode = Integer.valueOf(request.getParameter("enrolmentEvaluation[" + index
                    + "].studentCode"));

            enrolmentCode = Integer.valueOf(request.getParameter("enrolmentEvaluation[" + index
                    + "].enrolmentCode"));

            evaluationId = Integer.valueOf(request.getParameter("enrolmentEvaluation[" + index
                    + "].idInternal"));

        }
        if (studentCode != null) {

            // enrolment evaluation with only student code and mark and
            // enrolment code

            final Enrolment enrolment = (Enrolment) RootDomainObject.getInstance()
                    .readCurriculumModuleByOID(enrolmentCode);

            InfoStudentCurricularPlan infoStudentCurricularPlan = new InfoStudentCurricularPlan(
                    enrolment.getStudentCurricularPlan());

            InfoEnrolment infoEnrolment = new InfoEnrolment();
            infoEnrolment.setIdInternal(enrolmentCode);
            infoEnrolment.setInfoStudentCurricularPlan(infoStudentCurricularPlan);

            InfoEnrolmentEvaluation infoEnrolmentEvaluation = new InfoEnrolmentEvaluation();
            infoEnrolmentEvaluation.setInfoEnrolment(infoEnrolment);

            infoEnrolmentEvaluation.setGrade(evaluation);
            infoEnrolmentEvaluation.setIdInternal(evaluationId);
            return infoEnrolmentEvaluation;
        }
        return null;
    }

    private ActionErrors checkForErrors(List evaluationsWithError) {
        ActionErrors actionErrors = null;

        if (evaluationsWithError != null && evaluationsWithError.size() > 0) {
            actionErrors = new ActionErrors();
            Iterator iterator = evaluationsWithError.listIterator();
            while (iterator.hasNext()) {
                InfoEnrolmentEvaluation infoEnrolmentEvaluation = (InfoEnrolmentEvaluation) iterator
                        .next();

                actionErrors.add("invalidGrade", new ActionError("errors.invalidMark",
                        infoEnrolmentEvaluation.getGrade(), String.valueOf(infoEnrolmentEvaluation
                                .getInfoEnrolment().getInfoStudentCurricularPlan().getInfoStudent()
                                .getNumber().intValue())));
            }
        }
        return actionErrors;
    }
}