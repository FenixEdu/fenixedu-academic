/*
 * Created on 25/Mar/2003
 *
 * 
 */
package ServidorApresentacao.Action.teacher;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoSite;
import DataBeans.InfoTeacher;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.notAuthorizedServiceDeleteException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.InvalidArgumentsActionException;
import ServidorApresentacao.Action.exceptions.notAuthorizedActionDeleteException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author João Mota
 * 
 *  
 */
public class TeacherManagerDispatchAction extends FenixDispatchAction {

    public ActionForward viewTeachersByProfessorship(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        try {

            HttpSession session = getSession(request);
            session.removeAttribute(SessionConstants.INFO_SECTION);
            UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
            InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
            Object args[] = { infoSite.getInfoExecutionCourse() };
            boolean result = false;
            List teachers = (List) ServiceManagerServiceFactory.executeService(userView,
                    "ReadTeachersByExecutionCourseProfessorship", args);
            if (teachers != null && !teachers.isEmpty()) {
                session.setAttribute(SessionConstants.TEACHERS_LIST, teachers);
            }

            List responsibleTeachers = (List) ServiceManagerServiceFactory.executeService(userView,
                    "ReadTeachersByExecutionCourseResponsibility", args);

            Object[] args1 = { userView.getUtilizador() };
            InfoTeacher teacher = (InfoTeacher) ServiceManagerServiceFactory.executeService(userView,
                    "ReadTeacherByUsername", args1);
            if (responsibleTeachers != null && !responsibleTeachers.isEmpty()
                    && responsibleTeachers.contains(teacher)) {
                result = true;
            }
            session.setAttribute(SessionConstants.IS_RESPONSIBLE, new Boolean(result));

            return mapping.findForward("viewTeachers");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

    }

    public ActionForward removeTeacher(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        HttpSession session = getSession(request);
        session.removeAttribute(SessionConstants.INFO_SECTION);
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
        String teacherNumberString = request.getParameter("teacherNumber");

        Integer teacherNumber = new Integer(teacherNumberString);
        Object args[] = { infoSite.getInfoExecutionCourse(), teacherNumber };
        try {
            ServiceManagerServiceFactory.executeService(userView, "RemoveTeacher", args);

        } catch (notAuthorizedServiceDeleteException e) {
            throw new notAuthorizedActionDeleteException("error.invalidTeacherRemoval");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return viewTeachersByProfessorship(mapping, form, request, response);
    }

    public ActionForward associateTeacher(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        HttpSession session = getSession(request);
        session.removeAttribute(SessionConstants.INFO_SECTION);
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
        DynaActionForm teacherForm = (DynaActionForm) form;

        Integer teacherNumber = new Integer((String) teacherForm.get("teacherNumber"));
        Object args[] = { infoSite.getInfoExecutionCourse(), teacherNumber };
        try {
            ServiceManagerServiceFactory.executeService(userView, "AssociateTeacher", args);

        } catch (ExistingServiceException e) {
            throw new ExistingActionException("A associação do professor número " + teacherNumber, e);
        } catch (InvalidArgumentsServiceException e) {
            throw new InvalidArgumentsActionException("Professor número " + teacherNumber, e);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return viewTeachersByProfessorship(mapping, form, request, response);
    }

    public ActionForward prepareAssociateTeacher(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        return mapping.findForward("associateTeacher");
    }
}