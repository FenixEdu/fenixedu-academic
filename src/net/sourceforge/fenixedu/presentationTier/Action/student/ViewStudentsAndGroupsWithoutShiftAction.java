/*
 * Created on 8/Jan/2005
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.publico.ReadStudentsAndGroupsWithoutShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentsAndGroups;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author joaosa & rmalo
 * 
 */
@Mapping(module = "student", path = "/viewStudentsAndGroupsWithoutShift", scope = "request")
@Forwards(value = {
		@Forward(name = "sucess", path = "/student/viewStudentsAndGroupsWithoutShift_bd.jsp"),
		@Forward(name = "viewExecutionCourseProjects", path = "/viewExecutionCourseProjects.do") })
public class ViewStudentsAndGroupsWithoutShiftAction extends FenixContextAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws FenixActionException, FenixFilterException {

	IUserView userView = getUserView(request);

	String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
	Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);

	InfoSiteStudentsAndGroups infoSiteStudentsAndGroups = new InfoSiteStudentsAndGroups();

	try {
	    infoSiteStudentsAndGroups = (InfoSiteStudentsAndGroups) ReadStudentsAndGroupsWithoutShift.run(groupPropertiesCode);

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

	return mapping.findForward("sucess");
    }
}