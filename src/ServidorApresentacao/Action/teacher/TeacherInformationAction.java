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
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoTeacher;
import DataBeans.SiteView;
import DataBeans.teacher.InfoServiceProviderRegime;
import DataBeans.teacher.InfoSiteTeacherInformation;
import DataBeans.teacher.InfoWeeklyOcupation;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.ProviderRegimeType;

/**
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
	 * @param mapping
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
        InfoServiceProviderRegime infoServiceProviderRegime = getInfoServiceProviderRegimeFromForm(form);
        InfoWeeklyOcupation infoWeeklyOcupation = getInfoWeeklyOcupationFromForm(form);
        Object[] args = { infoServiceProviderRegime, infoWeeklyOcupation };
        ServiceUtils.executeService(SessionUtils.getUserView(request), getEditService(), args);
        return read(mapping, form, request, response);
    }

    /**
	 * This method creates an InfoServiceProviderRegime using the form properties.
	 * 
	 * @param form
	 * @return InfoServiceProviderRegime created
	 */
    protected InfoServiceProviderRegime getInfoServiceProviderRegimeFromForm(ActionForm form)
    {
        DynaActionForm dynaForm = (DynaActionForm) form;
        Integer teacherId = (Integer) dynaForm.get("teacherId");
        Integer serviceProviderRegimeId = (Integer) dynaForm.get("serviceProviderRegimeId");
        ProviderRegimeType providerRegimeType =
            ProviderRegimeType.getEnum((String) dynaForm.get("serviceProviderRegimeTypeName"));

        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setIdInternal(teacherId);

        InfoServiceProviderRegime infoServiceProviderRegime = new InfoServiceProviderRegime();
        infoServiceProviderRegime.setIdInternal(serviceProviderRegimeId);
        infoServiceProviderRegime.setProviderRegimeType(providerRegimeType);
        infoServiceProviderRegime.setInfoTeacher(infoTeacher);

        return infoServiceProviderRegime;
    }

    /**
	 * This method creates an InfoWeeklyOcupation using the form properties.
	 * 
	 * @param form
	 * @return InfoWeeklyOcupation created
	 */
    protected InfoWeeklyOcupation getInfoWeeklyOcupationFromForm(ActionForm form)
    {
        DynaActionForm dynaForm = (DynaActionForm) form;
        Integer teacherId = (Integer) dynaForm.get("teacherId");
        Integer weeklyOcupationId = (Integer) dynaForm.get("weeklyOcupationId");
        Integer management = new Integer((String) dynaForm.get("management"));
        Integer other = new Integer((String) dynaForm.get("other"));
        Integer research = new Integer((String) dynaForm.get("research"));

        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setIdInternal(teacherId);

        InfoWeeklyOcupation infoWeeklyOcupation = new InfoWeeklyOcupation();
        infoWeeklyOcupation.setIdInternal(weeklyOcupationId);
        infoWeeklyOcupation.setManagement(management);
        infoWeeklyOcupation.setOther(other);
        infoWeeklyOcupation.setResearch(research);
        infoWeeklyOcupation.setInfoTeacher(infoTeacher);

        return infoWeeklyOcupation;
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
	 * @param mapping
	 * @param form
	 * @param request
	 */
    protected void populateFormFromInfoWeeklyOcupationAndInfoServiceProviderRegime(
        ActionMapping mapping,
        InfoWeeklyOcupation infoWeeklyOcupation,
        InfoServiceProviderRegime infoServiceProviderRegime,
        ActionForm form,
        HttpServletRequest request)
    {
        try
        {
            BeanUtils.copyProperties(form, infoWeeklyOcupation);
            BeanUtils.copyProperties(form, infoServiceProviderRegime);

            InfoTeacher infoTeacher = infoWeeklyOcupation.getInfoTeacher();

            DynaActionForm dynaForm = (DynaActionForm) form;
            dynaForm.set("teacherId", infoTeacher.getIdInternal());
            dynaForm.set("serviceProviderRegimeId", infoServiceProviderRegime.getIdInternal());
            ProviderRegimeType providerRegimeType = infoServiceProviderRegime.getProviderRegimeType();
            dynaForm.set("serviceProviderRegimeTypeName", providerRegimeType == null ? null : providerRegimeType.getName());
            dynaForm.set("weeklyOcupationId", infoWeeklyOcupation.getIdInternal());
            dynaForm.set("management", infoWeeklyOcupation.getManagement().toString());
            dynaForm.set("research", infoWeeklyOcupation.getResearch().toString());
            dynaForm.set("other", infoWeeklyOcupation.getOther().toString());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
	 * @param mapping
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
        InfoSiteTeacherInformation infoSiteTeacherInformation =
            readInfoSiteTeacherInformation(mapping, form, request);
        if (!hasErrors(request) && infoSiteTeacherInformation != null)
        {
            InfoServiceProviderRegime infoServiceProviderRegime =
                infoSiteTeacherInformation.getInfoServiceProviderRegime();
            InfoWeeklyOcupation infoWeeklyOcupation =
                infoSiteTeacherInformation.getInfoWeeklyOcupation();
            populateFormFromInfoWeeklyOcupationAndInfoServiceProviderRegime(
                mapping,
                infoWeeklyOcupation,
                infoServiceProviderRegime,
                form,
                request);
        }
        setInfoSiteTeacherInformationToRequest(request, infoSiteTeacherInformation, mapping);
        request.setAttribute("providerRegimeTypeList", ProviderRegimeType.getEnumList());
        return mapping.findForward("show-form");
    }

    /**
	 * @param mapping
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
        InfoSiteTeacherInformation infoSiteTeacherInformation =
            readInfoSiteTeacherInformation(mapping, form, request);
        setInfoSiteTeacherInformationToRequest(request, infoSiteTeacherInformation, mapping);
        return mapping.findForward("successfull-read");
    }

    /**
	 * Reads the infoObject using de read service associated to the action
	 * 
	 * @param mapping
	 * @param form
	 * @return
	 */
    private InfoSiteTeacherInformation readInfoSiteTeacherInformation(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request)
        throws FenixServiceException
    {
        IUserView userView = SessionUtils.getUserView(request);
        Object[] args = { userView.getUtilizador()};
        SiteView siteView = (SiteView) ServiceUtils.executeService(
            userView,
            getReadService(),
            args);
        return (InfoSiteTeacherInformation) siteView.getComponent();
    }

    /**
	 * @param request
	 * @param infoObject
	 */
    private void setInfoSiteTeacherInformationToRequest(
        HttpServletRequest request,
        InfoSiteTeacherInformation infoSiteTeacherInformation,
        ActionMapping mapping)
    {
        if (infoSiteTeacherInformation != null)
        {
            request.setAttribute("infoSiteTeacherInformation", infoSiteTeacherInformation);
        }
    }
}
