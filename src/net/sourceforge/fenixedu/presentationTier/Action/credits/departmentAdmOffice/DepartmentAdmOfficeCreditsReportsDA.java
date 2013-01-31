package net.sourceforge.fenixedu.presentationTier.Action.credits.departmentAdmOffice;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.credits.util.DepartmentCreditsBean;
import net.sourceforge.fenixedu.presentationTier.Action.credits.CreditsReportsDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentAdmOffice", path = "/exportCredits", scope = "request", parameter = "method")
public class DepartmentAdmOfficeCreditsReportsDA extends CreditsReportsDA {

	@Override
	public ActionForward prepareExportDepartmentCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {
		IUserView userView = UserView.getUser();
		DepartmentCreditsBean departmentCreditsBean = new DepartmentCreditsBean();
		departmentCreditsBean.setAvailableDepartments(new ArrayList<Department>(userView.getPerson()
				.getManageableDepartmentCredits()));
		request.setAttribute("departmentCreditsBean", departmentCreditsBean);
		return mapping.findForward("exportDepartmentCourses");
	}
}