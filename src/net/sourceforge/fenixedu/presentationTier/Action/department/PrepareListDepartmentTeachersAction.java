/*
 * Created on Dec 1, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.department;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author jpvl
 */
public class PrepareListDepartmentTeachersAction extends Action {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        InfoDepartment infoDepartment = (InfoDepartment) ServiceUtils.executeService(userView,
                "ReadDepartmentByUser", new Object[] { userView.getUtilizador() });
        request.setAttribute("infoDepartment", infoDepartment);

        ActionForward actionForward = buildActionForward(mapping.findForward("successfull-prepare"),
                infoDepartment);
        return actionForward;
    }

    /**
     * @param forward
     * @param infoDepartment
     * @return
     */
    private ActionForward buildActionForward(ActionForward forward, InfoDepartment infoDepartment) {
        ActionForward forwardBuilded = new ActionForward();
        forwardBuilded.setName(forward.getName());
        forwardBuilded.setContextRelative(forward.getContextRelative());
        forwardBuilded.setRedirect(forward.getRedirect());
        StringBuffer path = new StringBuffer(forward.getPath());
        path.append("&idInternal=").append(infoDepartment.getIdInternal());
        forwardBuilded.setPath(path.toString());
        return forwardBuilded;
    }
}