/*
 * Created on 2003/08/08
 * 
 */
package ServidorApresentacao.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Luis Crus & Sara Ribeiro
 */
public class ManageCacheDA extends FenixDispatchAction {

	/**
	 * Prepare information to show existing execution periods
	 * and working areas.
	 **/
	public ActionForward prepare(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		IUserView userView = SessionUtils.getUserView(request);

		Integer numberCachedItems =
			(Integer) ServiceUtils.executeService(userView, "ReadNumberCachedItems", null);

		request.setAttribute(
			SessionConstants.NUMBER_CACHED_ITEMS,
			numberCachedItems);

		return mapping.findForward("Manage");
	}

	/**
	 * Prepare information to show existing execution periods
	 * and working areas.
	 **/
	public ActionForward clearCache(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		IUserView userView = SessionUtils.getUserView(request);

		ServiceUtils.executeService(userView, "ClearCache", null);

		return mapping.findForward("CacheCleared");
	}

}
