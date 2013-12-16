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

import net.sourceforge.fenixedu._development.Custodian;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadNumberCachedItems;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.tools.Profiler;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.dml.DomainModel;
import pt.ist.fenixframework.dml.DomainRelation;
import pt.ist.fenixframework.dml.Role;

/**
 * @author Luis Crus & Sara Ribeiro
 */
@Mapping(module = "manager", path = "/manageCache", input = "/manageCache.do?method=prepare", attribute = "cacheForm",
        formBean = "cacheForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "Manage", path = "/manager/manageCache_bd.jsp"),
        @Forward(name = "CacheCleared", path = "/manageCache.do?method=prepare") })
public class ManageCacheDA extends FenixDispatchAction {

    /**
     * Prepare information to show existing execution periods and working areas.
     */
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Profiler.getInstance();
        Profiler.report();
        Profiler.resetInstance();

        // User userView = Authenticate.getUser();

        Integer numberCachedItems = ReadNumberCachedItems.run();

        request.setAttribute(PresentationConstants.NUMBER_CACHED_ITEMS, numberCachedItems);

        return mapping.findForward("Manage");
    }

    /**
     * Prepare information to show existing execution periods and working areas.
     */
    public ActionForward clearCache(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("CacheCleared");
    }

    public ActionForward clearResponseCache(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("CacheCleared");
    }

    public ActionForward setResponseRefreshTimeout(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("CacheCleared");
    }

    public ActionForward loadAllObjectsToCache(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final DomainModel domainModel = FenixFramework.getDomainModel();

        final long startTime = System.currentTimeMillis();
        long numberOfReadDomainObjects = 0;
        try {
            for (final Iterator<DomainRelation> domainRelationIterator = domainModel.getRelations(); domainRelationIterator
                    .hasNext();) {

                final DomainRelation domainRelation = domainRelationIterator.next();
                final Role firstRole = domainRelation.getFirstRole();
                final Role secondRole = domainRelation.getSecondRole();

                if (firstRole.getType().getFullName().equals(Bennu.class.getName())) {
                    numberOfReadDomainObjects += loadDomainObjects(secondRole);
                } else if (secondRole.getType().getFullName().equals(Bennu.class.getName())) {
                    numberOfReadDomainObjects += loadDomainObjects(firstRole);
                }
                final Integer numberCachedItems = ReadNumberCachedItems.run();
                final Runtime runtime = Runtime.getRuntime();
                System.out.println("   total read: " + numberOfReadDomainObjects + " in cache: " + numberCachedItems
                        + " free memory: " + runtime.freeMemory() + " total memory: " + runtime.totalMemory() + " max memory: "
                        + runtime.maxMemory());
            }
        } finally {
            final long endTime = System.currentTimeMillis();
            System.out
                    .println("Read all " + numberOfReadDomainObjects + " domain objects took: " + (endTime - startTime) + "ms.");
        }

        return prepare(mapping, form, request, response);
    }

    private int loadDomainObjects(final Role role) throws Exception {
        final String roleName = role.getName();
        final String methodName = "get" + Character.toUpperCase(roleName.charAt(0)) + roleName.substring(1);
        int numberOfReadDomainObjects = 0;
        try {
            final Method method = Bennu.class.getMethod(methodName, (Class[]) null);
            final Collection<DomainObject> domainObjects =
                    (Collection<DomainObject>) method.invoke(rootDomainObject, (Object[]) null);
            numberOfReadDomainObjects = domainObjects.size();
            System.out.println("Read " + numberOfReadDomainObjects + " objects from method: " + methodName);
        } catch (Throwable t) {
            System.out.println("Unable to load objects with method: " + methodName);
        }
        return numberOfReadDomainObjects;
    }

    public ActionForward dumpThreadTrace(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Custodian.registerPID();
        Custodian.dumpThreadTrace();
        return prepare(mapping, form, request, response);
    }

}