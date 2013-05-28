package net.sourceforge.fenixedu.presentationTier.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.RemoveDegreeFromDepartment;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Department.DepartmentDegreeBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class DepartmentDegreesDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        DepartmentDegreeBean departmentDegreeBean = getRenderedObject();
        if (departmentDegreeBean == null) {
            departmentDegreeBean = new DepartmentDegreeBean();
        }
        return forwardToPage(mapping, request, departmentDegreeBean);
    }

    public ActionForward associate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException,  FenixServiceException {
        executeFactoryMethod();
        return prepare(mapping, form, request, response);
    }

    public ActionForward remove(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException,  FenixServiceException {
        final String departmentString = request.getParameter("departmentID");
        final String degreeString = request.getParameter("degreeID");
        final Department department = AbstractDomainObject.fromExternalId(Integer.valueOf(departmentString));
        final Degree degree = AbstractDomainObject.fromExternalId(Integer.valueOf(degreeString));
        RemoveDegreeFromDepartment.run(department, degree);
        final DepartmentDegreeBean departmentDegreeBean = new DepartmentDegreeBean();
        departmentDegreeBean.setDepartment(department);
        return forwardToPage(mapping, request, departmentDegreeBean);
    }

    private ActionForward forwardToPage(final ActionMapping mapping, final HttpServletRequest request,
            final DepartmentDegreeBean departmentDegreeBean) {
        request.setAttribute("departmentDegreeBean", departmentDegreeBean);
        return mapping.findForward("manageDepartmentDegrees");
    }

}