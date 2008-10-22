package net.sourceforge.fenixedu.presentationTier.Action.manager.competenceCourses;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.department.ReadAllDepartments;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

public class CreateEditCompetenceCourseDispatchAction extends FenixDispatchAction {

    public ActionForward prepareCreate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {
	IUserView userView = UserView.getUser();

	List<InfoDepartment> departmentList = null;
	try {
	    departmentList = ReadAllDepartments.run();
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
	Integer[] departmentIDs = (Integer[]) actionForm.get("departmentIDs");
	Object[] args = { null, code, name, departmentIDs };
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

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {
	IUserView userView = UserView.getUser();

	Integer competenceCourseID = Integer.valueOf(request.getParameter("competenceCourse"));
	Object[] args = { competenceCourseID };
	InfoCompetenceCourse competenceCourse = null;
	List<InfoDepartment> infoDepartments = null;
	try {
	    competenceCourse = (InfoCompetenceCourse) ServiceUtils.executeService("ReadCompetenceCourse", args);
	    infoDepartments = ReadAllDepartments.run();
	} catch (NotExistingServiceException notExistingServiceException) {

	} catch (FenixServiceException fenixServiceException) {
	    throw new FenixActionException(fenixServiceException.getMessage());
	}

	request.setAttribute("departments", infoDepartments);
	request.setAttribute("competenceCourse", competenceCourse);

	DynaActionForm actionForm = (DynaActionForm) form;
	actionForm.set("competenceCourseID", competenceCourse.getIdInternal());
	actionForm.set("code", competenceCourse.getCode());
	actionForm.set("name", competenceCourse.getName());
	List<Integer> departmentIDs = new ArrayList<Integer>();
	for (InfoDepartment department : competenceCourse.getDepartments()) {
	    departmentIDs.add(department.getIdInternal());
	}
	Integer[] a = new Integer[departmentIDs.size()];
	departmentIDs.toArray(a);
	actionForm.set("departmentIDs", a);

	return mapping.findForward("edit");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws FenixActionException, FenixFilterException {
	IUserView userView = UserView.getUser();
	DynaActionForm actionForm = (DynaActionForm) form;
	Integer competenceCourseID = (Integer) actionForm.get("competenceCourseID");
	String code = (String) actionForm.get("code");
	String name = (String) actionForm.get("name");
	Integer[] departmentIDs = (Integer[]) actionForm.get("departmentIDs");
	Object[] args = { competenceCourseID, code, name, departmentIDs };
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