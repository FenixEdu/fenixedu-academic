/*
 * Created on 10/Dec/2003
 */

package ServidorApresentacao.Action.grant.qualification;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.person.InfoSiteQualifications;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Barbosa
 * @author Pica
 */

public class ManageGrantQualificationAction extends FenixDispatchAction
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
		try
		{
			Integer idInternal = null;
			Integer idInternalPerson = null;
			String username = null;

			if (request.getParameter("idInternal") != null)
				idInternal = new Integer(request.getParameter("idInternal"));
			if (request.getParameter("idPerson") != null)
				idInternalPerson = new Integer(request.getParameter("idPerson"));
			username = request.getParameter("username");

			Object[] args = { username };
			IUserView userView = SessionUtils.getUserView(request);
			InfoSiteQualifications infoSiteQualifications =
				(InfoSiteQualifications) ServiceUtils.executeService(
					userView,
					"ReadQualifications",
					args);

			if (infoSiteQualifications != null)
			{
				if (infoSiteQualifications.getInfoPerson() != null)
					request.setAttribute("grantOwnerName",infoSiteQualifications.getInfoPerson().getNome());
				if (infoSiteQualifications.getInfoQualifications() != null
					&& !infoSiteQualifications.getInfoQualifications().isEmpty())
					request.setAttribute("infoQualificationList",infoSiteQualifications.getInfoQualifications());
			}
			request.setAttribute("idInternal", idInternal);
			request.setAttribute("idPerson", idInternalPerson);
			request.setAttribute("username", username);
		}
		catch (Exception e)
		{
			return setError(request,mapping,"errors.grant.unrecoverable","manage-grant-qualification",null);
		}
		return mapping.findForward("manage-grant-qualification");
	}
}