/*
 * Created on 27/Ago/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.Arrays;
import java.util.Collections;
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
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NoChangeMadeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonValidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadExportGroupingsByGrouping;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadStudentsWithoutGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadStudentsWithoutGroup.NewStudentGroupAlreadyExists;
import net.sourceforge.fenixedu.applicationTier.Servico.student.VerifyStudentGroupAtributes;
import net.sourceforge.fenixedu.dataTransferObject.InfoExportGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentsWithoutGroup;
import net.sourceforge.fenixedu.domain.student.GroupEnrolment;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author asnr and scpo
 * 
 */
@Mapping(module = "student", path = "/groupEnrolment", attribute = "groupEnrolmentForm", formBean = "groupEnrolmentForm",
        scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "sucess", path = "/student/viewGroupEnrolment_bd.jsp"),
        @Forward(name = "insucess", path = "/viewEnroledExecutionCourses.do?method=prepare"),
        @Forward(name = "viewStudentGroupInformation", path = "/viewStudentGroupInformation.do"),
        @Forward(name = "viewShiftsAndGroups", path = "/viewShiftsAndGroups.do"),
        @Forward(name = "viewExecutionCourseProjects", path = "/viewExecutionCourseProjects.do") })
public class GroupEnrolmentDispatchAction extends FenixDispatchAction {

    public ActionForward prepareEnrolment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException,  FenixServiceException {

        IUserView userView = getUserView(request);

        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");

        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        String shiftCodeString = request.getParameter("shiftCode");

        Integer shiftCode = null;
        if (shiftCodeString != null) {
            shiftCode = new Integer(shiftCodeString);
        }

        try {

            VerifyStudentGroupAtributes.run(groupPropertiesCode, shiftCode, null, userView.getUtilizador(), new Integer(2));

        } catch (NotAuthorizedException e) {
            ActionErrors actionErrors2 = new ActionErrors();
            ActionError error2 = null;
            error2 = new ActionError("errors.noStudentInAttendsSet");
            actionErrors2.add("errors.noStudentInAttendsSet", error2);
            saveErrors(request, actionErrors2);
            return mapping.findForward("insucess");
        } catch (InvalidChangeServiceException e) {
            ActionErrors actionErrors2 = new ActionErrors();
            ActionError error2 = null;
            error2 = new ActionError("error.noProject");
            actionErrors2.add("error.noProject", error2);
            saveErrors(request, actionErrors2);
            return mapping.findForward("viewExecutionCourseProjects");
        } catch (InvalidArgumentsServiceException e) {
            ActionErrors actionErrors2 = new ActionErrors();
            ActionError error2 = null;
            error2 = new ActionError("errors.impossible.nrOfGroups.groupEnrolment");
            actionErrors2.add("errors.impossible.nrOfGroups.groupEnrolment", error2);
            saveErrors(request, actionErrors2);
            return mapping.findForward("viewShiftsAndGroups");
        } catch (InvalidSituationServiceException e) {
            ActionErrors actionErrors2 = new ActionErrors();
            ActionError error2 = null;
            error2 = new ActionError("errors.existing.groupStudentEnrolment");
            actionErrors2.add("errors.existing.groupStudentEnrolment", error2);
            saveErrors(request, actionErrors2);
            return mapping.findForward("viewShiftsAndGroups");

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);

        }

        InfoSiteStudentsWithoutGroup studentsNotEnroled = null;

        try {
            studentsNotEnroled =
                    (InfoSiteStudentsWithoutGroup) ReadStudentsWithoutGroup.run(groupPropertiesCode, userView.getUtilizador());

        } catch (ExistingServiceException e) {
            ActionErrors actionErrors1 = new ActionErrors();
            ActionError error1 = null;
            error1 = new ActionError("error.noProject");
            actionErrors1.add("error.noProject", error1);
            saveErrors(request, actionErrors1);
            return mapping.findForward("viewExecutionCourseProjects");
        } catch (NewStudentGroupAlreadyExists e) {
            ActionErrors actionErrors1 = new ActionErrors();
            ActionError error1 = null;
            error1 = new ActionError("error.existingGroup");
            actionErrors1.add("error.existingGroup", error1);
            saveErrors(request, actionErrors1);
            return prepareEnrolment(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        List infoStudentList = studentsNotEnroled.getInfoStudentList();
        if (infoStudentList != null) {
            Collections.sort(infoStudentList, new BeanComparator("number"));
            request.setAttribute("infoStudents", infoStudentList);
        }
        request.setAttribute("groupNumber", studentsNotEnroled.getGroupNumber());
        request.setAttribute("groupPropertiesCode", groupPropertiesCode);
        request.setAttribute("shiftCode", shiftCode);
        request.setAttribute("infoUserStudent", studentsNotEnroled.getInfoUserStudent());
        request.setAttribute("infoGrouping", studentsNotEnroled.getInfoGrouping());

        List<InfoExportGrouping> infoExportGroupings = ReadExportGroupingsByGrouping.run(groupPropertiesCode);
        request.setAttribute("infoExportGroupings", infoExportGroupings);

        return mapping.findForward("sucess");

    }

    public ActionForward enrolment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException,  FenixServiceException {

        DynaActionForm enrolmentForm = (DynaActionForm) form;

        IUserView userView = getUserView(request);

        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);

        String groupNumberString = request.getParameter("groupNumber");
        Integer groupNumber = new Integer(groupNumberString);
        Integer shiftCode = null;
        String shiftCodeString = request.getParameter("shiftCode");
        if (shiftCodeString != null) {
            shiftCode = new Integer(shiftCodeString);
        }

        List<String> studentUsernames = Arrays.asList((String[]) enrolmentForm.get("studentsNotEnroled"));

        try {
            GroupEnrolment.run(groupPropertiesCode, shiftCode, groupNumber, studentUsernames, userView.getUtilizador());

        } catch (NonExistingServiceException e) {
            ActionErrors actionErrors1 = new ActionErrors();
            ActionError error1 = null;
            error1 = new ActionError("error.noProject");
            actionErrors1.add("error.noProject", error1);
            saveErrors(request, actionErrors1);
            return mapping.findForward("viewExecutionCourseProjects");
        } catch (NoChangeMadeServiceException e) {
            ActionErrors actionErrors1 = new ActionErrors();
            ActionError error1 = null;
            error1 = new ActionError("errors.noStudentInAttendsSet");
            actionErrors1.add("errors.noStudentInAttendsSet", error1);
            saveErrors(request, actionErrors1);
            return mapping.findForward("insucess");
        } catch (InvalidStudentNumberServiceException e) {
            ActionErrors actionErrors1 = new ActionErrors();
            ActionError error1 = null;
            error1 = new ActionError("errors.noStudentsInAttendsSet");
            actionErrors1.add("errors.noStudentsInAttendsSet", error1);
            saveErrors(request, actionErrors1);
            return prepareEnrolment(mapping, form, request, response);
        } catch (InvalidArgumentsServiceException e) {
            ActionErrors actionErrors1 = new ActionErrors();
            ActionError error1 = null;
            error1 = new ActionError("errors.impossible.nrOfGroups.groupEnrolment");
            actionErrors1.add("errors.impossible.nrOfGroups.groupEnrolment", error1);
            saveErrors(request, actionErrors1);
            return prepareEnrolment(mapping, form, request, response);
        } catch (NonValidChangeServiceException e) {
            ActionErrors actionErrors1 = new ActionErrors();
            ActionError error1 = null;
            error1 = new ActionError("errors.impossible.minimumCapacity.groupEnrolment");
            actionErrors1.add("errors.impossible.minimumCapacity.groupEnrolment", error1);
            saveErrors(request, actionErrors1);
            return prepareEnrolment(mapping, form, request, response);
        } catch (NotAuthorizedException e) {
            ActionErrors actionErrors1 = new ActionErrors();
            ActionError error1 = null;
            error1 = new ActionError("errors.impossible.maximumCapacity.groupEnrolment");
            actionErrors1.add("errors.impossible.maximumCapacity.groupEnrolment", error1);
            saveErrors(request, actionErrors1);
            return prepareEnrolment(mapping, form, request, response);

        } catch (ExistingServiceException e) {
            ActionErrors actionErrors1 = new ActionErrors();
            ActionError error1 = null;
            error1 = new ActionError("errors.existing.elementsEnrolment");
            actionErrors1.add("errors.existing.elementsEnrolment", error1);
            saveErrors(request, actionErrors1);
            return prepareEnrolment(mapping, form, request, response);

        } catch (InvalidSituationServiceException e) {
            ActionErrors actionErrors1 = new ActionErrors();
            ActionError error1 = null;
            error1 = new ActionError("errors.existing.groupStudentEnrolment");
            actionErrors1.add("errors.existing.groupStudentEnrolment", error1);
            saveErrors(request, actionErrors1);
            return mapping.findForward("viewShiftsAndGroups");

        } catch (FenixServiceException e) {
            ActionErrors actionErrors1 = new ActionErrors();
            ActionError error1 = null;
            error1 = new ActionError("error.existingGroup");
            actionErrors1.add("error.existingGroup", error1);
            saveErrors(request, actionErrors1);
            return prepareEnrolment(mapping, form, request, response);

        }

        request.setAttribute("groupPropertiesCode", groupPropertiesCode);
        request.setAttribute("shiftCode", shiftCode);

        return mapping.findForward("viewShiftsAndGroups");

    }
}