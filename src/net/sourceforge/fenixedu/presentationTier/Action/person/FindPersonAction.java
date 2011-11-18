/*
 * Created on 22/Dez/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.degree.ReadAllDegreesByType;
import net.sourceforge.fenixedu.applicationTier.Servico.department.ReadAllDepartments;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.security.UserView;
import pt.utl.ist.fenix.tools.util.CollectionPager;

/**
 * @author T�nia Pous�o
 * 
 */
public class FindPersonAction extends FenixDispatchAction {

    public ActionForward prepareFindPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	FindPersonBean bean = new FindPersonBean();
	request.setAttribute("bean", bean);
	return mapping.findForward("findPerson");
    }

    public ActionForward preparePerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	DynaActionForm findPersonForm = (DynaActionForm) actionForm;

	String roleType = null;
	String degreeType = null;

	if (request.getParameter("roleType") != null && request.getParameter("roleType").length() > 0) {
	    roleType = (String) request.getParameter("roleType");

	} else if (findPersonForm.get("roleType") != null) {
	    roleType = (String) findPersonForm.get("roleType");
	}
	if (request.getParameter("degreeType") != null && request.getParameter("degreeType").length() > 0) {
	    degreeType = (String) request.getParameter("degreeType");

	} else if (findPersonForm.get("degreeType") != null) {
	    degreeType = (String) findPersonForm.get("degreeType");
	}

	if (roleType != null && roleType.length() != 0) {
	    if (roleType.equals(RoleType.EMPLOYEE.getName()) || roleType.equals(RoleType.TEACHER.getName())) {
		if (roleType.equals(RoleType.TEACHER.getName())) {
		    List<InfoDepartment> departments = (List<InfoDepartment>) ReadAllDepartments.run();
		    request.setAttribute("departments", departments);
		}
	    }

	    if (roleType.equals(RoleType.STUDENT.getName())) {

		if (degreeType != null && degreeType.length() != 0) {

		    List<InfoDegree> nonMasterDegree = (List<InfoDegree>) ReadAllDegreesByType.run(degreeType);

		    request.setAttribute("nonMasterDegree", nonMasterDegree);
		    request.setAttribute("degreeType", true);

		}
		findPersonForm.set("degreeType", degreeType);
		request.setAttribute("degreeType", degreeType);
	    }

	    findPersonForm.set("roleType", roleType);
	    request.setAttribute("roleType", roleType);

	}
	String name = null;
	if (request.getParameter("name") != null && request.getParameter("name").length() > 0) {
	    name = request.getParameter("name");
	} else if (findPersonForm.get("name") != null) {
	    name = (String) findPersonForm.get("name");
	}
	if (name != null && name.length() > 0) {
	    findPersonForm.set("name", name);
	}

	Boolean viewPhoto = null;

	if (request.getParameter("viewPhoto") != null && request.getParameter("viewPhoto").length() > 0) {
	    viewPhoto = getCheckBoxValue((String) request.getParameter("viewPhoto"));

	} else if (findPersonForm.get("viewPhoto") != null) {
	    viewPhoto = getCheckBoxValue((String) findPersonForm.get("viewPhoto"));
	}
	findPersonForm.set("viewPhoto", viewPhoto.toString());
	request.setAttribute("viewPhoto", viewPhoto);

	return mapping.findForward("findPerson");
    }

    public ActionForward findPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	IUserView userView = UserView.getUser();

	FindPersonBean bean = getRenderedObject();

	boolean fromRequest = true;
	String name = request.getParameter("name");
	String roleType = null;
	Integer departmentId = null;
	Integer degreeId = null;
	String degreeType = null;

	if (bean != null && name == null) {
	    fromRequest = false;
	    name = bean.getName();
	    roleType = bean.getRoleType() == null ? null : bean.getRoleType().toString();
	    departmentId = bean.getDepartmentExternalId();
	    degreeId = bean.getDegreeExternalId();
	    degreeType = bean.getDegreeType() == null ? null : bean.getDegreeType().toString();
	} else {
	    roleType = request.getParameter("roleType");

	    String parameter = request.getParameter("departmentId");
	    departmentId = parameter != null && !parameter.isEmpty() ? Integer.parseInt(parameter) : null;

	    parameter = request.getParameter("degreeId");
	    degreeId = parameter != null && !parameter.isEmpty() ? Integer.parseInt(parameter) : null;
	    degreeType = request.getParameter("degreeType");
	}

	if (name == null) {
	    // error
	}

	SearchParameters searchParameters = new SearchPerson.SearchParameters(name, null, null, null, null, roleType, degreeType,
		degreeId, departmentId, Boolean.TRUE, null, Boolean.FALSE);

	SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(searchParameters);

	Object[] args = { searchParameters, predicate };

	CollectionPager result = null;
	try {
	    result = (CollectionPager) ServiceManagerServiceFactory.executeService("SearchPerson", args);

	} catch (FenixServiceException e) {
	    addErrorMessage(request, "impossibleFindPerson", e.getMessage());
	    return preparePerson(mapping, actionForm, request, response);
	}

	if (result == null) {
	    addErrorMessage(request, "impossibleFindPerson", "error.manager.implossible.findPerson");
	    return preparePerson(mapping, actionForm, request, response);
	}

	if (result.getCollection().isEmpty()) {
	    addErrorMessage(request, "impossibleFindPerson", "error.manager.implossible.findPerson");
	    return preparePerson(mapping, actionForm, request, response);
	}

	final String pageNumberString = request.getParameter("pageNumber");
	final Integer pageNumber = !StringUtils.isEmpty(pageNumberString) ? Integer.valueOf(pageNumberString) : Integer
		.valueOf(1);

	request.setAttribute("pageNumber", pageNumber);
	request.setAttribute("numberOfPages", Integer.valueOf(result.getNumberOfPages()));

	request.setAttribute("personListFinded", result.getPage(pageNumber.intValue()));
	request.setAttribute("totalFindedPersons", result.getCollection().size());

	request.setAttribute("name", name);
	request.setAttribute("roleType", roleType == null ? "" : roleType);
	request.setAttribute("degreeId", degreeId == null ? "" : degreeId.toString());
	request.setAttribute("degreeType", degreeType == null ? "" : degreeType.toString());
	request.setAttribute("departmentId", departmentId == null ? "" : departmentId.toString());

	if (isEmployeeOrTeacher(userView)) {
	    request.setAttribute("show", Boolean.TRUE);
	} else {
	    request.setAttribute("show", Boolean.FALSE);
	}

	Boolean viewPhoto = null;
	if (request.getParameter("viewPhoto") != null && request.getParameter("viewPhoto").length() > 0) {
	    viewPhoto = getCheckBoxValue((String) request.getParameter("viewPhoto"));
	} else if (bean.getViewPhoto() != null) {
	    viewPhoto = bean.getViewPhoto();
	}

	request.setAttribute("viewPhoto", viewPhoto);

	if (fromRequest) {
	    bean = new FindPersonBean();
	    bean.setName(name);
	    bean.setViewPhoto(viewPhoto);
	    if (!StringUtils.isEmpty(roleType)) {
		bean.setRoleType(RoleType.valueOf(roleType));
	    }
	    if (degreeId != null) {
		bean.setDegree(RootDomainObject.getInstance().readDegreeByOID(degreeId));
	    }
	    if (!StringUtils.isEmpty(degreeType)) {
		bean.setDegreeType(DegreeType.valueOf(degreeType));
	    }
	    if (departmentId != null) {
		bean.setDepartment(RootDomainObject.getInstance().readDepartmentByOID(departmentId));
	    }
	    System.out.printf("%s %s %s %s %s %s\n", bean.getRoleType(), bean.getName(), bean.getViewPhoto(), bean.getDegree(),
		    bean.getDegreeType(), bean.getDepartment());
	}
	RenderUtils.invalidateViewState();
	request.setAttribute("bean", bean);
	return mapping.findForward("findPerson");
    }

    private boolean isEmployeeOrTeacher(IUserView userView) {
	return userView.hasRoleType(RoleType.EMPLOYEE) || userView.hasRoleType(RoleType.TEACHER);
    }

    private Boolean getCheckBoxValue(String value) {

	if (value != null && (value.equals("true") || value.equals("yes") || value.equals("on"))) {
	    return Boolean.TRUE;
	}
	return Boolean.FALSE;

    }

    public ActionForward postback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	FindPersonBean bean = getRenderedObject();
	RenderUtils.invalidateViewState();
	request.setAttribute("bean", bean);
	return mapping.findForward("findPerson");
    }

}