/*
 * Created on Nov 15, 2003
 *  
 */
package ServidorApresentacao.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoObject;
import DataBeans.teacher.InfoServiceProviderRegime;
import DataBeans.teacher.InfoWeeklyOcupation;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.framework.actions.mappings.CRUDMapping;

/**
 * 
 * @author Leonor Almeida
 * @author Sergio Montelobo
 */
public class TeacherInformationAction extends DispatchAction
{
    private String getReadService()
    {
        return "ReadTeacherInformation";
    }
    
    private String getEditService()
    {
        return "EditTeacherInformation";
    }
    
    /**
     * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return forward to the action mapping configuration called
	 *              successfull-edit
	 * @throws Exception
	 */
    public ActionForward edit(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        InfoServiceProviderRegime infoServiceProviderRegime = getInfoServiceProviderRegimeFromForm(form);
        InfoWeeklyOcupation infoWeeklyOcupation = getInfoWeeklyOcupationFromForm(form);
        Object[] args = { infoServiceProviderRegime, infoWeeklyOcupation };
        ServiceUtils.executeService(
            SessionUtils.getUserView(request),
            getEditService(),
            args);
        return mapping.findForward("successfull-edit");
    }

    /**
	 * This method creates an InfoServiceProviderRegime using the form properties.
	 * 
	 * @param form
	 * @return InfoServiceProviderRegime created
	 */
    protected InfoServiceProviderRegime getInfoServiceProviderRegimeFromForm(ActionForm form)
    {
        return new InfoServiceProviderRegime();
        
        // TODO: falta acabar
    }
    
    /**
     * This method creates an InfoWeeklyOcupation using the form properties.
     * 
     * @param form
     * @return InfoWeeklyOcupation created
     */
    protected InfoWeeklyOcupation getInfoWeeklyOcupationFromForm(ActionForm form)
    {
        return new InfoWeeklyOcupation();
        
        // TODO: falta acabar
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
    {
        try
        {
            BeanUtils.copyProperties(form, infoObject);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return forward to the action mapping configuration called show-form
	 * @throws Exception
	 */
//    public ActionForward prepareEdit(
//        ActionMapping mapping,
//        ActionForm form,
//        HttpServletRequest request,
//        HttpServletResponse response)
//        throws Exception
//    {
//        InfoObject infoObject = readInfoObject(mapping, form, request);
//        if (!hasErrors(request) && infoObject != null)
//        {
//            populateFormFromInfoObject(mapping, infoObject, form, request);
//        }
//        setInfoObjectToRequest(request, infoObject, mapping);
//        return mapping.findForward("show-form");
//    }

    /**
	 * Method that invokes a service that extends @link
	 * ServidorAplicacao.Servico.framework.ReadDomainObjectService called @link
	 * CRUDMapping#getReadService()
	 * 
	 * @param mapping
	 *                   should be an instance of @link CRUDMapping
	 * @param form
	 * @param request
	 * @param response
	 * @return forward to the action mapping configuration called
	 *              successfull-read
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
        Object[] args = { /*getOIDProperty(crudMapping, form)*/};
        return (InfoObject) ServiceUtils.executeService(userView, crudMapping.getReadService(), args);
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
