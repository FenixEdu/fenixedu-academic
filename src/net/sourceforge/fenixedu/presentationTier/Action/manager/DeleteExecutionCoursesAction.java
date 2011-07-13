/*
 * Created on 30/Set/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.DeleteExecutionCourses;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author lmac1
 */

@Mapping(module = "manager", path = "/deleteExecutionCourses", attribute = "executionCourseForm", formBean = "executionCourseForm", scope = "request")
@Forwards(value = { @Forward(name = "readExecutionCourses", path = "/readExecutionCourses.do") })
public class DeleteExecutionCoursesAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws FenixActionException, FenixFilterException {

	IUserView userView = UserView.getUser();
	DynaActionForm deleteForm = (DynaActionForm) form;

	List internalIds = Arrays.asList((Integer[]) deleteForm.get("internalIds"));

	List errorCodes = new ArrayList();

	try {
	    errorCodes = (List) DeleteExecutionCourses.run(internalIds);
	} catch (FenixServiceException fenixServiceException) {
	    throw new FenixActionException(fenixServiceException.getMessage());
	}

	if (!errorCodes.isEmpty()) {
	    ActionErrors actionErrors = new ActionErrors();
	    Iterator codesIter = errorCodes.iterator();
	    ActionError error = null;
	    while (codesIter.hasNext()) {
		error = new ActionError("errors.invalid.delete.not.empty.execution.course", codesIter.next());
		actionErrors.add("errors.invalid.delete.not.empty.execution.course", error);
	    }
	    saveErrors(request, actionErrors);
	}
	return mapping.findForward("readExecutionCourses");
    }
}