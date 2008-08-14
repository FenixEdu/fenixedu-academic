/*
 * Created on 04/Feb/2004
 */

package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Barbosa
 * @author Pica
 * 
 */

public class ShowAllTeachersAction extends FenixDispatchAction {

    public ActionForward showForm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	IUserView userView = UserView.getUser();

	Object[] args = {};
	List teachersList = (List) ServiceUtils.executeService("ReadAllTeachersNumberAndName", args);
	request.setAttribute("teachersList", teachersList);

	return mapping.findForward("show-teachers");
    }
}