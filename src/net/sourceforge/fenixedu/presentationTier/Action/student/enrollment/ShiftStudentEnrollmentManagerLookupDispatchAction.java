/*
 * Created on 10/Fev/2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.student.enrollment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift.DeleteStudentAttendingCourse.AlreadyEnrolledInGroupServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift.DeleteStudentAttendingCourse.AlreadyEnrolledInShiftServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift.DeleteStudentAttendingCourse.AlreadyEnrolledServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift.WriteStudentAttendingCourse.ReachedAttendsLimitServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.commons.TransactionalLookupDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixTransactionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Tânia Pousão
 */
public class ShiftStudentEnrollmentManagerLookupDispatchAction extends TransactionalLookupDispatchAction {
    public ActionForward addCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixTransactionException, FenixFilterException {
        super.validateToken(request, form, mapping, "error.transaction.enrollment");

        checkParameter(request);
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        // Read data from form
        DynaActionForm enrollmentForm = (DynaActionForm) form;
        Integer wantedCourse = (Integer) enrollmentForm.get("wantedCourse");

        Integer studentId = (Integer) enrollmentForm.get("studentId");

        InfoStudent infoStudent = new InfoStudent();
        infoStudent.setIdInternal(studentId);

        // Add course
        Object[] args = { infoStudent, wantedCourse };
        Boolean result = Boolean.FALSE;
        try {
            result = (Boolean) ServiceManagerServiceFactory.executeService(userView,
                    "WriteStudentAttendingCourse", args);
        } catch (NotAuthorizedException exception) {
            errors.add("error", new ActionError("error.attend.curricularCourse.impossibleToEnroll"));
        } catch (ReachedAttendsLimitServiceException exception) {
            errors.add("error", new ActionError("message.maximum.number.curricular.courses.to.enroll",
                    new Integer(8)));
        } catch (FenixServiceException exception) {
            errors.add("error", new ActionError("errors.impossible.operation"));
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        if (result.equals(Boolean.FALSE)) {
            errors.add("error", new ActionError("errors.impossible.operation"));
            saveErrors(request, errors);

            return mapping.getInputForward();
        }

        return mapping.findForward("prepareShiftEnrollment");
    }

    public ActionForward removeCourses(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixTransactionException,
            FenixFilterException {
        super.validateToken(request, form, mapping, "error.transaction.enrollment");

        checkParameter(request);

        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        // Read data from form
        DynaActionForm enrollmentForm = (DynaActionForm) form;
        Integer removedCourse = (Integer) enrollmentForm.get("removedCourse");

        Integer studentId = (Integer) enrollmentForm.get("studentId");

        InfoStudent infoStudent = new InfoStudent();
        infoStudent.setIdInternal(studentId);

        // Remove course
        Object[] args = { infoStudent, removedCourse };
        Boolean result = Boolean.FALSE;
        try {
            result = (Boolean) ServiceManagerServiceFactory.executeService(userView,
                    "DeleteStudentAttendingCourse", args);
        } catch (AlreadyEnrolledInGroupServiceException exception) {
            errors.add("error", new ActionError("errors.student.already.enroled.in.group"));
        } catch (AlreadyEnrolledServiceException exception) {
            errors.add("error", new ActionError("errors.student.already.enroled"));
        } catch (AlreadyEnrolledInShiftServiceException exception) {
            errors.add("error", new ActionError("errors.student.already.enroled.in.shift"));
        } catch (FenixServiceException exception) {
            errors.add("error", new ActionError("errors.impossible.operation"));
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        if (result.equals(Boolean.FALSE)) {
            errors.add("error", new ActionError("errors.impossible.operation"));
            saveErrors(request, errors);

            return mapping.getInputForward();
        }

        return mapping.findForward("prepareShiftEnrollment");
    }

    public ActionForward proceedToShiftEnrolment(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {
        checkParameter(request);

        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        Integer classIdSelected = readClassSelected(request);        
        Integer studentId = Integer.valueOf((String)request.getParameter("studentId"));
        if(studentId == null){
            studentId = (Integer) request.getAttribute("studentId");
        }
        request.setAttribute("studentId", studentId);        
        
        Object[] args = { studentId };
        List infoClassList = (List) ServiceManagerServiceFactory.executeService(userView,
                "ReadClassesByStudentID", args);        
        request.setAttribute("infoClassList", infoClassList);
        
        Integer tempClassId = null;
        if (classIdSelected != null) {
            tempClassId = classIdSelected;            
        } else {
            tempClassId = ((InfoClass) infoClassList.get(0))
                    .getIdInternal();            
        }

        request.setAttribute("classId", tempClassId);
        final Integer classId = tempClassId;

        List selectedClasses = (List) CollectionUtils.select(infoClassList, new Predicate() {

            public boolean evaluate(Object arg0) {
                InfoClass infoClass = (InfoClass) arg0;
                return infoClass.getIdInternal().equals(classId);
            }
        });

        InfoClass infoClass = (InfoClass) selectedClasses.get(0);
        request.setAttribute("infoClass", infoClass);                
        
        Object[] args1 = { userView.getUtilizador(), classId };
        List infoClasslessons = (List) ServiceManagerServiceFactory.executeService(userView,
                "ReadClassTimeTableByStudent", args1);        
        request.setAttribute("infoClasslessons", infoClasslessons);

        Object[] args2 = { userView.getUtilizador() };
        List infoLessons = (List) ServiceManagerServiceFactory.executeService(userView,
                "ReadStudentTimeTable", args2);
        request.setAttribute("infoLessons", infoLessons);

        return mapping.findForward("showShiftsToEnroll");
    }

    private void checkParameter(HttpServletRequest request) {
        String selectCourses = request.getParameter("selectCourses");
        if (selectCourses != null) {
            request.setAttribute("selectCourses", selectCourses);
        }
    }

    private Integer readClassSelected(HttpServletRequest request) {
        String classIdSelectedString = request.getParameter("classId");
        Integer classIdSelected = null;
        if (classIdSelectedString != null) {
            classIdSelected = Integer.valueOf(classIdSelectedString);
        } else {
            classIdSelected = (Integer) request.getAttribute("classId");
        }
        return classIdSelected;
    }       

    public ActionForward exitEnrollment(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("studentFirstPage");
    }

    public ActionForward prepareStartViewWarning(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("prepareEnrollmentViewWarning");
    }

    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("button.addCourse", "addCourses");
        map.put("button.removeCourse", "removeCourses");
        map.put("button.continue.enrolment", "prepareStartViewWarning");
        map.put("button.exit.shift.enrollment", "exitEnrollment");
        map.put("label.class", "proceedToShiftEnrolment");
        map.put("link.shift.enrolement.edit", "proceedToShiftEnrolment");
        map.put("button.clean", "proceedToShiftEnrolment");
        return map;
    }
}