/*
 * Created on 08/Set/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadExportGroupingsByGrouping;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadShiftsAndGroups;
import net.sourceforge.fenixedu.dataTransferObject.InfoExportGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShiftsAndGroups;
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
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author asnr and scpo
 * 
 */
@Mapping(
		module = "student",
		path = "/viewShiftsAndGroups",
		attribute = "enroledExecutionCoursesForm",
		formBean = "enroledExecutionCoursesForm",
		scope = "request")
@Forwards(value = {
		@Forward(name = "sucess", path = "/student/viewShiftsAndGroups_bd.jsp", tileProperties = @Tile(
				title = "private.student.subscribe.groups")),
		@Forward(name = "insucess", path = "/viewEnroledExecutionCourses.do?method=prepare", tileProperties = @Tile(
				title = "private.student.subscribe.groups")) })
public class ViewShiftsAndGroupsAction extends FenixContextAction {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws FenixActionException, FenixFilterException, FenixServiceException {

		IUserView userView = getUserView(request);

		String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");

		Integer groupPropertiesCode = Integer.valueOf(groupPropertiesCodeString);

		String username = userView.getUtilizador();

		List<InfoExportGrouping> infoExportGroupings = ReadExportGroupingsByGrouping.run(groupPropertiesCode);
		request.setAttribute("infoExportGroupings", infoExportGroupings);

		InfoSiteShiftsAndGroups infoSiteShiftsAndGroups;

		try {
			infoSiteShiftsAndGroups = (InfoSiteShiftsAndGroups) ReadShiftsAndGroups.run(groupPropertiesCode, username);

		} catch (InvalidSituationServiceException e) {
			ActionErrors actionErrors2 = new ActionErrors();
			ActionError error2 = null;
			error2 = new ActionError("error.noProject");
			actionErrors2.add("error.noProject", error2);
			saveErrors(request, actionErrors2);
			return mapping.findForward("viewExecutionCourseProjects");
		} catch (NotAuthorizedException e) {
			ActionErrors actionErrors2 = new ActionErrors();
			ActionError error2 = null;
			error2 = new ActionError("errors.noStudentInAttendsSet");
			actionErrors2.add("errors.noStudentInAttendsSet", error2);
			saveErrors(request, actionErrors2);
			return mapping.findForward("insucess");
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		request.setAttribute("infoSiteShiftsAndGroups", infoSiteShiftsAndGroups);

		return mapping.findForward("sucess");
	}
}