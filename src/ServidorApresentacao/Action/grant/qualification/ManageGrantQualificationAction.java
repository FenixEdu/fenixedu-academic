/*
 * Created on 10/Dec/2003
 */

package ServidorApresentacao.Action.grant.qualification;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Barbosa
 * @author Pica
 */

public class ManageGrantQualificationAction extends DispatchAction
{
    /*
     * Fills the form with the correspondent data
     */
    public ActionForward prepareManageGrantQualificationForm(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        Integer idInternal = null;
        Integer idInternalPerson = null;

        //TODO.. verificar so que null.. ou tb verificar se são strings vazias!!!
        if (request.getParameter("idInternal") != null)
            idInternal = new Integer(request.getParameter("idInternal"));
        else if ((Integer) request.getAttribute("idInternal") != null)
            idInternal = (Integer) request.getAttribute("idInternal");
        
        //TODO.. verificar so que null.. ou tb verificar se são strings vazias!!!
        if (request.getParameter("idInternalPerson") != null)
            idInternalPerson = new Integer(request.getParameter("idInternalPerson"));
        else if ((Integer) request.getAttribute("idInternalPerson") != null)
            idInternalPerson = (Integer) request.getAttribute("idInternalPerson");
        
        //Run the service
        Object[] args = { idInternal };
        IUserView userView = SessionUtils.getUserView(request);
        List infoGrantQualificationList = null;
        //(List) ServiceUtils.executeService(userView, "ReadAllQualificationByPerson", args);
        //TODO... fazer este serviço

        //If they exist put them on request
        if (infoGrantQualificationList != null && !infoGrantQualificationList.isEmpty())
            request.setAttribute("infoGrantQualificationList", infoGrantQualificationList);

        request.setAttribute("idInternal", idInternal);
        request.setAttribute("idInternalPerson", idInternalPerson);
        
        return mapping.findForward("manage-grant-qualification");
    }
}