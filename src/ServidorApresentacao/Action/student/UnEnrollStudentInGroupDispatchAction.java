/*
 * Created on 27/Ago/2003
 *
 */
package ServidorApresentacao.Action.student;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.ISiteComponent;
import DataBeans.InfoSiteAllGroups;
import DataBeans.InfoSiteStudentGroup;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author asnr and scpo
 * 
 */
public class UnEnrollStudentInGroupDispatchAction extends FenixDispatchAction {
	
	public ActionForward prepareRemove(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		
		String studentGroupCodeString =	request.getParameter("studentGroupCode");

		Integer studentGroupCode = new Integer(studentGroupCodeString);

		ISiteComponent viewStudentGroup;
		Object[] args = { studentGroupCode };
		
		try {
			viewStudentGroup = (InfoSiteStudentGroup) ServiceUtils.executeService(userView,"ReadStudentGroupInformation",args);

		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		
		List infoSiteStudentInformationList = ((InfoSiteStudentGroup) viewStudentGroup).getInfoSiteStudentInformationList();

		request.setAttribute("infoSiteStudentInformationList",infoSiteStudentInformationList);
		return mapping.findForward("sucess");
	}


	public ActionForward remove(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws FenixActionException {
				
			HttpSession session = request.getSession(false);
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			String userName = userView.getUtilizador();
			
			String studentGroupCodeString =	request.getParameter("studentGroupCode");
			Integer studentGroupCode = new Integer(studentGroupCodeString);
			
			Object[] args1 = { userName, studentGroupCode };
			Integer result;
		
			try {
				result = (Integer) ServiceUtils.executeService(userView,"UnEnrollStudentInGroup",args1);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}

			if (result.intValue() == 0) {
				ActionErrors actionErrors = new ActionErrors();
				ActionError error = null;
				// Create an ACTION_ERROR 
				error = new ActionError("errors.removeEnrolment.minimumNumberOfElementsAndStudentNotEnroled");
				actionErrors.add("errors.removeEnrolment.minimumNumberOfElementsAndStudentNotEnroled",error);
				saveErrors(request, actionErrors);
			}

			if (result.intValue() == 2) {
				ActionErrors actionErrors = new ActionErrors();
				ActionError error = null;
				// Create an ACTION_ERROR 
				error = new ActionError("errors.removeEnrolment.minimumNumberOfElements");
				actionErrors.add("errors.removeEnrolment.minimumNumberOfElements",error);
				saveErrors(request, actionErrors);
			}
			
			if (result.intValue() == 3) {
				ActionErrors actionErrors = new ActionErrors();
				ActionError error = null;
				// Create an ACTION_ERROR 
				error = new ActionError("errors.removeEnrolment.studentNotEnroled");
				actionErrors.add("errors.removeEnrolment.studentNotEnroled",error);
				saveErrors(request, actionErrors);
			}

			String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
			Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);

			ISiteComponent viewAllGroups;
			Object[] args2 = { groupPropertiesCode };
			try {
				viewAllGroups =
					(InfoSiteAllGroups) ServiceUtils.executeService(
						userView,
						"ReadAllShiftsAndGroupsByProject",
						args2);

			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}
				
			List infoSiteGroupsByShiftList= ((InfoSiteAllGroups)viewAllGroups).getInfoSiteGroupsByShiftList();
			request.setAttribute("infoSiteGroupsByShiftList",infoSiteGroupsByShiftList);
			
			return mapping.findForward("remove");
		}

}
