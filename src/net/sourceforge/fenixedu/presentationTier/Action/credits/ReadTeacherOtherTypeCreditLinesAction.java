/*
 * Created on Dec 3, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.credits.TeacherOtherTypeCreditLineDTO;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author jpvl
 */
public class ReadTeacherOtherTypeCreditLinesAction extends Action {

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
        DynaActionForm dynaForm = (DynaActionForm) form;
        Integer executionPeriodId = (Integer) dynaForm.get("executionPeriodId");
        Object args[] = { infoTeacher.getIdInternal(), executionPeriodId };
        TeacherOtherTypeCreditLineDTO dto = (TeacherOtherTypeCreditLineDTO) ServiceUtils.executeService(
                userView, "ReadOtherTypeCreditLineByTeacherAndExecutionPeriodService", args);
        request.setAttribute("dto", dto);
        return mapping.findForward("successfull-read");
    }
}