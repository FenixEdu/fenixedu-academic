/*
 * Created on Dec 10, 2004
 */
package ServidorApresentacao.Action.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoForReadStudentsWithAttendsByExecutionCourse;
import DataBeans.TeacherAdministrationSiteView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Andre Fernandes / Joao Brito
 */
public class StudentsWithAttendsByCurricularCourseListAction extends
        DispatchAction
{
    public ActionForward readStudents(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        DynaActionForm formBean = (DynaActionForm)form;
        HttpSession session = request.getSession(false);
        Integer executionCourseID = null;
        List coursesIDs = null;
        Boolean hasEnrollment = null;
        Integer shiftID = null;
        try {
            executionCourseID = new Integer(request.getParameter("objectCode"));
            shiftID = new Integer(request.getParameter("shiftCode"));
        } catch (NumberFormatException ex) {
            //ok, we don't want to view a shift's student list
        }
        
        Integer checkedCoursesIds[] = (Integer[]) formBean.get("coursesIDs");
        hasEnrollment = (Boolean) formBean.get("hasEnrollment");
        
        coursesIDs = new ArrayList();
        for(int i = 0; i < checkedCoursesIds.length; i++){
            if(checkedCoursesIds[i].equals(new Integer(0))){
                coursesIDs=null;
                break;
            }
            else{
                coursesIDs.add(checkedCoursesIds[i]);
            }
        }

        Integer hasEnrollmentInteger = null;
        if(hasEnrollment.booleanValue()) hasEnrollmentInteger=new Integer (1);
        
        
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        Object args[] = { executionCourseID, coursesIDs, hasEnrollmentInteger, shiftID };
        TeacherAdministrationSiteView siteView = null;

        try {
            siteView = (TeacherAdministrationSiteView) ServiceManagerServiceFactory.executeService(
                    userView, "ReadStudentsWithAttendsByExecutionCourse", args);
            
            InfoForReadStudentsWithAttendsByExecutionCourse infoDTO = (InfoForReadStudentsWithAttendsByExecutionCourse) siteView.getComponent();
            
            Collections.sort(infoDTO.getInfoAttends(), new BeanComparator("infoAttends.aluno.number"));

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("objectCode", executionCourseID);
        request.setAttribute("siteView", siteView);

        DynaActionForm studentsByCurricularCourseForm = (DynaActionForm) form;
        String value = (String) studentsByCurricularCourseForm.get("viewPhoto");
        if (value != null && (value.equals("true") || value.equals("yes") || value.equals("on"))) {
            request.setAttribute("viewPhoto", Boolean.TRUE);
        } else {
            request.setAttribute("viewPhoto", Boolean.FALSE);
        }
        return mapping.findForward("success");
    }
    
    public ActionForward prepare(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        HttpSession session = request.getSession(false);
        Integer executionCourseID = null;
        try {
            executionCourseID = new Integer(request.getParameter("objectCode"));
        
        } catch (NumberFormatException ex) {
            //ok, we don't want to view a shift's student list
        }
        
        DynaActionForm formBean = (DynaActionForm)form;
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        
        // all the information, no filtering applied
        Object args[] = { executionCourseID, null, null, null };
        TeacherAdministrationSiteView siteView = null;

        try {
            siteView = (TeacherAdministrationSiteView) ServiceManagerServiceFactory.executeService(
                    userView, "ReadStudentsWithAttendsByExecutionCourse", args);

            InfoForReadStudentsWithAttendsByExecutionCourse infoDTO = (InfoForReadStudentsWithAttendsByExecutionCourse) siteView.getComponent();
                        
            Collections.sort(infoDTO.getInfoAttends(), new BeanComparator("infoAttends.aluno.number"));

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("siteView", siteView);
        request.setAttribute("objectCode", executionCourseID);
        request.setAttribute("viewPhoto", Boolean.FALSE);
        
        List infoDCPs = ((InfoForReadStudentsWithAttendsByExecutionCourse)siteView.getComponent()).getInfoDegreeCurricularPlans();
        Integer checkboxes[] = new Integer[infoDCPs.size() + 1];
        checkboxes[0] = new Integer(0);
        for (int i = 1; i < checkboxes.length; i++){
            checkboxes[i] = ((InfoDegreeCurricularPlan)infoDCPs.get(i-1)).getIdInternal();
        }
        formBean.set("coursesIDs",checkboxes);
        formBean.set("hasEnrollment",Boolean.FALSE);

        return mapping.findForward("success");
    }
}
