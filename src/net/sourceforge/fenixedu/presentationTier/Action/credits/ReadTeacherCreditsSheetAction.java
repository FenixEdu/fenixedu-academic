/*
 * Created on Dec 3, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.credits;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.credits.TeacherCreditsSheetDTO;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author jpvl
 */
public class ReadTeacherCreditsSheetAction extends Action {

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

        DynaActionForm dynaForm = (DynaActionForm) form;
        InfoTeacher infoTeacher = (InfoTeacher) request.getAttribute("infoTeacher");
        if (infoTeacher == null) {
            Integer teacherNumber = null;

            if (request.getParameter("teacherNumber") != null
                    && request.getParameter("teacherNumber").length() > 0) {
                teacherNumber = new Integer(request.getParameter("teacherNumber"));
            } else if (dynaForm.get("teacherNumber") != null) {
                teacherNumber = (Integer) dynaForm.get("teacherNumber");
            }

            infoTeacher = new InfoTeacher();
            infoTeacher.setTeacherNumber(teacherNumber);
        }

        Integer executionPeriodId = null;
        if (request.getParameter("executionPeriodId") != null
                && request.getParameter("executionPeriodId").length() > 0) {
            executionPeriodId = new Integer(request.getParameter("executionPeriodId"));
        } else if (dynaForm.get("executionPeriodId") != null) {
            executionPeriodId = (Integer) dynaForm.get("executionPeriodId");
        }

        Object args[] = { infoTeacher, executionPeriodId };
        TeacherCreditsSheetDTO teacherCreditsSheetDTO = (TeacherCreditsSheetDTO) ServiceUtils
                .executeService(userView, "ReadTeacherCreditsSheet", args);

        BeanComparator dateComparator = new BeanComparator("start");
        Collections.sort(teacherCreditsSheetDTO.getInfoManagementPositions(), dateComparator);
        Collections.sort(teacherCreditsSheetDTO.getInfoServiceExemptions(), dateComparator);
        request.setAttribute("teacherCreditsSheet", teacherCreditsSheetDTO);
        return mapping.findForward("successfull-read");
    }
}