/*
 * Created on Dec 3, 2003 by jpvl
 *  
 */
package ServidorApresentacao.Action.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoTeacher;
import DataBeans.credits.TeacherCreditsSheetDTO;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jpvl
 */
public class ReadTeacherCreditsSheetAction extends Action
{

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        IUserView userView = SessionUtils.getUserView(request);
        InfoTeacher infoTeacher = (InfoTeacher) request.getAttribute("infoTeacher");
        DynaActionForm dynaForm = (DynaActionForm) form;
        Integer executionPeriodId = (Integer) dynaForm.get("executionPeriodId");
        Object args[] = { infoTeacher, executionPeriodId };
        TeacherCreditsSheetDTO teacherCreditsSheetDTO =
            (TeacherCreditsSheetDTO) ServiceUtils.executeService(
                userView,
                "ReadTeacherCreditsSheet",
                args);
        request.setAttribute("teacherCreditsSheet", teacherCreditsSheetDTO);
        return mapping.findForward("successfull-read");
    }
}