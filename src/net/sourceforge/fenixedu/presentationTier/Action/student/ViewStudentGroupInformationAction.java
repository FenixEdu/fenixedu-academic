/*
 * Created on 26/Ago/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoExportGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroup;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author asnr and scpo
 *
 */
public class ViewStudentGroupInformationAction extends FenixContextAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException, FenixFilterException, FenixServiceException {

		IUserView userView = getUserView(request);

		String studentGroupCodeString = request.getParameter("studentGroupCode");
		Integer studentGroupCode = new Integer(studentGroupCodeString);
		String shiftCodeString = request.getParameter("shiftCode");
		String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
		Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
		
		ISiteComponent viewStudentGroup;
		Object[] args = { studentGroupCode };
		Object[] argsAux = {studentGroupCode,groupPropertiesCode,shiftCodeString,userView.getUtilizador()};
		
		try {
			
			Integer type = (Integer) ServiceUtils.executeService(userView, "VerifyGroupingAndStudentGroupWithoutShift",argsAux);
			viewStudentGroup = (InfoSiteStudentGroup) ServiceUtils.executeService(userView, "ReadStudentGroupInformation", args);
			request.setAttribute("ShiftType",type);
		}catch (ExistingServiceException e){
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			error = new ActionError("error.noProject");
			actionErrors.add("error.noProject", error);
			saveErrors(request, actionErrors);
			return mapping.findForward("viewExecutionCourseProjects");
		}catch (InvalidSituationServiceException e) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			error = new ActionError("error.noGroup");
			actionErrors.add("error.noGroup", error);
			saveErrors(request, actionErrors);
			return mapping.findForward("viewShiftsAndGroups");
		}catch (InvalidArgumentsServiceException e){
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			error = new ActionError("error.StudentGroupShiftIsChanged");
			actionErrors.add("error.StudentGroupShiftIsChanged", error);
			saveErrors(request, actionErrors);
			return mapping.findForward("viewShiftsAndGroups");
		}catch (FenixServiceException e){
			throw new FenixActionException(e);
		}
		
		InfoSiteStudentGroup infoSiteStudentGroup = (InfoSiteStudentGroup) viewStudentGroup;
		request.setAttribute("infoSiteStudentGroup", infoSiteStudentGroup);

        List<InfoExportGrouping> infoExportGroupings = (List<InfoExportGrouping>) ServiceUtils.
                executeService(userView, "ReadExportGroupingsByGrouping", new Object[]{ groupPropertiesCode });
        request.setAttribute("infoExportGroupings", infoExportGroupings);

		return mapping.findForward("sucess");
	}
}
