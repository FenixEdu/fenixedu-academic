/*
 * Created on Nov 15, 2003 by jpvl
 *  
 */
package ServidorApresentacao.Action.framework;

import java.beans.PropertyDescriptor;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoObject;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.mapping.framework.CRUDMapping;

/**
 * 
 * Example: <br>
 * 
 * <pre>
 * 	  <action path="...."  type="...."  name="..." input="..." scope="request" className="@link ServidorApresentacao.framework.actions.mappings.CRUDMapping"> 
 * 								<set-property property="editService" value="..."/>
 * 								<set-property property="deleteService" value="..."/>
 * 								<set-property property="readService" value="..."/>
 *  								<set-property property="oidProperty" value="..."/>
 * 								<forward name="show-form" path="..."/> 
 * 								<forward name="successfull-edit" path="..."/> 
 *   								<forward name="successfull-read" path="..."/>
 *   								<forward name="successfull-delete" path="..."/>
 * 		</action>
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
 * Methods availables are: <br>
 * 
 * <pre>
 * 	<b>prepareEdit</b> <br>
 *     <b>edit</b> <br>
 * 	<b>delete</b> <br>
 *     <b>read</b>
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
 * <b>Notes:</b><br>
 * 
 * <pre>
 * <li>Specializations of this class should implement method @link #populateFormFromInfoObject(ActionMapping, InfoObject, ActionForm, HttpServletRequest) and @link #getInfoObjectFromForm(ActionForm).
 * </li>
 * <li>The Struts Exception handling feature should be used to handle exceptions from the services configured.
 * </li>
 * </pre>
 * 
 * @see ServidorApresentacao.framework.actions.mappings.CRUDMapping
 * @author jpvl
 */
public class CRUDActionByOID extends DispatchAction
{
    /**
	 * Method that invokes a service that extends @link
	 * ServidorAplicacao.Servico.framework.DeleteDomainObjectService called @link
	 * CRUDMapping#getDeleteService()
	 * 
	 * @param mapping
	 *            should be an instance of @link CRUDMapping
	 * @param form
	 * @param request
	 * @param response
	 * @return forward to the action mapping configuration called successfull-delete
	 * @throws Exception
	 */
    public ActionForward delete(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        CRUDMapping crudMapping = (CRUDMapping) mapping;
        Object[] args = { getOIDProperty(crudMapping, form)};
        ServiceUtils.executeService(
            SessionUtils.getUserView(request),
            crudMapping.getDeleteService(),
            args);
        return crudMapping.findForward("successfull-delete");
    }

    /**
	 * Method that invokes a service that extends @link
	 * ServidorAplicacao.Servico.framework.EditDomainObjectService called @link
	 * CRUDMapping#getEditService()
	 * 
	 * @param mapping
	 *            should be an instance of @link CRUDMapping
	 * @param form
	 * @param request
	 * @param response
	 * @return forward to the action mapping configuration called successfull-edit
	 * @throws Exception
	 */
    public ActionForward edit(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        CRUDMapping crudMapping = (CRUDMapping) mapping;
        InfoObject infoObject = populateInfoObjectFromForm(form, crudMapping);
        Object[] args = getEditServiceArguments(form, crudMapping, infoObject, request);
        ServiceUtils.executeService(
            SessionUtils.getUserView(request),
            crudMapping.getEditService(),
            args);
        return crudMapping.findForward("successfull-edit");
    }

    protected Object[] getEditServiceArguments(
        ActionForm form,
        CRUDMapping crudMapping,
        InfoObject infoObject,
        HttpServletRequest request)
    {
        Object[] args = { getOIDProperty(crudMapping, form), infoObject };
        return args;
    }

    /**
	 * This method creates an InfoObject using the form properties.
	 * 
	 * @param form
	 * @return InfoObject created
	 */
    protected InfoObject populateInfoObjectFromForm(ActionForm form, CRUDMapping mapping)
        throws FenixActionException
    {
        try
        {
            InfoObject infoObject =
                (InfoObject) Class.forName(mapping.getInfoObjectClassName()).newInstance();

            Map formPropertiesHashMap = PropertyUtils.describe(form);
            Iterator iterator = formPropertiesHashMap.entrySet().iterator();
            while (iterator.hasNext())
            {
                Map.Entry entry = (Map.Entry) iterator.next();
                String formProperty = (String) entry.getKey();
                StringTokenizer propertyTokenizer = new StringTokenizer(formProperty, "#");

                if (propertyTokenizer.countTokens() == 1)
                {
                    if (PropertyUtils.isWriteable(infoObject, formProperty))
                    {
                        BeanUtils.copyProperty(infoObject, formProperty, entry.getValue());
                    }
                } else
                {
                    Object value = null;
                    while (propertyTokenizer.hasMoreElements())
                    {
                        String property = propertyTokenizer.nextToken();

                        PropertyDescriptor propertyDescriptor =
                            PropertyUtils.getPropertyDescriptor(infoObject, property);
                        if (value == null)
                        {
                            value = propertyDescriptor.getPropertyType().newInstance();
                            if (PropertyUtils.isWriteable(infoObject, property))
                            {
                                PropertyUtils.setProperty(infoObject, property, value);
                            } else
                            {
                                break;
                            }
                        } else
                        {
                            if (PropertyUtils.isWriteable(value, property))
                            {
                                PropertyUtils.setProperty(value, property, entry.getValue());
                            }
                        }
                    }
                }
            }
            return infoObject;
        } catch (Exception e)
        {
            throw new FenixActionException(e);
        }
    }

    /**
	 * @param crudMapping
	 * @param form
	 * @return
	 */
    private Integer getOIDProperty(CRUDMapping crudMapping, ActionForm form)
    {
        try
        {
            return new Integer(BeanUtils.getProperty(form, crudMapping.getOidProperty()));
        } catch (NumberFormatException e)
        {
        } catch (Exception e)
        {
        }
        return null;
    }

    /**
	 * Tests if errors are presented.
	 * 
	 * @param request
	 * @return
	 */
    private boolean hasErrors(HttpServletRequest request)
    {

        return request.getAttribute(Globals.ERROR_KEY) != null;
    }

    /**
	 * By default, it does a @link BeanUtils#copyProperties(java.lang.Object, java.lang.Object)
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 */
    protected void populateFormFromInfoObject(
        ActionMapping mapping,
        InfoObject infoObject,
        ActionForm form,
        HttpServletRequest request)
        throws FenixActionException
    {
        try
        {

            Map formPropertiesHashMap = BeanUtils.describe(form);

            Iterator iterator = formPropertiesHashMap.entrySet().iterator();

            while (iterator.hasNext())
            {
                Map.Entry entry = (Map.Entry) iterator.next();
                String formProperty = (String) entry.getKey();
                String beanProperty = formProperty.replace('#', '.');
                if (PropertyUtils.isReadable(infoObject, beanProperty))
                {
                    Object value = PropertyUtils.getProperty(infoObject, beanProperty);
                    BeanUtils.copyProperty(form, formProperty, value);
                }
            }
        } catch (Exception e1)
        {
            
            e1.printStackTrace(System.out);
            throw new FenixActionException(e1);
        }
    }

    /**
	 * Method that invokes a service that extends @link
	 * ServidorAplicacao.Servico.framework.ReadDomainObjectService called @link
	 * CRUDMapping#getReadService() Sets an attribute @link CRUDMapping#getRequestAttribute() with the
	 * infoObject readed.
	 * 
	 * @param mapping
	 *            should be an instance of @link CRUDMapping
	 * @param form
	 * @param request
	 * @param response
	 * @return forward to the action mapping configuration called show-form
	 * @throws Exception
	 */
    public ActionForward prepareEdit(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        CRUDMapping crudMapping = (CRUDMapping) mapping;
        InfoObject infoObject = readInfoObject(crudMapping, form, request);
        if (!hasErrors(request) && infoObject != null)
        {
            populateFormFromInfoObject(mapping, infoObject, form, request);
        }
        prepareFormConstants(mapping, form, request);
        setInfoObjectToRequest(request, infoObject, crudMapping);
        return crudMapping.findForward("show-form");
    }

    /**
	 * Should read all constants needed for form. By default do nothing.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 */
    protected void prepareFormConstants(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request) throws FenixServiceException
    {
    }

    /**
	 * Method that invokes a service that extends @link
	 * ServidorAplicacao.Servico.framework.ReadDomainObjectService called @link
	 * CRUDMapping#getReadService()
	 * 
	 * @param mapping
	 *            should be an instance of @link CRUDMapping
	 * @param form
	 * @param request
	 * @param response
	 * @return forward to the action mapping configuration called successfull-read
	 * @throws Exception
	 */
    public ActionForward read(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        CRUDMapping crudMapping = (CRUDMapping) mapping;
        InfoObject infoObject = readInfoObject(crudMapping, form, request);
        setInfoObjectToRequest(request, infoObject, crudMapping);
        return crudMapping.findForward("sucessfull-read");
    }

    /**
	 * Reads the infoObject using de read service associated to the action
	 * 
	 * @param crudMapping
	 * @param form
	 * @return
	 */
    private InfoObject readInfoObject(
        CRUDMapping crudMapping,
        ActionForm form,
        HttpServletRequest request)
        throws FenixServiceException
    {
        IUserView userView = SessionUtils.getUserView(request);
        Integer oid = getOIDProperty(crudMapping, form);
        InfoObject infoObject = null;
        if (oid != null)
        {
            Object[] args = { getOIDProperty(crudMapping, form)};
            System.out.println("OLA1");
            infoObject =
                (InfoObject) ServiceUtils.executeService(userView, crudMapping.getReadService(), args);
            System.out.println("OLA2");
        }
        return infoObject;
    }

    /**
	 * @param request
	 * @param infoObject
	 */
    private void setInfoObjectToRequest(
        HttpServletRequest request,
        InfoObject infoObject,
        CRUDMapping crudMapping)
    {
        if (infoObject != null)
        {
            request.setAttribute(crudMapping.getRequestAttribute(), infoObject);
        }
    }
}
