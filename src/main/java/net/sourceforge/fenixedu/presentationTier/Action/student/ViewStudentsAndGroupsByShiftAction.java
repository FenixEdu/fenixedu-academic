/*
 * Created on 8/Jan/2005
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.publico.ReadStudentsAndGroupsByShiftID;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadExportGroupingsByGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoExportGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentsAndGroups;
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
 * @author joaosa & rmalo
 * 
 */
@Mapping(module = "student", path = "/viewStudentsAndGroupsByShift", scope = "request")
@Forwards(value = { @Forward(name = "sucess", path = "/student/viewStudentsAndGroupsByShift_bd.jsp"),
        @Forward(name = "viewExecutionCourseProjects", path = "/viewExecutionCourseProjects.do") })
public class ViewStudentsAndGroupsByShiftAction extends FenixContextAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixServiceException {

        User userView = getUserView(request);

        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        String shiftCodeString = request.getParameter("shiftCode");

        InfoSiteStudentsAndGroups infoSiteStudentsAndGroups = new InfoSiteStudentsAndGroups();

        try {
            infoSiteStudentsAndGroups = ReadStudentsAndGroupsByShiftID.run(groupPropertiesCodeString, shiftCodeString);

        } catch (InvalidSituationServiceException e) {
            ActionErrors actionErrors2 = new ActionErrors();
            ActionError error2 = null;
            error2 = new ActionError("error.noProject");
            actionErrors2.add("error.noProject", error2);
            saveErrors(request, actionErrors2);
            return mapping.findForward("viewExecutionCourseProjects");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("infoSiteStudentsAndGroups", infoSiteStudentsAndGroups);

        List<InfoExportGrouping> infoExportGroupings = ReadExportGroupingsByGrouping.run(groupPropertiesCodeString);
        request.setAttribute("infoExportGroupings", infoExportGroupings);

        return mapping.findForward("sucess");
    }
}