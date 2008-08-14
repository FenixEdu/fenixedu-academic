/*
 * Created on 24/Set/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.CantDeleteServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.CantDeleteActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author lmac1
 */

public class DeleteCurricularCourseScopeAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws FenixActionException, FenixFilterException {

	IUserView userView = UserView.getUser();

	Integer scopeId = new Integer(request.getParameter("curricularCourseScopeId"));
	Object args[] = { scopeId };

	try {
	    ServiceUtils.executeService("DeleteCurricularCourseScope", args);
	} catch (CantDeleteServiceException e) {
	    throw new CantDeleteActionException("message.cant.delete.curricular.course.scope");
	} catch (FenixServiceException fenixServiceException) {
	    throw new FenixActionException(fenixServiceException.getMessage());
	}

	return mapping.findForward("readCurricularCourse");
    }
}