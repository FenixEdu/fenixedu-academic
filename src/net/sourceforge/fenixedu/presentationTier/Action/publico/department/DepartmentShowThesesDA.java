package net.sourceforge.fenixedu.presentationTier.Action.publico.department;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.functionalities.AbstractFunctionalityContext;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.publico.PublicShowThesesDA;
import net.sourceforge.fenixedu.presentationTier.Action.publico.ThesisFilterBean;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class DepartmentShowThesesDA extends PublicShowThesesDA {

    private Unit getUnit(HttpServletRequest request) {
	UnitSite site = (UnitSite) AbstractFunctionalityContext.getCurrentContext(request).getSelectedContainer();
	Unit unit = site.getUnit();

	if (unit == null) {
	    Integer id = getIntegerFromRequest(request, "selectedDepartmentUnitID");
	    unit = (Unit) RootDomainObject.getInstance().readPartyByOID(id);
	}

	return unit;
    }

    private Department getDepartment(HttpServletRequest request) {
	Unit unit = getUnit(request);
	if (unit == null) {
	    return null;
	} else {
	    return unit.getDepartment();
	}
    }

    @Override
    protected ThesisFilterBean getFilterBean(HttpServletRequest request) throws Exception {
	ThesisFilterBean bean = super.getFilterBean(request);

	Department department = getDepartment(request);

	bean.setDepartment(department);
	bean.setDegreeOptions(department.getDegrees());

	return bean;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("unit", getUnit(request));
	request.setAttribute("department", getDepartment(request));

	return super.execute(mapping, actionForm, request, response);
    }

}
