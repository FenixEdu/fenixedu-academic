/*
 * Created on 22/Dez/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonPredicate;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchPersonResults;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Tânia Pousão
 * 
 */
public class FindPersonAction extends FenixDispatchAction {
    public ActionForward prepareFindPerson(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("findPerson");
    }

    public ActionForward preparePerson(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        DynaActionForm findPersonForm = (DynaActionForm) actionForm;

        String roleType = null;
        String degreeType = null;

        if (request.getParameter("roleType") != null && request.getParameter("roleType").length() > 0) {
            roleType = (String) request.getParameter("roleType");

        } else if (findPersonForm.get("roleType") != null) {
            roleType = (String) findPersonForm.get("roleType");
        }
        if (request.getParameter("degreeType") != null
                && request.getParameter("degreeType").length() > 0) {
            degreeType = (String) request.getParameter("degreeType");

        } else if (findPersonForm.get("degreeType") != null) {
            degreeType = (String) findPersonForm.get("degreeType");
        }

        if (roleType != null && roleType.length() != 0) {
            if (roleType.equals(RoleType.EMPLOYEE.getName())
                    || roleType.equals(RoleType.TEACHER.getName())) {
                if (roleType.equals(RoleType.TEACHER.getName())) {
                    List departments = (List) ServiceUtils.executeService(null, "ReadAllDepartments",
                            null);
                    request.setAttribute("departments", departments);
                }
                // request.removeAttribute("degreeType");
            }

            if (roleType.equals(RoleType.STUDENT.getName())) {

                if (degreeType.length() != 0) {
                    Object[] args = { degreeType };
                    List nonMasterDegree = (List) ServiceUtils.executeService(null,
                            "ReadAllDegreesByType", args);

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
       
        if (request.getParameter("viewPhoto") != null
                && request.getParameter("viewPhoto").length() > 0) {
        	viewPhoto = getCheckBoxValue((String) request.getParameter("viewPhoto"));

        } else if (findPersonForm.get("viewPhoto") != null) {
        	viewPhoto = getCheckBoxValue((String) findPersonForm.get("viewPhoto"));
        }
        findPersonForm.set("viewPhoto",viewPhoto.toString());
        request.setAttribute("viewPhoto", viewPhoto);
      
        return mapping.findForward("findPerson");
    }

    public ActionForward findPerson(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm findPersonForm = (DynaActionForm) actionForm;
        String name = null;
        if (request.getParameter("name") != null && request.getParameter("name").length() > 0) {
            name = request.getParameter("name");
        } else if (findPersonForm.get("name") != null) {
            name = (String) findPersonForm.get("name");
        }

        Integer startIndex = null;
        if (request.getParameter("startIndex") != null
                && request.getParameter("startIndex").length() > 0) {
            startIndex = Integer.valueOf(request.getParameter("startIndex"));
        } else if (findPersonForm.get("startIndex") != null) {
            startIndex = Integer.valueOf((String) findPersonForm.get("startIndex"));
        }

        String roleType = null;
        Integer departmentId = null;
        Integer degreeId = null;
        String degreeType = null;

        if (request.getParameter("roleType") != null && request.getParameter("roleType").length() > 0) {
            roleType = (String) request.getParameter("roleType");

        } else if (findPersonForm.get("roleType") != null) {
            roleType = (String) findPersonForm.get("roleType");
        }

        if (request.getParameter("degreeType") != null
                && request.getParameter("degreeType").length() > 0) {
            degreeType = (String) request.getParameter("degreeType");

        } else if (findPersonForm.get("degreeType") != null) {
            degreeType = (String) findPersonForm.get("degreeType");
        }
        if (degreeType.length() == 0 && roleType.length() == 0) {
            degreeType = null;
        } else if (roleType.equals(RoleType.STUDENT.getName())) {
            if (degreeType.length() != 0) {
                Object[] args1 = { degreeType };
                List nonMasterDegree = (List) ServiceUtils.executeService(null, "ReadAllDegreesByType",
                        args1);

                request.setAttribute("nonMasterDegree", nonMasterDegree);
                request.setAttribute("degreeType", degreeType);

            } else {
                request.setAttribute("degreeType", true);
            }
        }
        if (request.getParameter("departmentId") != null
                && request.getParameter("departmentId").length() > 0) {
            departmentId = Integer.valueOf(request.getParameter("departmentId"));

        } else if (findPersonForm.get("departmentId") != null) {
            departmentId = (Integer) findPersonForm.get("departmentId");
        }

        if (roleType.equals(RoleType.TEACHER.getName())) {
            List departments = (List) ServiceUtils.executeService(null, "ReadAllDepartments", null);
            request.setAttribute("departments", departments);
        }

        if (request.getParameter("degreeId") != null && request.getParameter("degreeId").length() > 0) {
            degreeId = Integer.valueOf(request.getParameter("degreeId"));

        } else if (findPersonForm.get("degreeId") != null) {
            degreeId = (Integer) findPersonForm.get("degreeId");
        }

        SearchParameters searchParameters = new SearchPerson.SearchParameters(name, null, null, null,
                roleType, degreeType, degreeId, departmentId);

        SearchPersonPredicate predicate = new SearchPerson.SearchPersonPredicate(searchParameters);

        Object[] args = { searchParameters, predicate };
      
        SearchPerson.SearchPersonResults result = null;
        List<InfoPerson> searchPersons = null;
        try {
            result = (SearchPersonResults) ServiceManagerServiceFactory.executeService(userView,
                    "SearchPerson", args);

            Integer start = (startIndex - 1) * SessionConstants.LIMIT_FINDED_PERSONS;
            Integer end = start + SessionConstants.LIMIT_FINDED_PERSONS;

            searchPersons = SearchParameters.getIntervalPersons(start, end, result.getValidPersons());

        } catch (FenixServiceException e) {
            errors.add("impossibleFindPerson", new ActionError(e.getMessage()));
            saveErrors(request, errors);
            return preparePerson(mapping, actionForm, request, response);
        }

        if (result == null || result.getValidPersons() == null) {
            errors.add("impossibleFindPerson", new ActionError("error.manager.implossible.findPerson"));
        }

        if (searchPersons.isEmpty()) {
            errors.add("impossibleFindPerson", new ActionError("error.manager.implossible.findPerson"));
            saveErrors(request, errors);

        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return preparePerson(mapping, actionForm, request, response);
        }

        int totalPersons = result.getTotalPersons();
        List pagesList = getPageList(totalPersons);

        request.setAttribute("totalFindedPersons", totalPersons);
        request.setAttribute("personListFinded", searchPersons);
        request.setAttribute("name", name);
        request.setAttribute("roleType", roleType);
        request.setAttribute("degreeId", degreeId);
        request.setAttribute("departmentId", departmentId);
        request.setAttribute("pages", pagesList);
        request.setAttribute("startIndex", startIndex);
        findPersonForm.set("name", name);

        if (isEmployeeOrTeacher(userView)) {
            request.setAttribute("show", Boolean.TRUE);
        } else {
            request.setAttribute("show", Boolean.FALSE);
        }
        Boolean viewPhoto = null;
        if (request.getParameter("viewPhoto") != null
                && request.getParameter("viewPhoto").length() > 0) {            
        	viewPhoto = getCheckBoxValue((String) request.getParameter("viewPhoto"));

        } else if (findPersonForm.get("viewPhoto") != null) {
        	viewPhoto = getCheckBoxValue((String) findPersonForm.get("viewPhoto"));
        }
        findPersonForm.set("viewPhoto", viewPhoto.toString());
        request.setAttribute("viewPhoto", viewPhoto);
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

    private List getPageList(Integer totalPersons) {
        List pagesList = new ArrayList();
        int numberOfPages;

        if ((totalPersons % SessionConstants.LIMIT_FINDED_PERSONS) != 0) {
            numberOfPages = (totalPersons / SessionConstants.LIMIT_FINDED_PERSONS) + 1;
        } else {
            numberOfPages = (totalPersons / SessionConstants.LIMIT_FINDED_PERSONS);
        }

        for (int i = 1; i <= numberOfPages; i++) {
            pagesList.add(i);
        }

        return pagesList;
    }
}