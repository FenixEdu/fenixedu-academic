/*
 * Created on 13/Nov/2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidStudentNumberServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.EnrollGroupShift;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadGroupingShifts;
import net.sourceforge.fenixedu.applicationTier.Servico.student.VerifyStudentGroupAtributes;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShifts;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author joaosa and rmalo
 * 
 */
@Mapping(module = "student", path = "/enrollStudentGroupShift", attribute = "groupEnrolmentForm",
        formBean = "groupEnrolmentForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "sucess", path = "/student/enrollStudentGroupShift_bd.jsp"),
        @Forward(name = "insucess", path = "/viewEnroledExecutionCourses.do?method=prepare"),
        @Forward(name = "viewStudentGroupInformation", path = "/viewStudentGroupInformation.do"),
        @Forward(name = "viewShiftsAndGroups", path = "/viewShiftsAndGroups.do"),
        @Forward(name = "viewExecutionCourseProjects", path = "/viewExecutionCourseProjects.do", tileProperties = @Tile(
                title = "private.student.subscribe.groups")) })
public class EnrollStudentGroupShiftDispatchAction extends FenixDispatchAction {

    public ActionForward prepareEnrollStudentGroupShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        IUserView userView = getUserView(request);

        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        String studentGroupCodeString = request.getParameter("studentGroupCode");

        try {
            VerifyStudentGroupAtributes.run(groupPropertiesCodeString, null, studentGroupCodeString, userView.getUtilizador(),
                    new Integer(5));

        } catch (NotAuthorizedException e) {
            ActionErrors actionErrors2 = new ActionErrors();
            ActionError error2 = null;
            error2 = new ActionError("errors.noStudentInAttendsSet");
            actionErrors2.add("errors.noStudentInAttendsSet", error2);
            saveErrors(request, actionErrors2);
            return mapping.findForward("insucess");
        } catch (ExistingServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noProject");
            actionErrors.add("error.noProject", error);
            saveErrors(request, actionErrors);
            return mapping.findForward("viewExecutionCourseProjects");
        } catch (InvalidSituationServiceException e) {
            ActionErrors actionErrors1 = new ActionErrors();
            ActionError error1 = null;
            error1 = new ActionError("errors.enrollStudentGroupShift.notEnroled");
            actionErrors1.add("errors.enrollStudentGroupShift.notEnroled", error1);
            saveErrors(request, actionErrors1);
            return mapping.findForward("viewStudentGroupInformation");

        } catch (InvalidChangeServiceException e) {
            ActionErrors actionErrors2 = new ActionErrors();
            ActionError error2 = null;
            error2 = new ActionError("error.enrollStudentGroupShift");
            actionErrors2.add("error.enrollStudentGroupShift", error2);
            saveErrors(request, actionErrors2);
            return mapping.findForward("viewShiftsAndGroups");
        } catch (FenixServiceException e) {
            ActionErrors actionErrors2 = new ActionErrors();
            ActionError error2 = null;
            error2 = new ActionError("error.noGroup");
            actionErrors2.add("error.noGroup", error2);
            saveErrors(request, actionErrors2);
            return mapping.findForward("viewShiftsAndGroups");
        }

        InfoSiteShifts infoSiteShifts = null;

        try {
            infoSiteShifts = ReadGroupingShifts.run(groupPropertiesCodeString, studentGroupCodeString);

        } catch (ExistingServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noProject");
            actionErrors.add("error.noProject", error);
            saveErrors(request, actionErrors);
            return mapping.findForward("viewExecutionCourseProjects");
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

        List shifts = infoSiteShifts.getShifts();

        if (shifts.size() == 0) {
            ActionErrors actionErrors3 = new ActionErrors();
            ActionError error3 = null;
            // Create an ACTION_ERROR
            error3 = new ActionError("errors.enrollStudentGroupShift.allShiftsFull");
            actionErrors3.add("errors.enrollStudentGroupShift.allShiftsFull", error3);
            saveErrors(request, actionErrors3);
            request.setAttribute("groupPropertiesCode", groupPropertiesCodeString);
            return mapping.findForward("viewStudentGroupInformation");
        }

        ArrayList shiftsList = new ArrayList();
        InfoShift oldInfoShift = infoSiteShifts.getOldShift();
        if (shifts.size() != 0) {
            shiftsList.add(new LabelValueBean("(escolher)", ""));
            InfoShift infoShift;
            Iterator iter = shifts.iterator();
            String label, value;
            List shiftValues = new ArrayList();
            while (iter.hasNext()) {
                infoShift = (InfoShift) iter.next();
                value = infoShift.getExternalId().toString();
                shiftValues.add(value);
                label = infoShift.getNome();
                shiftsList.add(new LabelValueBean(label, value));
            }
            if (shiftsList.size() == 1 && shiftValues.contains(oldInfoShift.getExternalId().toString())) {
                ActionErrors actionErrors4 = new ActionErrors();
                ActionError error4 = null;
                error4 = new ActionError("errors.enrollStudentGroupShift.allShiftsFull");
                actionErrors4.add("errors.enrollStudentGroupShift.allShiftsFull", error4);
                saveErrors(request, actionErrors4);
                return mapping.findForward("viewStudentGroupInformation");
            }
            request.setAttribute("shiftsList", shiftsList);
        }

        request.setAttribute("shift", oldInfoShift);

        return mapping.findForward("sucess");

    }

    public ActionForward enrollStudentGroupShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        DynaActionForm enrollStudentGroupForm = (DynaActionForm) form;

        IUserView userView = getUserView(request);

        String studentGroupCodeString = request.getParameter("studentGroupCode");
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        String newShiftString = (String) enrollStudentGroupForm.get("shift");

        if (newShiftString.equals("")) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("errors.invalid.shift.groupEnrolment");
            actionErrors.add("errors.invalid.shift.groupEnrolment", error);
            saveErrors(request, actionErrors);
            return prepareEnrollStudentGroupShift(mapping, form, request, response);

        }

        try {
            EnrollGroupShift.run(studentGroupCodeString, groupPropertiesCodeString, newShiftString, userView.getUtilizador());
        } catch (NotAuthorizedException e) {
            ActionErrors actionErrors2 = new ActionErrors();
            ActionError error2 = null;
            error2 = new ActionError("errors.noStudentInAttendsSet");
            actionErrors2.add("errors.noStudentInAttendsSet", error2);
            saveErrors(request, actionErrors2);
            return mapping.findForward("insucess");
        } catch (ExistingServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noProject");
            actionErrors.add("error.noProject", error);
            saveErrors(request, actionErrors);
            return mapping.findForward("viewExecutionCourseProjects");
        } catch (InvalidArgumentsServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroup");
            actionErrors.add("error.noGroup", error);
            saveErrors(request, actionErrors);
            return mapping.findForward("viewShiftsAndGroups");

        } catch (InvalidSituationServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("errors.enrollStudentGroupShift.notEnroled");
            actionErrors.add("errors.enrollStudentGroupShift.notEnroled", error);
            saveErrors(request, actionErrors);
            return mapping.findForward("viewStudentGroupInformation");

        } catch (InvalidChangeServiceException e) {
            ActionErrors actionErrors3 = new ActionErrors();
            ActionError error3 = null;
            // Create an ACTION_ERROR
            error3 = new ActionError("errors.enrollStudentGroupShift.shiftFull");
            actionErrors3.add("errors.enrollStudentGroupShift.shiftFull", error3);
            saveErrors(request, actionErrors3);
            return mapping.findForward("viewStudentGroupInformation");

        } catch (InvalidStudentNumberServiceException e) {
            ActionErrors actionErrors3 = new ActionErrors();
            ActionError error3 = null;
            error3 = new ActionError("error.enrollStudentGroupShift");
            actionErrors3.add("error.enrollStudentGroupShift", error3);
            saveErrors(request, actionErrors3);
            return mapping.findForward("viewShiftsAndGroups");

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return mapping.findForward("viewShiftsAndGroups");
    }
}