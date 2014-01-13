/*
 * Created on 22/Dez/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.degree.ReadAllDegreesByType;
import net.sourceforge.fenixedu.applicationTier.Servico.department.ReadAllDepartments;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;
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
            roleType = request.getParameter("roleType");

        } else if (findPersonForm.get("roleType") != null) {
            roleType = (String) findPersonForm.get("roleType");
        }
        if (request.getParameter("degreeType") != null && request.getParameter("degreeType").length() > 0) {
            degreeType = request.getParameter("degreeType");

        } else if (findPersonForm.get("degreeType") != null) {
            degreeType = (String) findPersonForm.get("degreeType");
        }

        if (roleType != null && roleType.length() != 0) {
            if (roleType.equals(RoleType.EMPLOYEE.getName()) || roleType.equals(RoleType.TEACHER.getName())) {
                if (roleType.equals(RoleType.TEACHER.getName())) {
                    List<InfoDepartment> departments = ReadAllDepartments.run();
                    request.setAttribute("departments", departments);
                }
            }

            if (roleType.equals(RoleType.STUDENT.getName())) {

                if (degreeType != null && degreeType.length() != 0) {

                    List<InfoDegree> nonMasterDegree = ReadAllDegreesByType.run(degreeType);

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
            viewPhoto = getCheckBoxValue(request.getParameter("viewPhoto"));

        } else if (findPersonForm.get("viewPhoto") != null) {
            viewPhoto = getCheckBoxValue((String) findPersonForm.get("viewPhoto"));
        }
        findPersonForm.set("viewPhoto", viewPhoto.toString());
        request.setAttribute("viewPhoto", viewPhoto);

        return mapping.findForward("findPerson");
    }

    public ActionForward findPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User userView = Authenticate.getUser();

        FindPersonBean bean = getRenderedObject();

        boolean fromRequest = true;
        String name = request.getParameter("name");
        String roleType = null;
        String departmentId = null;
        String degreeId = null;
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

            departmentId = request.getParameter("departmentId");

            degreeId = request.getParameter("degreeId");
            degreeType = request.getParameter("degreeType");
        }

        if (name == null) {
            // error
        }

        SearchParameters searchParameters =
                new SearchPerson.SearchParameters(name, null, null, null, null, roleType, degreeType, degreeId, departmentId,
                        Boolean.TRUE, null, Boolean.FALSE, (String) null);

        SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(searchParameters);

        CollectionPager result = SearchPerson.runSearchPerson(searchParameters, predicate);

        if (result == null) {
            addErrorMessage(request, "impossibleFindPerson", "error.manager.implossible.findPerson");
            return preparePerson(mapping, actionForm, request, response);
        }

        if (result.getCollection().isEmpty()) {
            addErrorMessage(request, "impossibleFindPerson", "error.manager.implossible.findPerson");
            return preparePerson(mapping, actionForm, request, response);
        }

        final String pageNumberString = request.getParameter("pageNumber");
        final Integer pageNumber =
                !StringUtils.isEmpty(pageNumberString) ? Integer.valueOf(pageNumberString) : Integer.valueOf(1);

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
            viewPhoto = getCheckBoxValue(request.getParameter("viewPhoto"));
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
            if (!StringUtils.isEmpty(degreeId)) {
                bean.setDegree(FenixFramework.<Degree> getDomainObject(degreeId));
            }
            if (!StringUtils.isEmpty(degreeType)) {
                bean.setDegreeType(DegreeType.valueOf(degreeType));
            }
            if (!StringUtils.isEmpty(departmentId)) {
                bean.setDepartment(FenixFramework.<Department> getDomainObject(departmentId));
            }
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("bean", bean);
        return mapping.findForward("findPerson");
    }

    private boolean isEmployeeOrTeacher(User userView) {
        return userView.getPerson().hasRole(RoleType.EMPLOYEE) || userView.getPerson().hasRole(RoleType.TEACHER);
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