/*
 * Created on 7/Mar/2004
 */
package ServidorApresentacao.Action.credits.serviceExemption;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoTeacher;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jpvl
 */
public class ListTeacherServiceExemptionsAction extends Action
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
        
        Object args[] = { infoTeacher.getIdInternal() };
        
        List infoServiceExemptions = (List) ServiceUtils.executeService(userView, "ReadTeacherServiceExemptions", args);
        
        request.setAttribute("infoServiceExemptions", infoServiceExemptions);
        
        return mapping.findForward("successfull-read");
    }
}
