/*
 * Created on 3/Jul/2003 by jpvl
 *
 */
package ServidorApresentacao.Action.credits;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.DepartmentTeachersDTO;
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
		ActionForward actionForward = null;
		if (userView.hasRoleType(RoleType.CREDITS_MANAGER)) {
			actionForward = mapping.findForward("creditsManager");
		} else if (userView.hasRoleType(RoleType.DEPARTMENT_CREDITS_MANAGER)) {
			Object args[] = { userView.getUtilizador()};
			List  departmentList =
				(List) ServiceUtils.executeService(
					userView,
					"ReadUserManageableDepartments",
					args);
					
			if (departmentList == null
				|| departmentList.isEmpty()) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				return null;
			} else {
				sortList(departmentList);
				request.setAttribute("departmentTeachersDTOList", departmentList);
				actionForward =
					mapping.findForward("creditsManagerDepartment");
			}
			return actionForward;
		}
		return actionForward;
	}

	private void sortList(List departmentList) {
		Collections.sort(departmentList, new BeanComparator("infoDepartment.name"));
		
		Iterator departmentIterator = departmentList.iterator();
		while (departmentIterator.hasNext()) {		
			DepartmentTeachersDTO departmentTeachersDTO	= (DepartmentTeachersDTO) departmentIterator.next();
			Collections.sort(departmentTeachersDTO.getInfoTeacherList(), new BeanComparator("teacherNumber"));
		}
	}

}
