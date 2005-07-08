/*
 * Created on 7/Mar/2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.credits.managementPosition;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author jpvl
 */
public class ListTeacherManagementPositionsAction extends Action {

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

        InfoTeacher infoTeacher = (InfoTeacher) request.getAttribute("infoTeacher");

        Object args[] = { infoTeacher.getIdInternal() };

        List infoManagementPositions = (List) ServiceUtils.executeService(userView,
                "ReadTeacherManagementPositions", args);

        BeanComparator dateComparator = new BeanComparator("start");

        Collections.sort(infoManagementPositions, dateComparator);

        request.setAttribute("infoManagementPositions", infoManagementPositions);

        return mapping.findForward("successfull-read");
    }
}