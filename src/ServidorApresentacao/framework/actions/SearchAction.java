/*
 * Created on Nov 15, 2003 by jpvl
 *  
 */
package ServidorApresentacao.framework.actions;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoObject;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.framework.actions.mappings.SearchActionMapping;

/**
 * Example:
 * <pre>
 * 	  <action path="...."  type="...."  name="..." input="..." scope="request" className="ServidorApresentacao.framework.actions.mappings.SearchActionMapping"> 
 * 								<set-property property="serviceName" value="..."/>
 * 								<set-property property="objectAttribute" value="..."/>
 * 								<set-property property="listAttribute" value="..."/>
 *  								<set-property property="notFoundMessageKey" value="..."/>
 * 								<forward name="list-one" path="..."/> 
 *   								<forward name="list-many" path="..."/>
 * 		</action>
 * </pre>
 * 
 * The properties are:
 * <ul>
 * <li><b>serviceName</b> is the service that do the search @link
 * ServidorAplicacao.Servico.framework.SearchService</li>
 * <li><b>objectAttribute</b> if search result size = 1 is the name of request attribute where the
 * object found will be placed.</li>
 * <li><b>listAttribute</b> if more than one object is found is the name of request attribute where
 * the collection will be placed.</li>
 * <li><b>notFoundMessageKey</b> key used to notify user that nothing was found.</li>
 * </u>
 * 
 * <b>Notes:</b><br>
 * <pre>
 * <li>The Struts Exception handling feature should be used to handle exceptions from the services configured.
 * </li>
 * </pre>
 * 
 * @see ServidorApresentacao.framework.actions.mappings.SearchActionMapping
 * @author jpvl
 */
public abstract class SearchAction extends Action
{
    /**
	 * Invokes the service <code>serviceName</code>.
	 * @param mapping
	 *                   should be an instance of @see ServidorApresentacao.framework.actions.mappings.SearchActionMapping
	 * @param form
	 * @param request
	 * @param response
	 * @return 
	 * 	if search result size equals to 1 returns <code>list-one</code> forward.<br> 
	 * 	if search result size greater then 1 returns <code>list-many</code> forward. <br>
	 * 	if search result size equals to 0 returns <code>inputForward</code> 		   
	 * 		
	 * @throws Exception
	 * 
	 * TODO: Some verifications should be done... not tested yet
	 */
    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        SearchActionMapping searchActionMapping = (SearchActionMapping) mapping;
        String serviceName = searchActionMapping.getServiceName();
        IUserView userView = SessionUtils.getUserView(request);
        Map formProperties = BeanUtils.describe(form);
        Object[] args = { formProperties };
        Collection result = (Collection) ServiceUtils.executeService(userView, serviceName, args);
        ActionForward actionForward = null;
        if (result.isEmpty())
        {
            ActionErrors errors = new ActionErrors();
            String notMessageKey = searchActionMapping.getNotFoundMessageKey();
            ActionError error = new ActionError(notMessageKey);
            errors.add(notMessageKey, error);
            saveErrors(request, errors);
            actionForward = mapping.getInputForward();
        } else if (result.size() == 1)
        {
            Iterator iterator = result.iterator();
            while (iterator.hasNext())
            {
                InfoObject infoObject = (InfoObject) iterator.next();
                request.setAttribute(searchActionMapping.getObjectAttribute(), infoObject);
                break;
            }
            actionForward = mapping.findForward("list-one");
        } else
        {
            request.setAttribute(searchActionMapping.getListAttribute(), result);
            actionForward = mapping.findForward("list-many");
        }
        return actionForward;
    }
}
