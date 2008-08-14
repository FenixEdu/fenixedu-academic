package net.sourceforge.fenixedu.presentationTier.Action.manager.competenceCourses;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

public class CompetenceCourseDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws FenixActionException, FenixFilterException {
	IUserView userView = UserView.getUser();
	Object[] args = {};
	List<InfoDepartment> infoDepartments;
	try {
	    infoDepartments = (List<InfoDepartment>) ServiceUtils.executeService("ReadAllDepartments", args);
	} catch (FenixServiceException fse) {
	    throw new FenixActionException(fse.getMessage());
	}

	request.setAttribute("departments", infoDepartments);
	request.setAttribute("competenceCourses", new ArrayList());
	return mapping.findForward("showCompetenceCourses");
    }

    public ActionForward showDepartmentCompetenceCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {

	IUserView userView = UserView.getUser();

	DynaActionForm actionForm = (DynaActionForm) form;
	String departmentString = (String) actionForm.get("departmentID");
	Integer departmentID = (departmentString != null && StringUtils.isNumeric(departmentString)) ? Integer
		.valueOf(departmentString) : null;

	Object[] args = {};
	Object[] args2 = { departmentID };
	List<InfoDepartment> infoDepartments;
	List<InfoCompetenceCourse> infoCompetenceCourses;

	try {
	    infoCompetenceCourses = (List<InfoCompetenceCourse>) ServiceUtils.executeService("ReadCompetenceCoursesByDepartment",
		    args2);
	    infoDepartments = (List<InfoDepartment>) ServiceUtils.executeService("ReadAllDepartments", args);
	} catch (FenixServiceException fse) {
	    throw new FenixActionException(fse.getMessage());
	}

	request.setAttribute("departments", infoDepartments);
	Collections.sort(infoCompetenceCourses, new BeanComparator("name", Collator.getInstance()));
	request.setAttribute("competenceCourses", infoCompetenceCourses);
	return mapping.findForward("showCompetenceCourses");
    }

    public ActionForward deleteCompetenceCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {
	IUserView userView = UserView.getUser();
	DynaActionForm actionForm = (DynaActionForm) form;

	Integer[] competenceCoursesIDs = (Integer[]) actionForm.get("competenceCoursesIds");
	Object[] args = { competenceCoursesIDs };

	try {
	    ServiceUtils.executeService("DeleteCompetenceCourses", args);
	} catch (FenixServiceException fenixServiceException) {
	    throw new FenixActionException(fenixServiceException.getMessage());
	} catch (DomainException e) {
	    return setError(request, mapping, e.getMessage(), "readCompetenceCourses", null);
	}
	return mapping.findForward("readCompetenceCourses");
    }

    public ActionForward chooseDepartment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {
	IUserView userView = UserView.getUser();
	Object[] args = {};
	List<InfoDepartment> infoDepartments;
	try {
	    infoDepartments = (List<InfoDepartment>) ServiceUtils.executeService("ReadAllDepartments", args);
	} catch (FenixServiceException fse) {
	    throw new FenixActionException(fse.getMessage());
	}

	request.setAttribute("departments", infoDepartments);
	return mapping.findForward("chooseDepartment");
    }

    public ActionForward showAllCompetences(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {

	IUserView userView = UserView.getUser();
	Object[] args = {};
	List<InfoCompetenceCourse> infoCompetenceCoursesList = null;

	try {

	    infoCompetenceCoursesList = (List<InfoCompetenceCourse>) ServiceUtils
		    .executeService("ReadAllCompetenceCourses", args);

	} catch (FenixServiceException fenixServiceException) {
	    throw new FenixActionException(fenixServiceException.getMessage());
	}

	Collections.sort(infoCompetenceCoursesList, new BeanComparator("name", Collator.getInstance()));

	request.setAttribute("competenceCourses", infoCompetenceCoursesList);
	return mapping.findForward("showAllCompetenceCourses");
    }

    public ActionForward showCompetenceCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {
	IUserView userView = UserView.getUser();

	Integer competenceCourseID = Integer.valueOf((String) request.getParameter("competenceCourseID"));
	Object[] args = { competenceCourseID };
	InfoCompetenceCourse competenceCourse = null;
	try {
	    competenceCourse = (InfoCompetenceCourse) ServiceUtils.executeService("ReadCompetenceCourse", args);
	} catch (NotExistingServiceException notExistingServiceException) {

	} catch (FenixServiceException fenixServiceException) {
	    throw new FenixActionException(fenixServiceException.getMessage());
	}

	request.setAttribute("competenceCourse", competenceCourse);
	return mapping.findForward("showCompetenceCourse");
    }

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws FenixActionException, FenixFilterException {
	IUserView userView = UserView.getUser();
	Object[] args = {};
	List<InfoDepartment> departmentList = null;
	try {
	    departmentList = (List<InfoDepartment>) ServiceUtils.executeService("ReadAllDepartments", args);
	} catch (FenixServiceException fenixServiceException) {
	    throw new FenixActionException(fenixServiceException.getMessage());
	}

	request.setAttribute("departments", departmentList);
	return mapping.findForward("createCompetenceCourse");
    }

    public ActionForward createCompetenceCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {
	IUserView userView = UserView.getUser();
	DynaActionForm actionForm = (DynaActionForm) form;

	String code = (String) actionForm.get("code");
	String name = (String) actionForm.get("name");
	Integer departmentID = (Integer) actionForm.get("departmentID");
	Object[] args = { null, code, name, departmentID };
	InfoCompetenceCourse competenceCourse = null;
	try {
	    competenceCourse = (InfoCompetenceCourse) ServiceUtils.executeService("CreateEditCompetenceCourse", args);

	} catch (InvalidArgumentsServiceException invalidArgumentsServiceException) {

	} catch (NotExistingServiceException notExistingServiceException) {

	} catch (FenixServiceException fenixServiceException) {
	    throw new FenixActionException(fenixServiceException.getMessage());
	}

	request.setAttribute("competenceCourse", competenceCourse);
	return mapping.findForward("showCompetenceCourse");
    }

}
