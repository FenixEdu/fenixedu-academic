/*
 * Created on 04/Feb/2004
 */

package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Barbosa
 * @author Pica
 *  
 */

public class ShowAllTeachersAction extends FenixDispatchAction {

    public ActionForward showForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
            IUserView userView = SessionUtils.getUserView(request);

            Object[] args = {};
            List teachersList = (List) ServiceUtils.executeService(userView,
                    "ReadAllTeachersNumberAndName", args);
            request.setAttribute("teachersList", teachersList);

            return mapping.findForward("show-teachers");
        } catch (Exception e) {
            return setError(request, mapping, "errors.grant.unrecoverable", "show-teachers", null);
        }
    }
}