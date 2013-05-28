package net.sourceforge.fenixedu.presentationTier.Action.manager.organizationalStructure;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement.CreateUnitSite;
import net.sourceforge.fenixedu.dataTransferObject.VariantBean;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.contents.Portal;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.webSiteManager.CustomUnitSiteManagementDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

@Mapping(module = "manager", path = "/unitSiteManagement", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "chooseManagers", path = "/manager/organizationalStructureManagament/unitSites/editSiteManagers.jsp"),
        @Forward(name = "createEntryPoint", path = "/manager/organizationalStructureManagament/unitSites/createEntryPoint.jsp"),
        @Forward(name = "showUnits", path = "/manager/organizationalStructureManagament/unitSites/showUnits.jsp",
                tileProperties = @Tile(head = "/commons/renderers/treeRendererHeader.jsp")) })
public class UnitSiteManagementDA extends CustomUnitSiteManagementDA {

    @Override
    protected Unit getUnit(HttpServletRequest request) {
        Unit unit = super.getUnit(request);

        if (unit != null) {
            return unit;
        }

        Integer unitId = getExternalId(request, "unitID");
        return (Unit) RootDomainObject.getInstance().readPartyByOID(unitId);
    }

    @Override
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("units", Collections.singleton(RootDomainObject.getInstance().getInstitutionUnit()));
        return mapping.findForward("showUnits");
    }

    public ActionForward createSite(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {
        Unit unit = getUnit(request);

        try {
            CreateUnitSite.run(unit);
        } catch (DomainException e) {
            addActionMessage("error", request, e.getKey(), e.getArgs());
        }

        return prepare(mapping, actionForm, request, response);
    }

    @Override
    protected String getAuthorNameForFile(HttpServletRequest request) {
        return null;
    }

    public ActionForward prepareCreateEntryPoint(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("multiLanguageStringBean", getMultiLanguageString());
        request.setAttribute("siteOid", getSite(request).getExternalId());
        return mapping.findForward("createEntryPoint");
    }

    public ActionForward createEntryPoint(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        VariantBean multilanguagebean = getMultiLanguageString();
        Site site = getSite(request);
        Portal.getRootPortal().addContentJump(site, multilanguagebean.getMLString());
        return prepare(mapping, actionForm, request, response);
    }

    private VariantBean getMultiLanguageString() {
        VariantBean variantBean = getRenderedObject();
        if (variantBean == null) {
            variantBean = new VariantBean();
            variantBean.setMLString(new MultiLanguageString());
        }
        return variantBean;
    }
}