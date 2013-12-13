/*
 * Created on 27/Ago/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadStudentGroupInformation;
import net.sourceforge.fenixedu.applicationTier.Servico.student.UnEnrollStudentInGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.student.VerifyStudentGroupAtributes;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
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
@Mapping(module = "student", path = "/removeGroupEnrolment", attribute = "enroledExecutionCoursesForm",
        formBean = "enroledExecutionCoursesForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "sucess", path = "/student/removeStudentInGroup.jsp"),
        @Forward(name = "insucess", path = "/viewEnroledExecutionCourses.do?method=prepare"),
        @Forward(name = "viewStudentGroupInformation", path = "/viewStudentGroupInformation.do"),
        @Forward(name = "viewShiftsAndGroups", path = "/viewShiftsAndGroups.do") })
public class UnEnrollStudentInGroupDispatchAction extends FenixDispatchAction {

    public ActionForward prepareRemove(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        User userView = getUserView(request);

        String studentGroupCodeString = request.getParameter("studentGroupCode");

        String shiftCodeString = request.getParameter("shiftCode");
        request.setAttribute("shiftCode", shiftCodeString);

        try {
            VerifyStudentGroupAtributes.run(null, null, studentGroupCodeString, userView.getUsername(), new Integer(3));

        } catch (NotAuthorizedException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("errors.noStudentInAttendsSetToDelete");
            actionErrors.add("errors.noStudentInAttendsSetToDelete", error);
            saveErrors(request, actionErrors);
            return mapping.findForward("insucess");

        } catch (InvalidSituationServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            // Create an ACTION_ERROR
            error = new ActionError("errors.removeEnrolment.studentNotEnroled");
            actionErrors.add("errors.removeEnrolment.studentNotEnroled", error);
            saveErrors(request, actionErrors);
            return mapping.findForward("viewStudentGroupInformation");

        } catch (InvalidArgumentsServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            // Create an ACTION_ERROR
            error = new ActionError("errors.removeEnrolment.minimumNumberOfElements");
            actionErrors.add("errors.removeEnrolment.minimumNumberOfElements", error);
            saveErrors(request, actionErrors);
            return mapping.findForward("viewStudentGroupInformation");

        } catch (FenixServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroup");
            actionErrors.add("error.noGroup", error);
            saveErrors(request, actionErrors);
            return mapping.findForward("viewShiftsAndGroups");
        }

        ISiteComponent viewStudentGroup;

        try {
            viewStudentGroup = ReadStudentGroupInformation.run(studentGroupCodeString);

        } catch (InvalidSituationServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroup");
            actionErrors.add("error.noGroup", error);
            saveErrors(request, actionErrors);
            return mapping.findForward("viewShiftsAndGroups");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        InfoSiteStudentGroup infoSiteStudentGroup = (InfoSiteStudentGroup) viewStudentGroup;

        request.setAttribute("infoSiteStudentGroup", infoSiteStudentGroup);

        return mapping.findForward("sucess");
    }

    public ActionForward remove(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        User userView = getUserView(request);
        String userName = userView.getUsername();

        String studentGroupCodeString = request.getParameter("studentGroupCode");

        Boolean shiftWithGroups;
        try {
            shiftWithGroups = UnEnrollStudentInGroup.run(userName, studentGroupCodeString);

        } catch (NotAuthorizedException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("errors.noStudentInAttendsSetToDelete");
            actionErrors.add("errors.noStudentInAttendsSetToDelete", error);
            saveErrors(request, actionErrors);
            return mapping.findForward("insucess");

        } catch (InvalidSituationServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroup");
            actionErrors.add("error.noGroup", error);
            saveErrors(request, actionErrors);
            return mapping.findForward("viewShiftsAndGroups");

        } catch (InvalidArgumentsServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("errors.removeEnrolment.studentNotEnroled");
            actionErrors.add("errors.removeEnrolment.studentNotEnroled", error);
            saveErrors(request, actionErrors);
            return mapping.findForward("viewStudentGroupInformation");

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);

        } catch (DomainException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.studentGroup.cannotRemoveAttendsBecauseAlreadyHasProjectSubmissions");
            actionErrors.add("error.studentGroup.cannotRemoveAttendsBecauseAlreadyHasProjectSubmissions", error);
            saveErrors(request, actionErrors);
            return mapping.findForward("insucess");
        }

        if (!shiftWithGroups.booleanValue()) {
            return mapping.findForward("viewShiftsAndGroups");
        }

        return mapping.findForward("viewStudentGroupInformation");

    }

}