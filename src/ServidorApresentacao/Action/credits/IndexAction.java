/*
 * Created on 3/Jul/2003 by jpvl
 *
 */
package ServidorApresentacao.Action.credits;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.DepartmentCreditsView;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.RoleType;

/**
 * @author jpvl
 */
public class IndexAction extends Action {

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		IUserView userView = SessionUtils.getUserView(request);

		if (userView.hasRoleType(RoleType.CREDITS_MANAGER)) {
			return mapping.findForward("creditsManager");
		} else if (userView.hasRoleType(RoleType.CREDITS_MANAGER_DEPARTMENT)) {
			ActionForward actionForward = new ActionForward();
			ActionForward mappingForward =
				mapping.findForward("creditsManagerDepartment");
			actionForward.setContextRelative(
				mappingForward.getContextRelative());
			actionForward.setRedirect(mappingForward.getRedirect());

			
			Object args[] = { userView.getUtilizador()};
			DepartmentCreditsView departmentCreditsView =
				(DepartmentCreditsView) ServiceUtils.executeService(
					userView,
					"ReadUserManageableDepartments",
					args);
					
			List manageableDeparments =
				departmentCreditsView.getDepartmentList();
			if (manageableDeparments == null
				|| manageableDeparments.isEmpty()) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				return null;
			} else if (manageableDeparments.size() == 1) {
				String departmentCode = "";
				actionForward.setPath(
					mappingForward.getPath()
						+ "&amp;departmentCode="
						+ departmentCode);
			} else {
				request.setAttribute("departmentCreditsView", departmentCreditsView);
				actionForward =
					mapping.findForward("departament.teachers.list");
			}
			return actionForward;
		} else {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
	}

}
