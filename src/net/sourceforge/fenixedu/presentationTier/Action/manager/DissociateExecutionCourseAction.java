/*
 * Created on 11/Set/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author lmac1
 */

public class DissociateExecutionCourseAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws FenixActionException, FenixFilterException {

	IUserView userView = UserView.getUser();
	Integer executionCourseId = new Integer(request.getParameter("executionCourseId"));
	Integer curricularCourseId = new Integer(request.getParameter("curricularCourseId"));

	Object args[] = { executionCourseId, curricularCourseId };

	try {
	    ServiceUtils.executeService("DissociateExecutionCourse", args);

	} catch (NonExistingServiceException e) {
	    throw new NonExistingActionException(e.getMessage(), "");
	} catch (FenixServiceException fenixServiceException) {
	    throw new FenixActionException(fenixServiceException.getMessage());
	}
	return mapping.findForward("readCurricularCourse");
    }

}