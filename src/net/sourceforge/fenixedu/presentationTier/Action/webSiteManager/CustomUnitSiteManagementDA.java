package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteBanner;
import net.sourceforge.fenixedu.domain.UnitSiteLayoutType;
import net.sourceforge.fenixedu.domain.UnitSiteLink;
import net.sourceforge.fenixedu.presentationTier.Action.manager.FunctionalitySectionCreator;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteManagementDA;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.util.FileUtils;

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

		return (UnitSite) RootDomainObject.getInstance().readSiteByOID(oid);
	}

	public ActionForward prepare(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward("start");
	}

	public ActionForward introduction(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		IViewState viewState = RenderUtils.getViewState();
		if (viewState != null && viewState.isValid() && !viewState.skipUpdate()) {
			request.setAttribute("successful", true);
		}

		return mapping.findForward("editIntroduction");
	}

	public ActionForward sideBanner(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		IViewState viewState = RenderUtils.getViewState();
		if (viewState != null && viewState.isValid() && !viewState.skipUpdate()) {
			request.setAttribute("successful", true);
		}

		return mapping.findForward("editSideBanner");
	}

	public ActionForward manageConfiguration(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward("editConfiguration");
	}

	public ActionForward updateInstitutionLogo(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		IViewState viewState = RenderUtils.getViewState("institutionLogo");
		if (viewState != null && viewState.isValid()) {
			request.setAttribute("institutionLogoChanged", true);
		}
		
		request.setAttribute("bean", new SimpleFileBean());
		return mapping.findForward("editLogo");
	}
	
	public ActionForward updateI18n(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		IViewState viewState = RenderUtils.getViewState("i18n");
		if (viewState != null && viewState.isValid()) {
			request.setAttribute("i18nChanged", true);
		}

		return mapping.findForward("editConfiguration");
	}

	public ActionForward updateConfiguration(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		IViewState viewState = RenderUtils.getViewState("visualization");
		if (viewState != null && viewState.isValid()) {
			request.setAttribute("visualizationChanged", true);
		}

		return mapping.findForward("editConfiguration");
	}

	public ActionForward changeLayout(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		UnitSite site = getSite(request);
		String layoutParamenter = request.getParameter("layout");

		if (layoutParamenter == null) {
			return prepare(mapping, actionForm, request, response);
		}

		UnitSiteLayoutType layout = UnitSiteLayoutType.valueOf(layoutParamenter);
		executeService("ChangeUnitSiteLayout", site, layout);

		return mapping.findForward("editConfiguration");
	}

	public ActionForward chooseLogo(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		IViewState viewState = RenderUtils.getViewState("personalizedLogo");
		if (viewState != null && viewState.isValid()) {
			request.setAttribute("successful", true);
		}

		request.setAttribute("bean", new SimpleFileBean());
		return mapping.findForward("editLogo");
	}

	public ActionForward uploadLogo(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		UnitSite site = getSite(request);

		IViewState viewState = RenderUtils.getViewState("logoUpload");
		SimpleFileBean bean = (SimpleFileBean) ((MetaSlot) viewState.getMetaObject()).getMetaObject()
				.getObject();

		if (bean == null || bean.getFile() == null) {
			return chooseLogo(mapping, actionForm, request, response);
		}

		RenderUtils.invalidateViewState("logoUpload");
		File file = FileUtils.copyToTemporaryFile(bean.getFile());
		try {
			executeService("UploadUnitSiteLogo", site, file, bean.getName());
		} finally {
			file.delete();
		}
		request.setAttribute("successful", true);
		return chooseLogo(mapping, actionForm, request, response);
	}

	public ActionForward manageBanners(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		UnitSite site = getSite(request);
		request.setAttribute("banners", site.getBanners());

		request.setAttribute("bannerBean", new BannerBean());
		return mapping.findForward("editBanners");
	}

	public ActionForward editBanner(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
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

	public ActionForward updateBanner(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		UnitSite site = getSite(request);
		BannerBean bean = (BannerBean) getRenderedObject("editBanner");

		if (bean == null) {
			manageBanners(mapping, actionForm, request, response);
		}

		UnitSiteBanner banner = getBanner(request);
		SimpleFileBean main = bean.getMainImage();
		SimpleFileBean back = bean.getBackgroundImage();
		File mainFile = main.getFile() == null ? null : FileUtils.copyToTemporaryFile(main.getFile());
		File backgroundFile = back.getFile() == null ? null : FileUtils.copyToTemporaryFile(back.getFile());

		try {
			executeService("UpdateUnitSiteBanner", site, banner, mainFile, main.getName(), backgroundFile, back.getName(), bean.getRepeat(), bean.getColor(), bean.getLink(), bean.getWeight());
		}
		finally {
			if (mainFile != null) {
				mainFile.delete();
			}
			
			if (backgroundFile != null) {
				backgroundFile.delete();
			}
        }
		
		RenderUtils.invalidateViewState();

		return manageBanners(mapping, actionForm, request, response);
	}

	public ActionForward removeBanner(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
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

	public ActionForward addBanner(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		UnitSite site = getSite(request);
		BannerBean bean = (BannerBean) getRenderedObject("newBanner");

		if (bean == null || bean.getMainImage().getFile() == null) {
			manageBanners(mapping, actionForm, request, response);
		}

		RenderUtils.invalidateViewState("newBanner");

		SimpleFileBean main = bean.getMainImage();
		SimpleFileBean background = bean.getBackgroundImage();
		File mainFile = FileUtils.copyToTemporaryFile(main.getFile());
		File backgroundFile = background.getFile() == null ? null : FileUtils.copyToTemporaryFile(background.getFile());
		try {
			executeService("CreateUnitSiteBanner", site, mainFile, main.getName(), backgroundFile,
					background.getName(), bean.getRepeat(), bean.getColor(), bean.getLink(), bean.getWeight());
		} finally {
			if (mainFile != null) {
				mainFile.delete();
			}
			
			if (backgroundFile != null) {
				backgroundFile.delete();
			}
		}
		return manageBanners(mapping, actionForm, request, response);
	}

	public ActionForward topNavigation(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		UnitSite site = getSite(request);
		request.setAttribute("customLinks", site.getSortedTopLinks());

		return mapping.findForward("editTopNavigation");
	}

	public ActionForward editTopLink(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setAttribute("editLink" + request.getParameter("linkID"), true);
		return topNavigation(mapping, actionForm, request, response);
	}

	public ActionForward removeTopLink(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		UnitSite site = getSite(request);
		Integer linkId = getId(request.getParameter("linkID"));

		for (UnitSiteLink link : site.getTopLinks()) {
			if (link.getIdInternal().equals(linkId)) {
				executeService("DeleteUnitSiteLink", site, link);
				break;
			}
		}

		return topNavigation(mapping, actionForm, request, response);
	}

	public ActionForward createTopLink(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		RenderUtils.invalidateViewState();
		return topNavigation(mapping, actionForm, request, response);
	}

	public ActionForward footerNavigation(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		UnitSite site = getSite(request);
		request.setAttribute("customLinks", site.getSortedFooterLinks());

		return mapping.findForward("editFooterNavigation");
	}

	public ActionForward editFooterLink(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setAttribute("editLink" + request.getParameter("linkID"), true);
		return footerNavigation(mapping, actionForm, request, response);
	}

	public ActionForward removeFooterLink(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		UnitSite site = getSite(request);
		Integer linkId = getId(request.getParameter("linkID"));

		for (UnitSiteLink link : site.getFooterLinks()) {
			if (link.getIdInternal().equals(linkId)) {
				executeService("DeleteUnitSiteLink", site, link);
				break;
			}
		}

		return footerNavigation(mapping, actionForm, request, response);
	}

	public ActionForward createFooterLink(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		RenderUtils.invalidateViewState();
		return footerNavigation(mapping, actionForm, request, response);
	}

	public ActionForward organizeTopLinks(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		UnitSite site = getSite(request);
		request.setAttribute("customLinks", site.getSortedTopLinks());

		return mapping.findForward("organizeTopLinks");
	}

	public ActionForward organizeFooterLinks(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		UnitSite site = getSite(request);
		request.setAttribute("customLinks", site.getSortedFooterLinks());

		return mapping.findForward("organizeFooterLinks");
	}

	public ActionForward saveTopLinksOrder(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		RenderUtils.invalidateViewState();
		saveLinksOrder(request, true);
		return topNavigation(mapping, actionForm, request, response);
	}

	public ActionForward saveFooterLinksOrder(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		RenderUtils.invalidateViewState();
		saveLinksOrder(request, false);
		return footerNavigation(mapping, actionForm, request, response);
	}

	protected void saveLinksOrder(HttpServletRequest request, boolean top) throws FenixFilterException,
			FenixServiceException {
		UnitSite site = getSite(request);
		String orderString = request.getParameter("linksOrder");

		List<UnitSiteLink> initialLinks = new ArrayList<UnitSiteLink>(top ? site.getSortedTopLinks()
				: site.getSortedFooterLinks());
		List<UnitSiteLink> orderedLinks = new ArrayList<UnitSiteLink>();

		String[] nodes = orderString.split(",");
		for (int i = 0; i < nodes.length; i++) {
			String[] parts = nodes[i].split("-");

			Integer itemIndex = getId(parts[0]);
			orderedLinks.add(initialLinks.get(itemIndex - 1));
		}

		ServiceUtils.executeService(getUserView(request), "RearrangeUnitSiteLinks", site, top,
				orderedLinks);
	}

	public ActionForward chooseIntroductionSections(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		IViewState viewState = RenderUtils.getViewState();
		if (viewState != null && viewState.isValid() && !viewState.skipUpdate()) {
			request.setAttribute("successful", true);
		}

		UnitSite site = getSite(request);
		Section sideSection = site.getSideSection();
		
		if (sideSection != null && sideSection.hasAnyAssociatedSections()) {
			request.setAttribute("canChoose", true);
		}
		
		return mapping.findForward("chooseIntroductionSections");
	}
	
	public ActionForward prepareAddInstitutionSection(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Section section = selectSection(request);
        
        if (section == null) {
            Site site = getSite(request);
            
            request.setAttribute("creator", new FunctionalitySectionCreator(site));
        } else {
            request.setAttribute("creator", new FunctionalitySectionCreator(section));
        }

		return mapping.findForward("addInstitutionSection");
	}
	
    public ActionForward functionalitySection(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        section(mapping, actionForm, request, response);
        
        return mapping.findForward("functionalitySection");
    }

    public ActionForward editFunctionalitySection(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        selectSection(request);
        
        return mapping.findForward("editFunctionalitySection");
    }

}
