/*
 * Created on 15/Fev/2004
 */

package ServidorApresentacao.Action.grant.contract;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.grant.contract.InfoGrantPaymentEntity;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Barbosa
 * @author Pica
 */
public class ManageGrantProjectAction extends FenixDispatchAction
{

    public ActionForward prepareManageGrantProject(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        try
        {
            Object[] args = { InfoGrantPaymentEntity.getGrantProjectOjbConcreteClass() };
            IUserView userView = SessionUtils.getUserView(request);
            List infoGrantProjectList =
                (List) ServiceUtils.executeService(userView, "ReadAllGrantPaymentEntitiesByClassName", args);

            if (infoGrantProjectList != null && !infoGrantProjectList.isEmpty())
                request.setAttribute("infoGrantProjectList", infoGrantProjectList);
            
            return mapping.findForward("manage-grant-project");
        }
        catch (FenixServiceException e)
        {
            return setError(request, mapping, "errors.grant.unrecoverable", "manage-grant-project", null);
        }
    }
}