/*
 * Created on 10/Fev/2004
 */
package ServidorApresentacao.Action.student.enrollment;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoClass;
import DataBeans.InfoShift;
import DataBeans.InfoStudent;
import DataBeans.enrollment.shift.ExecutionCourseShiftEnrollmentDetails;
import DataBeans.enrollment.shift.InfoClassEnrollmentDetails;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.enrollment.shift.DeleteStudentAttendingCourse.AlreadyEnrolledInGroupServiceException;
import ServidorAplicacao.Servico.enrollment.shift.DeleteStudentAttendingCourse.AlreadyEnrolledInShiftServiceException;
import ServidorAplicacao.Servico.enrollment.shift.DeleteStudentAttendingCourse.AlreadyEnrolledServiceException;
import ServidorAplicacao.Servico.enrollment.shift.WriteStudentAttendingCourse.ReachedAttendsLimitServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorApresentacao.Action.commons.TransactionalLookupDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixTransactionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Tânia Pousão
 */
public class ShiftStudentEnrollmentManagerLookupDispatchAction extends
        TransactionalLookupDispatchAction
{
    public ActionForward addCourses(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws FenixTransactionException
    {
        super.validateToken(request, form, mapping,
                "error.transaction.enrollment");

        checkParameter(request);
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session
                .getAttribute(SessionConstants.U_VIEW);

        //Read data from form
        DynaActionForm enrollmentForm = (DynaActionForm) form;
        Integer wantedCourse = (Integer) enrollmentForm.get("wantedCourse");

        Integer studentId = (Integer) enrollmentForm.get("studentId");

        InfoStudent infoStudent = new InfoStudent();
        infoStudent.setIdInternal(studentId);

        //Add course
        Object[] args = {infoStudent, wantedCourse};
        Boolean result = Boolean.FALSE;
        try
        {
            result = (Boolean) ServiceManagerServiceFactory.executeService(
                    userView, "WriteStudentAttendingCourse", args);
        }
        catch (NotAuthorizedException exception)
        {
            errors.add("error", new ActionError(
                    "error.attend.curricularCourse.impossibleToEnroll"));
        }
        catch (ReachedAttendsLimitServiceException exception)
        {
            errors.add("error", new ActionError(
                    "message.maximum.number.curricular.courses.to.enroll",
                    new Integer(8)));
        }
        catch (FenixServiceException exception)
        {
            errors.add("error", new ActionError("errors.impossible.operation"));
        }
        if (!errors.isEmpty())
        {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        if (result.equals(Boolean.FALSE))
        {
            errors.add("error", new ActionError("errors.impossible.operation"));
            saveErrors(request, errors);

            return mapping.getInputForward();
        }

        return mapping.findForward("prepareShiftEnrollment");
    }

    public ActionForward removeCourses(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws FenixTransactionException
    {
        super.validateToken(request, form, mapping,
                "error.transaction.enrollment");

        checkParameter(request);

        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session
                .getAttribute(SessionConstants.U_VIEW);

        //Read data from form
        DynaActionForm enrollmentForm = (DynaActionForm) form;
        Integer removedCourse = (Integer) enrollmentForm.get("removedCourse");

        Integer studentId = (Integer) enrollmentForm.get("studentId");

        InfoStudent infoStudent = new InfoStudent();
        infoStudent.setIdInternal(studentId);

        //Remove course
        Object[] args = {infoStudent, removedCourse};
        Boolean result = Boolean.FALSE;
        try
        {
            result = (Boolean) ServiceManagerServiceFactory.executeService(
                    userView, "DeleteStudentAttendingCourse", args);
        }
        catch (AlreadyEnrolledInGroupServiceException exception)
        {
            errors.add("error", new ActionError(
                    "errors.student.already.enroled.in.group"));
        }
        catch (AlreadyEnrolledServiceException exception)
        {
            errors.add("error", new ActionError(
                    "errors.student.already.enroled"));
        }
        catch (AlreadyEnrolledInShiftServiceException exception)
        {
            errors.add("error", new ActionError(
                    "errors.student.already.enroled.in.shift"));
        }
        catch (FenixServiceException exception)
        {
            errors.add("error", new ActionError("errors.impossible.operation"));
        }
        if (!errors.isEmpty())
        {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        if (result.equals(Boolean.FALSE))
        {
            errors.add("error", new ActionError("errors.impossible.operation"));
            saveErrors(request, errors);

            return mapping.getInputForward();
        }

        return mapping.findForward("prepareShiftEnrollment");
    }

    public ActionForward proceedToShiftEnrolment(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response)
    {
        checkParameter(request);

        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session
                .getAttribute(SessionConstants.U_VIEW);

        Integer classIdSelected = readClassSelected(request);

        //Read data from form
        DynaActionForm enrollmentForm = (DynaActionForm) form;
        Integer studentId = readStudentId(request, enrollmentForm);

        InfoStudent infoStudent = new InfoStudent();
        infoStudent.setIdInternal(studentId);
        
        Object[] args = {infoStudent, classIdSelected};
        InfoClassEnrollmentDetails infoClassEnrollmentDetails = null;
        try
        {
            infoClassEnrollmentDetails = (InfoClassEnrollmentDetails) ServiceManagerServiceFactory
                    .executeService(userView,
                            "ReadClassShiftEnrollmentDetails", args);
        }
        catch (FenixServiceException exception)
        {
            exception.printStackTrace();
            errors.add("error", new ActionError("errors.impossible.operation"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        if (infoClassEnrollmentDetails == null || infoClassEnrollmentDetails.getInfoClassList().size() == 0)
        {
            errors.add("error", new ActionError("errors.impossible.operation"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        HashMap shiftsMap = buildDataToForm(infoClassEnrollmentDetails);
        enrollmentForm.set("shiftMap", shiftsMap);
        enrollmentForm.set("studentId", infoClassEnrollmentDetails
                .getInfoStudent().getIdInternal());
        request.setAttribute("studentId", infoClassEnrollmentDetails
                .getInfoStudent().getIdInternal());

        if (classIdSelected != null)
        {
            request.setAttribute("classId", classIdSelected);
        }
        else
        {
            request.setAttribute("classId",
                    ((InfoClass) infoClassEnrollmentDetails.getInfoClassList()
                            .get(0)).getIdInternal());
        }

        order(infoClassEnrollmentDetails);

        request.setAttribute("infoClassEnrollmentDetails",
                infoClassEnrollmentDetails);

        return mapping.findForward("showShiftsToEnroll");
    }

    private void checkParameter(HttpServletRequest request)
    {
        String selectCourses = request.getParameter("selectCourses");
        if (selectCourses != null)
        {
            request.setAttribute("selectCourses", selectCourses);
        }
    }
    
    private Integer readClassSelected(HttpServletRequest request)
    {
        String classIdSelectedString = request.getParameter("classId");
        Integer classIdSelected = null;
        if (classIdSelectedString != null)
        {
            classIdSelected = Integer.valueOf(classIdSelectedString);
        }
        else
        {
            classIdSelected = (Integer) request.getAttribute("classId");
        }
        return classIdSelected;
    }

    /**
     * @param infoClassEnrollmentDetails
     */
    private void order(InfoClassEnrollmentDetails infoClassEnrollmentDetails)
    {
        Map classExecutionCourseShiftEnrollmentDetailsMap = infoClassEnrollmentDetails
                .getClassExecutionCourseShiftEnrollmentDetailsMap();

        ListIterator iterator = infoClassEnrollmentDetails.getInfoClassList()
                .listIterator();
        while (iterator.hasNext())
        {
            InfoClass infoClass = (InfoClass) iterator.next();

            List executionCourseDetails = (List) classExecutionCourseShiftEnrollmentDetailsMap
                    .get(infoClass.getIdInternal());

            //order execution course list
            if (executionCourseDetails != null)
            {
                Collections.sort(executionCourseDetails, new BeanComparator(
                        "infoExecutionCourse.nome"));

                ListIterator iterator2 = executionCourseDetails.listIterator();
                while (iterator2.hasNext())
                {
                    ExecutionCourseShiftEnrollmentDetails details2 = (ExecutionCourseShiftEnrollmentDetails) iterator2
                            .next();
                    Collections.sort(details2.getShiftEnrollmentDetailsList(),
                            new BeanComparator("infoShift.tipo"));

                }
            }
        }

    }

    /**
     * @param infoClassEnrollmentDetails
     * @return
     */
    private HashMap buildDataToForm(
            InfoClassEnrollmentDetails infoClassEnrollmentDetails)
    {
        HashMap shiftsMap = new HashMap();

        //na lista de turnos e torná-la num Hashmap em que a key do map é
        //idDisciplina concatenada com o tipo do turno (tipo de aula) e o
        // valor é o id do turno
        List infoShifts = infoClassEnrollmentDetails.getInfoShiftEnrolledList();
        ListIterator iterator = infoShifts.listIterator();
        while (iterator.hasNext())
        {
            InfoShift infoShift = (InfoShift) iterator.next();

            String key = infoShift.getInfoDisciplinaExecucao().getIdInternal()
                    + "-" + infoShift.getTipo().toString();
            shiftsMap.put(key, infoShift.getIdInternal());
        }

        return shiftsMap;
    }

    private Integer readStudentId(HttpServletRequest request,
            DynaActionForm enrollmentForm)
    {
        Integer studentId = (Integer) enrollmentForm.get("studentId");
        if (studentId == null)
        {
            String studentIdString = request.getParameter("studentId");
            if (studentIdString != null)
            {
                studentId = Integer.valueOf(request.getParameter("studentId"));
            }
        }
        return studentId;
    }

    public ActionForward exitEnrollment(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
    {
        return mapping.findForward("studentFirstPage");
    }

    protected Map getKeyMethodMap()
    {
        Map map = new HashMap();
        map.put("button.addCourse", "addCourses");
        map.put("button.removeCourse", "removeCourses");
        map.put("button.continue.enrolment", "proceedToShiftEnrolment");
        map.put("button.exit.enrollment", "exitEnrollment");
        map.put("label.class", "proceedToShiftEnrolment");
        map.put("link.shift.enrolement.edit", "proceedToShiftEnrolment");
        map.put("button.clean", "proceedToShiftEnrolment");
        return map;
    }
}
