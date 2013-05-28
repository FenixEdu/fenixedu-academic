package net.sourceforge.fenixedu.presentationTier.Action.credits;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.credits.util.DepartmentCreditsBean;
import net.sourceforge.fenixedu.domain.credits.util.DepartmentCreditsPoolBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "departmentAdmOffice", path = "/creditsPool", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "manageUnitCredits", path = "/credits/creditsPool/manageUnitCredits.jsp",
        tileProperties = @Tile(title = "private.department.coursestypes")) })
public class ManageDepartmentCreditsPool extends FenixDispatchAction {

    public ActionForward prepareManageUnitCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {
        DepartmentCreditsBean departmentCreditsBean = getDepartmentCreditsBean();
        request.setAttribute("departmentCreditsBean", departmentCreditsBean);
        return mapping.findForward("manageUnitCredits");
    }

    protected DepartmentCreditsBean getDepartmentCreditsBean() {
        IUserView userView = UserView.getUser();
        DepartmentCreditsBean departmentCreditsBean = new DepartmentCreditsBean();
        departmentCreditsBean.setAvailableDepartments(new ArrayList<Department>(userView.getPerson()
                .getManageableDepartmentCredits()));
        return departmentCreditsBean;
    }

    public ActionForward viewDepartmentExecutionCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {
        DepartmentCreditsBean departmentCreditsBean = getRenderedObject();
        if (departmentCreditsBean == null) {
            return prepareManageUnitCredits(mapping, form, request, response);
        }
        request.setAttribute("departmentCreditsBean", departmentCreditsBean);
        DepartmentCreditsPoolBean departmentCreditsPoolBean = new DepartmentCreditsPoolBean(departmentCreditsBean);
        request.setAttribute("departmentCreditsPoolBean", departmentCreditsPoolBean);
        return mapping.findForward("manageUnitCredits");
    }

    public ActionForward editUnitCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {
        DepartmentCreditsPoolBean departmentCreditsPoolBean = getRenderedObject("departmentCreditsPoolBean");
        RenderUtils.invalidateViewState();
        if (departmentCreditsPoolBean == null) {
            return prepareManageUnitCredits(mapping, form, request, response);
        }
        try {
            departmentCreditsPoolBean.editUnitCredits();
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
        }
        DepartmentCreditsBean departmentCreditsBean = getDepartmentCreditsBean();
        departmentCreditsBean.setDepartment(departmentCreditsPoolBean.getDepartment());
        departmentCreditsBean.setExecutionYear(departmentCreditsPoolBean.getAnnualCreditsState().getExecutionYear());
        request.setAttribute("departmentCreditsBean", departmentCreditsBean);
        request.setAttribute("departmentCreditsPoolBean", departmentCreditsPoolBean);
        return mapping.findForward("manageUnitCredits");
    }
}
