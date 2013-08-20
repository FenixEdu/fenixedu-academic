/*
 * Created on 27/Ago/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.GroupStudentEnrolment;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadStudentGroupInformation;
import net.sourceforge.fenixedu.applicationTier.Servico.student.VerifyStudentGroupAtributes;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroup;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author asnr and scpo
 * 
 */
@Mapping(module = "student", path = "/groupStudentEnrolment", attribute = "enroledExecutionCoursesForm",
        formBean = "enroledExecutionCoursesForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "sucess", path = "/student/viewGroupStudentEnrolment_bd.jsp"),
        @Forward(name = "insucess", path = "/viewEnroledExecutionCourses.do?method=prepare"),
        @Forward(name = "viewStudentGroupInformation", path = "/viewStudentGroupInformation.do"),
        @Forward(name = "viewShiftsAndGroups", path = "/viewShiftsAndGroups.do") })
public class GroupStudentEnrolmentDispatchAction extends FenixDispatchAction {

    public ActionForward prepareEnrolment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        IUserView userView = getUserView(request);

        String studentGroupCodeString = request.getParameter("studentGroupCode");

        String shiftCodeString = request.getParameter("shiftCode");
        request.setAttribute("shiftCode", shiftCodeString);

        try {
            VerifyStudentGroupAtributes.run(null, null, studentGroupCodeString, userView.getUtilizador(), new Integer(1));

        } catch (NotAuthorizedException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("errors.noStudentInAttendsSet");
            actionErrors.add("errors.noStudentInAttendsSet", error);
            saveErrors(request, actionErrors);
            return mapping.findForward("insucess");

        } catch (InvalidSituationServiceException e) {
            ActionErrors actionErrors2 = new ActionErrors();
            ActionError error2 = null;
            // Create an ACTION_ERROR
            error2 = new ActionError("errors.existing.groupStudentEnrolment");
            actionErrors2.add("errors.existing.groupStudentEnrolment", error2);
            saveErrors(request, actionErrors2);
            return mapping.findForward("viewStudentGroupInformation");

        } catch (InvalidArgumentsServiceException e) {
            ActionErrors actionErrors1 = new ActionErrors();
            ActionError error1 = null;
            // Create an ACTION_ERROR
            error1 = new ActionError("errors.invalid.insert.groupStudentEnrolment");
            actionErrors1.add("errors.invalid.insert.groupStudentEnrolment", error1);
            saveErrors(request, actionErrors1);
            return mapping.findForward("viewShiftsAndGroups");

        } catch (FenixServiceException e) {
            ActionErrors actionErrors3 = new ActionErrors();
            ActionError error3 = null;
            // Create an ACTION_ERROR
            error3 = new ActionError("error.noGroup");
            actionErrors3.add("error.noGroup", error3);
            saveErrors(request, actionErrors3);
            return mapping.findForward("viewShiftsAndGroups");

        }

        ISiteComponent viewStudentGroup = null;
        try {
            viewStudentGroup = ReadStudentGroupInformation.run(studentGroupCodeString);

        } catch (InvalidSituationServiceException e) {
            ActionErrors actionErrors3 = new ActionErrors();
            ActionError error3 = null;
            // Create an ACTION_ERROR
            error3 = new ActionError("error.noGroup");
            actionErrors3.add("error.noGroup", error3);
            saveErrors(request, actionErrors3);
            return mapping.findForward("viewShiftsAndGroups");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        InfoSiteStudentGroup infoSiteStudentGroup = (InfoSiteStudentGroup) viewStudentGroup;

        request.setAttribute("infoSiteStudentGroup", infoSiteStudentGroup);

        return mapping.findForward("sucess");

    }

    public ActionForward enrolment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        IUserView userView = getUserView(request);

        String studentGroupCodeString = request.getParameter("studentGroupCode");

        try {

            GroupStudentEnrolment.run(studentGroupCodeString, userView.getUtilizador());

        } catch (NotAuthorizedException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("errors.noStudentInAttendsSet");
            actionErrors.add("errors.noStudentInAttendsSet", error);
            saveErrors(request, actionErrors);
            return mapping.findForward("insucess");

        } catch (InvalidSituationServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("errors.existing.groupStudentEnrolment");
            actionErrors.add("errors.existing.groupStudentEnrolment", error);
            saveErrors(request, actionErrors);
            return mapping.findForward("viewStudentGroupInformation");

        } catch (InvalidArgumentsServiceException e) {
            ActionErrors actionErrors1 = new ActionErrors();
            ActionError error1 = null;
            // Create an ACTION_ERROR
            error1 = new ActionError("errors.invalid.insert.groupStudentEnrolment");
            actionErrors1.add("errors.invalid.insert.groupStudentEnrolment", error1);
            saveErrors(request, actionErrors1);
            return mapping.findForward("viewShiftsAndGroups");

        } catch (FenixServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroup");
            actionErrors.add("error.noGroup", error);
            saveErrors(request, actionErrors);
            return mapping.findForward("viewShiftsAndGroups");

        }
        return mapping.findForward("viewStudentGroupInformation");

    }

}