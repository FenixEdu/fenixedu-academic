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
import org.apache.struts.action.DynaActionForm;

import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.servlets.filters.cache.ResponseCacheOSCacheImpl;
import Tools.Profiler;

/**
 * @author Luis Crus & Sara Ribeiro
 */
public class ManageCacheDA extends FenixDispatchAction {

    /**
     * Prepare information to show existing execution periods and working areas.
     */
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Profiler.getInstance();
        Profiler.report();
        Profiler.resetInstance();

        //IUserView userView = SessionUtils.getUserView(request);

        Integer numberCachedItems = (Integer) ServiceUtils.executeService(null,
                "ReadNumberCachedItems", null);

        request.setAttribute(SessionConstants.NUMBER_CACHED_ITEMS, numberCachedItems);

        Integer numberCachedResponses = new Integer(ResponseCacheOSCacheImpl.getInstance()
                .getNumberOfCachedItems());
        Integer refreshTimeout = new Integer(ResponseCacheOSCacheImpl.getInstance().getRefreshTimeout());
        request.setAttribute(SessionConstants.NUMBER_CACHED_RESPONSES, numberCachedResponses);
        request.setAttribute(SessionConstants.CACHED_RESPONSES_TIMEOUT, refreshTimeout);

        DynaActionForm dynaActionForm = (DynaActionForm) form;
        dynaActionForm.set("responseRefreshTimeout", refreshTimeout);

        return mapping.findForward("Manage");
    }

    /**
     * Prepare information to show existing execution periods and working areas.
     */
    public ActionForward clearCache(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        //IUserView userView = SessionUtils.getUserView(request);

        ServiceUtils.executeService(null, "ClearCache", null);

        return mapping.findForward("CacheCleared");
    }

    public ActionForward clearResponseCache(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ResponseCacheOSCacheImpl.getInstance().clear();

        return mapping.findForward("CacheCleared");
    }

    public ActionForward setResponseRefreshTimeout(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        DynaActionForm dynaActionForm = (DynaActionForm) form;

        Integer responseRefreshTimeout = (Integer) dynaActionForm.get("responseRefreshTimeout");

        ResponseCacheOSCacheImpl.getInstance().setRefreshTimeout(responseRefreshTimeout.intValue());

        return mapping.findForward("CacheCleared");
    }

}