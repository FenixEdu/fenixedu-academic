package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DepartmentSite;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteBanner;
import net.sourceforge.fenixedu.domain.UnitSiteLayoutType;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteManagementDA;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class CustomUnitSiteManagementDA extends SiteManagementDA {

    private Integer getId(String id) {
        if (id == null || id.equals("")) {
            return null;
        }

        try {
            return new Integer(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    protected String getItemLocationForFile(HttpServletRequest request, Item item, Section section) {
        return null;
    }

    @Override
    protected UnitSite getSite(HttpServletRequest request) {
        Integer oid = getId(request.getParameter("oid"));

        if (oid == null) {
            return null;
        }
        
        return (DepartmentSite) RootDomainObject.getInstance().readSiteByOID(oid);
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("start");
    }
    
    public ActionForward introduction(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IViewState viewState = RenderUtils.getViewState();
        if (viewState != null && viewState.isValid() && !viewState.skipUpdate()) {
            request.setAttribute("successful", true);
        }
        
        return mapping.findForward("editIntroduction");
    }

    public ActionForward manageConfiguration(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("editConfiguration");
    }

    public ActionForward updateConfiguration(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IViewState viewState = RenderUtils.getViewState("visualization");
        if (viewState != null && viewState.isValid()) {
            request.setAttribute("visualizationChanged", true);
        }
        
        return mapping.findForward("editConfiguration");
    }
    
    public ActionForward changeLayout(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UnitSite site = getSite(request);
        String layoutParamenter = request.getParameter("layout");
        
        if (layoutParamenter == null) {
            return mapping.findForward("editLogo");
        }
        
        UnitSiteLayoutType layout = UnitSiteLayoutType.valueOf(layoutParamenter);
        executeService("ChangeUnitSiteLayout", site, layout);
        
        return mapping.findForward("editConfiguration");
    }

    public ActionForward chooseLogo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IViewState viewState = RenderUtils.getViewState("personalizedLogo");
        if (viewState != null && viewState.isValid()) {
            request.setAttribute("successful", true);
        }
        
        request.setAttribute("bean", new SimpleFileBean());
        return mapping.findForward("editLogo");
    }
    
    public ActionForward uploadLogo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UnitSite site = getSite(request);
        
        IViewState viewState = RenderUtils.getViewState("logoUpload");
        SimpleFileBean bean = (SimpleFileBean) ((MetaSlot) viewState.getMetaObject()).getMetaObject().getObject();
        
        if (bean == null || bean.getFile() == null) {
            chooseLogo(mapping, actionForm, request, response);
        }
        
        RenderUtils.invalidateViewState("logoUpload");
        executeService("UploadUnitSiteLogo", site, bean.getFile(), bean.getName());
        
        request.setAttribute("successful", true);
        return chooseLogo(mapping, actionForm, request, response);
    }

    public ActionForward manageBanners(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UnitSite site = getSite(request);
        request.setAttribute("banners", site.getBanners());
        
        request.setAttribute("bannerBean", new BannerBean());
        return mapping.findForward("editBanners");
    }

    public ActionForward editBanner(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UnitSiteBanner banner = getBanner(request);
        
        if (banner != null) {
            request.setAttribute("editBanner" + banner.getIdInternal(), true);
            request.setAttribute("editBannerBean", new BannerBean(banner));
        }
        
        return manageBanners(mapping, actionForm, request, response);
    }

    private UnitSiteBanner getBanner(HttpServletRequest request) {
        UnitSite site = getSite(request);
        Integer bannerId = getId(request.getParameter("bannerID"));
        
        for (UnitSiteBanner banner : site.getBanners()) {
            if (banner.getIdInternal().equals(bannerId)) {
                return banner;
            }
        }
        
        return null;
    }

    public ActionForward updateBanner(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UnitSite site = getSite(request);
        BannerBean bean = (BannerBean) getRenderedObject("editBanner");
        
        if (bean == null) {
            manageBanners(mapping, actionForm, request, response);
        }
        
        UnitSiteBanner banner = getBanner(request);
        SimpleFileBean main = bean.getMainImage();
        SimpleFileBean back = bean.getBackgroundImage();
        
        executeService("UpdateUnitSiteBanner", site, banner, main.getFile(), main.getName(), back.getFile(), back.getName(), bean.getColor());
        RenderUtils.invalidateViewState();
        
        return manageBanners(mapping, actionForm, request, response);
    }
    
    public ActionForward removeBanner(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UnitSite site = getSite(request);
        Integer bannerId = getId(request.getParameter("bannerID"));
        
        for (UnitSiteBanner banner : site.getBanners()) {
            if (banner.getIdInternal().equals(bannerId)) {
                executeService("DeleteUnitSiteBanner", site, banner);
                break;
            }
        }
        
        return manageBanners(mapping, actionForm, request, response);
    }

    public ActionForward addBanner(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UnitSite site = getSite(request);
        BannerBean bean = (BannerBean) getRenderedObject("newBanner");
        
        if (bean == null || bean.getMainImage().getFile() == null) {
            manageBanners(mapping, actionForm, request, response);
        }
        
        RenderUtils.invalidateViewState("newBanner");
        
        SimpleFileBean main = bean.getMainImage();
        SimpleFileBean background = bean.getBackgroundImage();
        executeService("CreateUnitSiteBanner", site, main.getFile(), main.getName(), background.getFile(), background.getName(), bean.getColor());
        
        return manageBanners(mapping, actionForm, request, response);
    }
    
}
