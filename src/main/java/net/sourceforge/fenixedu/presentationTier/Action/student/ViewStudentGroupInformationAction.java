/*
 * Created on 26/Ago/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadExportGroupingsByGrouping;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadStudentGroupInformation;
import net.sourceforge.fenixedu.applicationTier.Servico.student.VerifyGroupingAndStudentGroupWithoutShift;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoExportGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroup;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextAction;
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
@Mapping(module = "student", path = "/viewStudentGroupInformation", attribute = "enroledExecutionCoursesForm",
        formBean = "enroledExecutionCoursesForm", scope = "request")
@Forwards(value = { @Forward(name = "sucess", path = "/student/viewStudentGroupInformation_bd.jsp"),
        @Forward(name = "viewShiftsAndGroups", path = "/viewShiftsAndGroups.do"),
        @Forward(name = "viewExecutionCourseProjects", path = "/viewExecutionCourseProjects.do") })
public class ViewStudentGroupInformationAction extends FenixContextAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixServiceException {

        User userView = getUserView(request);

        String studentGroupCodeString = request.getParameter("studentGroupCode");
        String shiftCodeString = request.getParameter("shiftCode");
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");

        ISiteComponent viewStudentGroup;
        try {
            Integer type =
                    VerifyGroupingAndStudentGroupWithoutShift.run(studentGroupCodeString, groupPropertiesCodeString,
                            shiftCodeString, userView.getUsername());
            viewStudentGroup = ReadStudentGroupInformation.run(studentGroupCodeString);
            request.setAttribute("ShiftType", type);
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
        } catch (InvalidArgumentsServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.StudentGroupShiftIsChanged");
            actionErrors.add("error.StudentGroupShiftIsChanged", error);
            saveErrors(request, actionErrors);
            return mapping.findForward("viewShiftsAndGroups");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        InfoSiteStudentGroup infoSiteStudentGroup = (InfoSiteStudentGroup) viewStudentGroup;
        request.setAttribute("infoSiteStudentGroup", infoSiteStudentGroup);

        List<InfoExportGrouping> infoExportGroupings = ReadExportGroupingsByGrouping.run(groupPropertiesCodeString);
        request.setAttribute("infoExportGroupings", infoExportGroupings);

        return mapping.findForward("sucess");
    }
}