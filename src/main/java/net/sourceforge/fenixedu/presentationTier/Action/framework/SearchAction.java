/*
 * Created on Nov 15, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.framework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionYears;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

/**
 * Example:
 * 
 * <pre>
 * 
 * 
 * 
 *    	  &lt;action path=&quot;....&quot;  type=&quot;....&quot;  name=&quot;...&quot; input=&quot;...&quot; scope=&quot;request&quot; className=&quot;presentationTier.mapping.framework.SearchActionMapping&quot;&gt; 
 *    								&lt;set-property property=&quot;serviceName&quot; value=&quot;...&quot;/&gt;
 *    								&lt;set-property property=&quot;objectAttribute&quot; value=&quot;...&quot;/&gt;
 *    								&lt;set-property property=&quot;listAttribute&quot; value=&quot;...&quot;/&gt;
 *     								&lt;set-property property=&quot;notFoundMessageKey&quot; value=&quot;...&quot;/&gt;
 *    								&lt;forward name=&quot;search-form&quot; path=&quot;...&quot;/&gt;* 
 *    								&lt;forward name=&quot;list-one&quot; path=&quot;...&quot;/&gt; 
 *      								&lt;forward name=&quot;list-many&quot; path=&quot;...&quot;/&gt;
 *    		&lt;/action&gt;
 * 
 * 
 * 
 * </pre>
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * The properties are:
 * <ul>
 * <li><b>serviceName </b> is the service that do the search
 * 
 * @link ServidorAplicacao.Servico.framework.SearchService</li> <li>
 *       <b>objectAttribute </b> if search result size = 1 is the name of request attribute where the object found will be placed.
 *       </li> <li>
 *       <b>listAttribute </b> if more than one object is found is the name of request attribute where the collection will be
 *       placed.</li> <li>
 *       <b>notFoundMessageKey </b> key used to notify user that nothing was found.</li> </u>
 * 
 *       <b>Notes: </b> <br/>
 * 
 *       <pre>
 * 
 *       <li>
 * 
 * 
 *       The Struts Exception handling feature should be used to handle exceptions from the services configured.
 * 
 * 
 * 
 *       </li>
 * 
 *       </pre>
 * 
 */
public abstract class SearchAction extends FenixDispatchAction {

    private Comparator defaultBeanComparator;

    private Boolean defaultComparatorInitialized = Boolean.FALSE;

    /**
     * @param string
     * @return
     */
    private Comparator buildComparator(String sortby) {
        ComparatorChain comparatorChain = null;
        if ((sortby != null) && (!sortby.equals(""))) {
            StringTokenizer stringTokenizer = new StringTokenizer(sortby, ",");
            comparatorChain = new ComparatorChain();
            while (stringTokenizer.hasMoreElements()) {
                String property = stringTokenizer.nextToken();
                BeanComparator beanComparator = new BeanComparator(property);
                comparatorChain.addComparator(beanComparator);
            }
        }
        return comparatorChain;
    }

    /**
     * Invokes the service <code>serviceName</code>.
     * 
     * @param mapping
     *            should be an instance of
     * @param form
     * @param request
     * @param response
     * @return if search result size equals to 1 returns <code>list-one</code> forward. <br/>
     *         if search result size greater then 1 returns <code>list-many</code> forward. <br/>
     *         if search result size equals to 0 returns <code>inputForward</code>
     * 
     * @throws Exception
     * 
     *             TODO: Some verifications should be done... not tested yet
     */
    public ActionForward doSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Comparator beanComparator = getDefaultBeanComparator();
        return doSearch(mapping, form, request, response, beanComparator);
    }

    protected abstract Collection searchIt(ActionForm form, HttpServletRequest request) throws Exception;

    protected abstract String getObjectAttribute();

    protected abstract String getListAttribute();

    protected abstract String getNotFoundMessageKey();

    private ActionForward doSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, Comparator comparator) throws Exception {
        Collection result = searchIt(form, request);
        result = treateServiceResult(mapping, request, result);
        ActionForward actionForward = null;
        if (result.isEmpty()) {
            ActionErrors errors = new ActionErrors();
            String notMessageKey = getNotFoundMessageKey();
            ActionError error = new ActionError(notMessageKey);
            errors.add(notMessageKey, error);
            saveErrors(request, errors);
            actionForward = mapping.getInputForward();
        } else if (result.size() == 1) {
            Iterator iterator = result.iterator();
            while (iterator.hasNext()) {
                Object object = iterator.next();
                request.setAttribute(getObjectAttribute(), object);
                break;
            }
            actionForward = mapping.findForward("list-one");
        } else {
            actionForward = mapping.findForward("list-many");
        }
        if (comparator != null) {
            Collections.sort((List) result, comparator);
        }

        prepareFormConstants(mapping, request, form);
        materializeSearchCriteria(mapping, request, form);
        doAfterSearch(mapping, request, result);
        request.setAttribute(getListAttribute(), result);

        return actionForward;
    }

    protected abstract String getDefaultSortBy();

    /**
     * After the execution of the service we may want to use the result for
     * something.
     * 
     * @param mapping
     * @param request
     * @param result
     */
    protected void doAfterSearch(ActionMapping mapping, HttpServletRequest request, Collection result) throws Exception {
    }

    /**
     * After the execution of the service, and before setting the mapping, treat
     * the result of the service
     * 
     * @param mapping
     * @param request
     * @param result
     */
    protected Collection treateServiceResult(ActionMapping mapping, HttpServletRequest request, Collection result)
            throws Exception {
        return result;
    }

    /**
     * If we search an object(s) using some criteria (for instance the
     * externalId) we may want to use it after the search. This method
     * transforms the search criteria (the externalId) into an object set in the
     * request.
     * 
     * @param mapping
     * @param request
     * @param form
     */
    protected void materializeSearchCriteria(ActionMapping mapping, HttpServletRequest request, ActionForm form) throws Exception {
    }

    /**
     * @param searchActionMapping
     * @return
     */
    private Comparator getDefaultBeanComparator() {
        if (!defaultComparatorInitialized.booleanValue()) {
            synchronized (defaultComparatorInitialized) {
                if (!defaultComparatorInitialized.booleanValue()) {
                    defaultBeanComparator = buildComparator(getDefaultSortBy());
                    defaultComparatorInitialized = Boolean.TRUE;
                }
            }
        }
        return defaultBeanComparator;
    }

    /**
     * By default does nothing. Should be used to load constants on any scope
     * and initialize form
     * 
     * @param mapping
     * @param request
     * @param form
     */
    protected void prepareFormConstants(ActionMapping mapping, HttpServletRequest request, ActionForm form) throws Exception {
    }

    /**
     * Shows the form that performs the search
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return @throws Exception
     */
    public ActionForward searchForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        prepareFormConstants(mapping, request, form);
        return mapping.findForward("search-form");
    }

    /**
     * Uses the request parameter sortBy to sort the result. Delegates on method
     * 
     * @see SearchAction#doSearch(SearchActionMapping, ActionForm, HttpServletRequest, HttpServletResponse, Comparator)
     */
    public ActionForward sortBy(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String sortBy = request.getParameter("sortBy");
        ActionForward actionForward = null;
        if (sortBy != null) {
            BeanComparator beanComparator = new BeanComparator(sortBy);
            actionForward = doSearch(mapping, form, request, response, beanComparator);
        } else {
            actionForward = doSearch(mapping, form, request, response);
        }
        return actionForward;
    }

    public ActionForward doBeforeSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // execution years
        List executionYears = ReadNotClosedExecutionYears.run();

        if (executionYears != null && !executionYears.isEmpty()) {
            ComparatorChain comparator = new ComparatorChain();
            comparator.addComparator(new BeanComparator("year"), true);
            Collections.sort(executionYears, comparator);

            List executionYearLabels = buildLabelValueBeanForJsp(executionYears);
            request.setAttribute("executionYears", executionYearLabels);
        }
        request.setAttribute("showNextSelects", "false");
        return mapping.findForward("search-form");
    }

    protected List buildLabelValueBeanForJsp(List infoExecutionYears) {
        List executionYearLabels = new ArrayList();
        CollectionUtils.collect(infoExecutionYears, new Transformer() {
            @Override
            public Object transform(Object arg0) {
                InfoExecutionYear infoExecutionYear = (InfoExecutionYear) arg0;

                LabelValueBean executionYear = new LabelValueBean(infoExecutionYear.getYear(), infoExecutionYear.getYear());
                return executionYear;
            }
        }, executionYearLabels);
        return executionYearLabels;
    }

}