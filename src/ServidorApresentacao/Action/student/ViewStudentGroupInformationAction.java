/*
 * Created on 26/Ago/2003
 *
 */
package ServidorApresentacao.Action.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.ISiteComponent;
import DataBeans.InfoSiteStudentGroup;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorApresentacao.Action.base.FenixContextAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author asnr and scpo
 *
 */
public class ViewStudentGroupInformationAction extends FenixContextAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException, FenixFilterException {

		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		String studentGroupCodeString = request.getParameter("studentGroupCode");
		Integer studentGroupCode = new Integer(studentGroupCodeString);
		String shiftCodeString = request.getParameter("shiftCode");
		String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
		Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
		
		ISiteComponent viewStudentGroup;
		Object[] args = { studentGroupCode };
		Object[] argsAux = {studentGroupCode,groupPropertiesCode,shiftCodeString,userView.getUtilizador()};
		
		try {
			
			Integer type = (Integer) ServiceUtils.executeService(userView, "VerifyGroupPropertiesAndStudentGroupWithoutShift",argsAux);
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

		return mapping.findForward("sucess");
	}
}
