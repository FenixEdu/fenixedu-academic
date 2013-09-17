package net.sourceforge.fenixedu.presentationTier.Action.resourceManager;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceManager.CreateMaterial;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceManager.DeleteMaterial;
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

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.CollectionPager;

@Mapping(module = "resourceManager", path = "/materialManagement", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "editMaterial", path = "/resourceManager/materialManagement/editMaterial.jsp"),
        @Forward(name = "prepareMaterialManage", path = "/resourceManager/materialManagement/materialManagement.jsp") })
public class MaterialManagementDA extends FenixDispatchAction {

    public ActionForward prepareMaterialManage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws InvalidArgumentException {

        MaterialBean bean = new MaterialBean(null, null);
        request.setAttribute("materialBean", bean);
        return mapping.findForward("prepareMaterialManage");
    }

    public ActionForward listMaterial(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws InvalidArgumentException {

        MaterialBean bean = null;
        MaterialType materialType = null;
        String identification = null;

        Material material = getRenderedObject("materialEditID");
        if (material != null) {
            bean =
                    new MaterialBean(material.getIdentification(), MaterialType.getMaterialTypeByMaterialClass(material
                            .getClass()));
        } else {
            bean = getRenderedObject("materialBeanID");
            bean = bean != null ? bean : (MaterialBean) getRenderedObject("createMaterialBeanID");
            if (bean == null) {
                materialType = getMaterialTypeFromParameter(request);
                identification = getMaterialIdentificationFromParameter(request);
                bean = new MaterialBean(identification, materialType);
            }
        }

        return readAndSetMaterial(request, bean, mapping);
    }

    public ActionForward prepareEditMaterial(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws InvalidArgumentException {

        Material material = getMaterialFromParameter(request);
        request.setAttribute("material", material);
        return mapping.findForward("editMaterial");
    }

    public ActionForward prepareCreateMaterial(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws InvalidArgumentException {

        MaterialBean bean = getRenderedObject("prepareCreateMaterialBeanID");
        bean = bean != null ? bean : (MaterialBean) getRenderedObject("createMaterialBeanID");

        if (bean == null) {
            bean = new MaterialBean(null, null);
        }

        request.setAttribute("materialBean", bean);
        return mapping.findForward("editMaterial");
    }

    public ActionForward createMaterial(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws InvalidArgumentException, FenixServiceException {

        MaterialBean bean = getRenderedObject("createMaterialBeanID");

        try {
            CreateMaterial.run(bean);

        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            request.setAttribute("materialBean", bean);
            return mapping.findForward("editMaterial");
        }

        return readAndSetMaterial(request, bean, mapping);
    }

    public ActionForward deleteMaterial(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws InvalidArgumentException, FenixServiceException {

        Material material = getMaterialFromParameter(request);
        Class<? extends Material> materialClass = material.getClass();
        MaterialType materialType = MaterialType.getMaterialTypeByMaterialClass(materialClass);

        try {
            DeleteMaterial.run(material);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            MaterialBean bean = new MaterialBean(material.getIdentification(), materialType);
            return readAndSetMaterial(request, bean, mapping);
        }

        MaterialBean bean = new MaterialBean(null, materialType);
        return readAndSetMaterial(request, bean, mapping);
    }

    // /////

    private ActionForward readAndSetMaterial(HttpServletRequest request, MaterialBean bean, ActionMapping mapping) {

        List<Material> materialList = null;
        if (bean.getMaterialType() != null) {
            if (StringUtils.isEmpty(bean.getIdentification())) {
                materialList = Material.getAllMaterialByTypeOrderByIdentification(bean.getMaterialType().getMaterialClass());
            } else {
                Material material =
                        Material.getMaterialByTypeAndIdentification(bean.getMaterialType().getMaterialClass(),
                                bean.getIdentification());
                materialList = new ArrayList<Material>();
                if (material != null) {
                    materialList.add(material);
                }
            }
        }

        CollectionPager<Material> collectionPager =
                new CollectionPager<Material>(materialList != null ? materialList : new ArrayList<Material>(), 50);
        final String pageNumberString = request.getParameter("pageNumber");
        final Integer pageNumber =
                !StringUtils.isEmpty(pageNumberString) ? Integer.valueOf(pageNumberString) : Integer.valueOf(1);

        request.setAttribute("pageNumber", pageNumber);
        request.setAttribute("numberOfPages", Integer.valueOf(collectionPager.getNumberOfPages()));
        request.setAttribute("materials", collectionPager.getPage(pageNumber.intValue()));
        request.setAttribute("materialBean", bean);

        return mapping.findForward("prepareMaterialManage");
    }

    private Material getMaterialFromParameter(final HttpServletRequest request) {
        final String materialIDString = request.getParameter("materialID");
        return (Material) FenixFramework.getDomainObject(materialIDString);
    }

    private MaterialType getMaterialTypeFromParameter(final HttpServletRequest request) {
        final String materialType = request.getParameter("materialType");
        return materialType != null ? MaterialType.valueOf(materialType) : null;
    }

    private String getMaterialIdentificationFromParameter(final HttpServletRequest request) {
        return request.getParameter("materialIdentification");
    }
}