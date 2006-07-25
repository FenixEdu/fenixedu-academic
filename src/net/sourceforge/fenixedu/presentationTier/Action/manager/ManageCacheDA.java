/*
 * Created on 2003/08/08
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.MetadataManager;
import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.cache.ResponseCacheOSCacheImpl;
import net.sourceforge.fenixedu.stm.Transaction;
import net.sourceforge.fenixedu.tools.Profiler;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import dml.DomainModel;
import dml.DomainRelation;
import dml.Role;

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

    public ActionForward loadAllObjectsToCache(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {

    	final DomainModel domainModel = MetadataManager.getDomainModel();

    	final long startTime = System.currentTimeMillis();
    	long numberOfReadDomainObjects = 0;
    	try {
    		for (final Iterator<DomainRelation> domainRelationIterator = domainModel.getRelations();
    				domainRelationIterator.hasNext(); ) {

    			final DomainRelation domainRelation = domainRelationIterator.next();
    			final Role firstRole = domainRelation.getFirstRole();
    			final Role secondRole = domainRelation.getSecondRole();

    			if (firstRole.getType().getFullName().equals(RootDomainObject.class.getName())) {
    				numberOfReadDomainObjects += loadDomainObjects(secondRole);
    			} else if (secondRole.getType().getFullName().equals(RootDomainObject.class.getName())) {
    				numberOfReadDomainObjects += loadDomainObjects(firstRole);
    			}
                final Integer numberCachedItems = (Integer) ServiceUtils.executeService(null, "ReadNumberCachedItems", null);
                final Runtime runtime = Runtime.getRuntime();
                System.out.println("   total read: " + numberOfReadDomainObjects
                        + " in cache: " + numberCachedItems
                        + " free memory: " + runtime.freeMemory()
                        + " total memory: " + runtime.totalMemory()
                        + " max memory: " + runtime.maxMemory());
    		}
    	} finally {
    		final long endTime = System.currentTimeMillis();
    		System.out.println("Read all " + numberOfReadDomainObjects + " domain objects took: " + (endTime - startTime) + "ms.");
    	}

        return prepare(mapping, form, request, response);
    }

	private int loadDomainObjects(final Role role) throws Exception {
		final String roleName = role.getName();
		final String methodName = "get" + Character.toUpperCase(roleName.charAt(0)) + roleName.substring(1);
		int numberOfReadDomainObjects = 0;
		try {
			final Method method = RootDomainObject.class.getMethod(methodName, (Class[]) null);
			final Collection<DomainObject> domainObjects = (Collection<DomainObject>) method.invoke(rootDomainObject, (Object[]) null);
			numberOfReadDomainObjects = domainObjects.size();
			System.out.println("Read " + numberOfReadDomainObjects + " objects from method: " + methodName);
		} catch (Throwable t) {
			System.out.println("Unable to load objects with method: " + methodName);			
		} finally {
			final IUserView userView = AccessControl.getUserView();
			Transaction.forceFinish();
            Transaction.begin();
            Transaction.currentFenixTransaction().setReadOnly();
            AccessControl.setUserView(userView);
		}
        return numberOfReadDomainObjects;
	}

}