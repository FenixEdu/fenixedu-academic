/*
 * Created on 11/Jun/2004
 *
 */
package ServidorApresentacao.Action.person;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * 
 *	@author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali</a>
 *	@author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed</a>
 * 
 */
public class ViewSmsDeliveryReportsAction extends FenixAction
{

	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
	throws FenixActionException
	{
		
		IUserView userView = SessionUtils.getUserView(request);
		
		List infoSentSmsList = null;
		
		Object args[] = { userView };
		
		try
		{
			infoSentSmsList = (List) ServiceUtils.executeService(userView, "ReadSentSmsByPerson", args);
			request.setAttribute(SessionConstants.LIST_SMS_DELIVERY_REPORTS, infoSentSmsList);
		}
		catch (FenixServiceException e)
		{
		}		
		
		return mapping.findForward("start");
		
	}
	

}
