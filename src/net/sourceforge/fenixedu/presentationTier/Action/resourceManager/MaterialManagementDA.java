package net.sourceforge.fenixedu.presentationTier.Action.resourceManager;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.resourceManager.MaterialBean;
import net.sourceforge.fenixedu.dataTransferObject.resourceManager.MaterialBean.MaterialType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.material.Material;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.jcs.access.exception.InvalidArgumentException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.util.CollectionPager;

public class MaterialManagementDA extends FenixDispatchAction {

    public ActionForward prepareMaterialManage(ActionMapping mapping, ActionForm form,	   	    
	    HttpServletRequest request, HttpServletResponse response) throws InvalidArgumentException {

	MaterialBean bean = new MaterialBean(null, null);
	request.setAttribute("materialBean", bean);	
	return mapping.findForward("prepareMaterialManage");	
    }
    
    public ActionForward listMaterial(ActionMapping mapping, ActionForm form,	   	    
	    HttpServletRequest request, HttpServletResponse response) throws InvalidArgumentException {
		
	MaterialBean bean = null;
	MaterialType materialType = null;
	String identification = null;

	Material material = (Material) getRenderedObject("materialEditID");
	if(material != null) {
	    bean = new MaterialBean(material.getIdentification(), MaterialType.getMaterialTypeByMaterialClass(material.getClass()));
	} else {	
	    bean = (MaterialBean) getRenderedObject("materialBeanID");
	    bean = bean != null ? bean : (MaterialBean) getRenderedObject("createMaterialBeanID");
	    if(bean == null) {	    
		materialType = getMaterialTypeFromParameter(request);
		identification = getMaterialIdentificationFromParameter(request);
		bean = new MaterialBean(identification, materialType);	    
	    }
	}
	
	return readAndSetMaterial(request, bean, mapping);	
    }
          
    public ActionForward prepareEditMaterial(ActionMapping mapping, ActionForm form,	   	    
	    HttpServletRequest request, HttpServletResponse response) throws InvalidArgumentException {

	Material material = getMaterialFromParameter(request);	
	request.setAttribute("material", material);	
	return mapping.findForward("editMaterial");	
    }
        
    public ActionForward prepareCreateMaterial(ActionMapping mapping, ActionForm form,	   	    
	    HttpServletRequest request, HttpServletResponse response) throws InvalidArgumentException {

	MaterialBean bean = (MaterialBean) getRenderedObject("prepareCreateMaterialBeanID");
	bean = bean != null ? bean : (MaterialBean) getRenderedObject("createMaterialBeanID");
	
	if(bean == null) {	    
	    bean = new MaterialBean(null, null);
	}
	
	request.setAttribute("materialBean", bean);	
	return mapping.findForward("editMaterial");	
    }
    
    public ActionForward createMaterial(ActionMapping mapping, ActionForm form,	   	    
	    HttpServletRequest request, HttpServletResponse response) throws InvalidArgumentException, FenixFilterException, FenixServiceException {

	MaterialBean bean = (MaterialBean) getRenderedObject("createMaterialBeanID");	
	
	try {
	    executeService(request, "CreateMaterial", new Object[]{bean});   
	
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	    request.setAttribute("materialBean", bean);	
	    return mapping.findForward("editMaterial");
	}
	
	return readAndSetMaterial(request, bean, mapping);	
    }
        
    public ActionForward deleteMaterial(ActionMapping mapping, ActionForm form,	   	    
	    HttpServletRequest request, HttpServletResponse response) throws InvalidArgumentException, FenixFilterException, FenixServiceException {

	Material material = getMaterialFromParameter(request);
	Class<? extends Material> materialClass = material.getClass();
	MaterialType materialType = MaterialType.getMaterialTypeByMaterialClass(materialClass);
	
	try {
	    executeService(request, "DeleteMaterial", new Object[]{material});
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	    MaterialBean bean = new MaterialBean(material.getIdentification(), materialType);
	    return readAndSetMaterial(request, bean, mapping);	    	    
	}	
			
	MaterialBean bean = new MaterialBean(null, materialType);	
	return readAndSetMaterial(request, bean, mapping);			
    }  
        
    ///////
    
    private ActionForward readAndSetMaterial(HttpServletRequest request, MaterialBean bean, ActionMapping mapping) {
	
	List<Material> materialList = null;	
	if(bean.getMaterialType() != null) {
	    if(StringUtils.isEmpty(bean.getIdentification())) {
		materialList = Material.getAllMaterialByTypeOrderByIdentification(bean.getMaterialType().getMaterialClass());	
	    } else {
		Material material = Material.getMaterialByTypeAndIdentification(bean.getMaterialType().getMaterialClass(), bean.getIdentification());
		materialList = new ArrayList<Material>();
		if(material != null) {
		    materialList.add(material);
		}
	    }
	}
		
	CollectionPager<Material> collectionPager = new CollectionPager<Material>(materialList != null ? materialList : new ArrayList<Material>(), 50);
	final String pageNumberString = request.getParameter("pageNumber");
	final Integer pageNumber = !StringUtils.isEmpty(pageNumberString) ? Integer.valueOf(pageNumberString) : Integer.valueOf(1); 	
	
	request.setAttribute("pageNumber", pageNumber);
	request.setAttribute("numberOfPages", Integer.valueOf(collectionPager.getNumberOfPages()));	
	request.setAttribute("materials", collectionPager.getPage(pageNumber.intValue()));
	request.setAttribute("materialBean", bean);
	
	return mapping.findForward("prepareMaterialManage");	
    }   
    
    private Material getMaterialFromParameter(final HttpServletRequest request) {
	final String materialIDString = request.getParameter("materialID");
	final Integer materialID = materialIDString != null ? Integer.valueOf(materialIDString) : null;
	return (Material) rootDomainObject.readResourceByOID(materialID);
    }
    
    private MaterialType getMaterialTypeFromParameter(final HttpServletRequest request) {
	final String materialType = request.getParameter("materialType");
	return materialType != null ? MaterialType.valueOf(materialType) : null;	
    }
    
    private String getMaterialIdentificationFromParameter(final HttpServletRequest request) {
	return request.getParameter("materialIdentification");	
    }
}
