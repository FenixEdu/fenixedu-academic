/*
 * Created on 17/Nov/2003
 *  
 */
package ServidorApresentacao.Action.teacher;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoObject;
import DataBeans.InfoTeacher;
import DataBeans.teacher.InfoCareer;
import DataBeans.teacher.InfoCategory;
import DataBeans.teacher.InfoProfessionalCareer;
import DataBeans.teacher.InfoTeachingCareer;
import ServidorApresentacao.Action.framework.CRUDActionByOID;
import Util.CareerType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class CareerAction extends CRUDActionByOID
{

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorApresentacao.framework.actions.CRUDActionByOID#getInfoObjectFromForm(org.apache.struts.action.ActionForm)
	 */
    protected InfoObject getInfoObjectFromForm(ActionForm form)
    {
        DynaActionForm dynaForm = (DynaActionForm) form;

        Integer careerId = (Integer) dynaForm.get("careerId");
        Integer teacherId = (Integer) dynaForm.get("teacherId");
        Integer beginYear = new Integer((String) dynaForm.get("beginYear"));
        Integer endYear = new Integer((String) dynaForm.get("endYear"));
        CareerType careerType = CareerType.getEnum((String) dynaForm.get("careerType"));

        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setIdInternal(teacherId);

        InfoCareer infoCareer = null;

        if (careerType.equals(CareerType.PROFESSIONAL))
        {
            String entity = (String) dynaForm.get("entity");
            String function = (String) dynaForm.get("function");

            infoCareer = new InfoProfessionalCareer();
            infoCareer.setIdInternal(careerId);
            infoCareer.setBeginYear(beginYear);
            infoCareer.setEndYear(endYear);
            ((InfoProfessionalCareer) infoCareer).setEntity(entity);
            ((InfoProfessionalCareer) infoCareer).setFunction(function);

        } else
        {
            String courseOrPosition = (String) dynaForm.get("courseOrPosition");
            Integer categoryId = (Integer) dynaForm.get("categoryId");

            InfoCategory infoCategory = new InfoCategory();
            infoCategory.setIdInternal(categoryId);

            infoCareer = new InfoTeachingCareer();
            infoCareer.setIdInternal(careerId);
            infoCareer.setBeginYear(beginYear);
            infoCareer.setEndYear(endYear);
            ((InfoTeachingCareer) infoCareer).setCourseOrPosition(courseOrPosition);
            ((InfoTeachingCareer) infoCareer).setInfoCategory(infoCategory);
        }
        infoCareer.setInfoTeacher(infoTeacher);

        return infoCareer;
    }

    protected void populateFormFromInfoObject(
        ActionMapping mapping,
        InfoObject infoObject,
        ActionForm form,
        HttpServletRequest request)
    {
        try
        {
            super.populateFormFromInfoObject(mapping, infoObject, form, request);

            InfoCareer infoCareer = (InfoCareer) infoObject;
            DynaActionForm dynaForm = (DynaActionForm) form;

            dynaForm.set("careerId", infoCareer.getIdInternal());
            
//            dynaForm.set("beginYear", infoCareer.getBeginYear());
//            dynaForm.set("endYear", infoCareer.getEndYear());
            CareerType careerType = infoCareer.getCareerType();
            dynaForm.set("careerType", careerType.getName());

            if (careerType.equals(CareerType.PROFESSIONAL))
            {
                dynaForm.set("entity", ((InfoProfessionalCareer) infoCareer).getEntity());
                dynaForm.set("function", ((InfoProfessionalCareer) infoCareer).getFunction());
            } else
            {
                dynaForm.set(
                    "courseOrPosition",
                    ((InfoTeachingCareer) infoCareer).getCourseOrPosition());
                InfoCategory infoCategory = ((InfoTeachingCareer) infoCareer).getInfoCategory();
                dynaForm.set("categoryId", infoCategory.getIdInternal());
            }

            InfoTeacher infoTeacher = infoCareer.getInfoTeacher();
            dynaForm.set("teacherId", infoTeacher.getIdInternal());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
