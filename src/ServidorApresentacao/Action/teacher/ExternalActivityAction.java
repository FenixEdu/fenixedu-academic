/*
 * Created on 18/Nov/2003
 *  
 */
package ServidorApresentacao.Action.teacher;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoObject;
import DataBeans.InfoTeacher;
import DataBeans.teacher.InfoExternalActivity;
import ServidorApresentacao.framework.actions.CRUDActionByOID;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ExternalActivityAction extends CRUDActionByOID
{

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorApresentacao.framework.actions.CRUDActionByOID#getInfoObjectFromForm(org.apache.struts.action.ActionForm)
	 */
    protected InfoObject getInfoObjectFromForm(ActionForm form)
    {
        DynaActionForm dynaForm = (DynaActionForm) form;

        Integer externalActivityId = (Integer) dynaForm.get("externalActivityId");
        Integer teacherId = (Integer) dynaForm.get("teacherId");
        String activity = (String) dynaForm.get("activity");

        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setIdInternal(teacherId);

        InfoExternalActivity infoExternalActivity = new InfoExternalActivity();
        infoExternalActivity.setIdInternal(externalActivityId);
        infoExternalActivity.setInfoTeacher(infoTeacher);

        return infoExternalActivity;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorApresentacao.framework.actions.CRUDActionByOID#populateFormFromInfoObject(org.apache.struts.action.ActionMapping,
	 *      DataBeans.InfoObject, org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest)
	 */
    protected void populateFormFromInfoObject(
        ActionMapping mapping,
        InfoObject infoObject,
        ActionForm form,
        HttpServletRequest request)
    {
        super.populateFormFromInfoObject(mapping, infoObject, form, request);

        InfoExternalActivity infoExternalActivity = (InfoExternalActivity) infoObject;
        DynaActionForm dynaForm = (DynaActionForm) form;

        InfoTeacher infoTeacher = infoExternalActivity.getInfoTeacher();
        dynaForm.set("professionalCareerId", infoExternalActivity.getIdInternal());
        dynaForm.set("activity", infoExternalActivity.getActivity());
        dynaForm.set("teacherId", infoTeacher.getIdInternal());
    }

}
