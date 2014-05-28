/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.CreateUnitSiteBanner;
import net.sourceforge.fenixedu.applicationTier.Servico.DeleteUnitSiteBanner;
import net.sourceforge.fenixedu.applicationTier.Servico.DeleteUnitSiteLink;
import net.sourceforge.fenixedu.applicationTier.Servico.RearrangeUnitSiteLinks;
import net.sourceforge.fenixedu.applicationTier.Servico.UpdateUnitSiteBanner;
import net.sourceforge.fenixedu.applicationTier.Servico.UploadUnitSiteLogo;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.site.AddUnitSiteManager;
import net.sourceforge.fenixedu.applicationTier.Servico.site.ChangeUnitSiteLayout;
import net.sourceforge.fenixedu.applicationTier.Servico.site.CreateVirtualFunction;
import net.sourceforge.fenixedu.applicationTier.Servico.site.DeleteUnitSitePersonFunction;
import net.sourceforge.fenixedu.applicationTier.Servico.site.DeleteVirtualFunction;
import net.sourceforge.fenixedu.applicationTier.Servico.site.EditVirtualFunction;
import net.sourceforge.fenixedu.applicationTier.Servico.site.RearrangeUnitSiteFunctions;
import net.sourceforge.fenixedu.applicationTier.Servico.site.RemoveUnitSiteManager;
import net.sourceforge.fenixedu.dataTransferObject.VariantBean;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteBanner;
import net.sourceforge.fenixedu.domain.UnitSiteLayoutType;
import net.sourceforge.fenixedu.domain.UnitSiteLink;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Contract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteManagementDA;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.YearMonthDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.model.MetaSlot;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.FileUtils;

@Mapping(path = "/manageUnitSite", module = "webSiteManager", functionality = ListSitesAction.class)
@Forwards({ @Forward(name = "confirmDeleteFunction", path = "/webSiteManager/commons/deleteFunctionConfirm.jsp"),
        @Forward(name = "changePersonFunctions", path = "/webSiteManager/commons/changePersonFunctions.jsp"),
        @Forward(name = "createPersonFunction", path = "/webSiteManager/commons/createPersonFunction.jsp"),
        @Forward(name = "editSideBanner", path = "/webSiteManager/commons/edit-side-banner.jsp"),
        @Forward(name = "confirmSectionDelete", path = "/commons/sites/confirmSectionDelete.jsp"),
        @Forward(name = "editFooterNavigation", path = "/webSiteManager/commons/edit-footer-navigation.jsp"),
        @Forward(name = "editSection", path = "/commons/sites/editSection.jsp"),
        @Forward(name = "chooseManagers", path = "/webSiteManager/commons/chooseManagers.jsp"),
        @Forward(name = "uploadFile", path = "/commons/sites/uploadFile.jsp"),
        @Forward(name = "editConfiguration", path = "/webSiteManager/commons/edit-configuration.jsp"),
        @Forward(name = "organizeTopLinks", path = "/webSiteManager/commons/organize-top-links.jsp"),
        @Forward(name = "editFunctionalitySection", path = "/webSiteManager/commons/editInstitutionalSection.jsp"),
        @Forward(name = "organizeFooterLinks", path = "/webSiteManager/commons/organize-footer-links.jsp"),
        @Forward(name = "editItem", path = "/commons/sites/editItem.jsp"),
        @Forward(name = "editFile", path = "/commons/sites/editFile.jsp"),
        @Forward(name = "functionalitySection", path = "/webSiteManager/commons/institutionalSection.jsp"),
        @Forward(name = "addInstitutionSection", path = "/commons/sites/addInstitutionSection.jsp"),
        @Forward(name = "organizeFunctions", path = "/webSiteManager/commons/organizeFunctions.jsp"),
        @Forward(name = "editBanners", path = "/webSiteManager/commons/edit-banners.jsp"),
        @Forward(name = "organizeItems", path = "/commons/sites/organizeItems.jsp"),
        @Forward(name = "manageExistingFunctions", path = "/webSiteManager/commons/manageExistingFunctions.jsp"),
        @Forward(name = "organizeFiles", path = "/commons/sites/organizeFiles.jsp"),
        @Forward(name = "edit-fileItem-name", path = "/commons/sites/editFileItemDisplayName.jsp"),
        @Forward(name = "addFunction", path = "/webSiteManager/commons/addFunction.jsp"),
        @Forward(name = "editSectionPermissions", path = "/commons/sites/editSectionPermissions.jsp"),
        @Forward(name = "analytics", path = "/webSiteManager/commons/analytics.jsp"),
        @Forward(name = "editFunction", path = "/webSiteManager/commons/editFunction.jsp"),
        @Forward(name = "chooseIntroductionSections", path = "/webSiteManager/commons/chooseIntroductionSections.jsp"),
        @Forward(name = "section", path = "/commons/sites/section.jsp"),
        @Forward(name = "createSection", path = "/commons/sites/createSection.jsp"),
        @Forward(name = "editItemPermissions", path = "/commons/sites/editItemPermissions.jsp"),
        @Forward(name = "editIntroduction", path = "/webSiteManager/commons/edit-introduction.jsp"),
        @Forward(name = "editLogo", path = "/webSiteManager/commons/edit-logo.jsp"),
        @Forward(name = "editTopNavigation", path = "/webSiteManager/commons/edit-top-navigation.jsp"),
        @Forward(name = "sectionsManagement", path = "/webSiteManager/commons/sectionsManagement.jsp"),
        @Forward(name = "start", path = "/webSiteManager/commons/start.jsp"),
        @Forward(name = "createItem", path = "/commons/sites/createItem.jsp"),
        @Forward(name = "manageFunctions", path = "/webSiteManager/commons/manageFunctions.jsp"),
        @Forward(name = "editPersonFunction", path = "/webSiteManager/commons/editPersonFunction.jsp") })
public class CustomUnitSiteManagementDA extends SiteManagementDA {

    private static final Logger logger = LoggerFactory.getLogger(CustomUnitSiteManagementDA.class);

    private static final ActionForward FORWARD = new ActionForward("/websiteFrame.jsp");

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        setContext(request);
        ActionForward forward = super.execute(mapping, actionForm, request, response);
        if (forward.getPath().endsWith(".jsp")) {
            request.setAttribute("actual$page", forward.getPath());
            return FORWARD;
        } else {
            return forward;
        }
    }

    protected void setContext(HttpServletRequest request) {
        request.setAttribute("siteActionName", "/manageUnitSite.do");
        request.setAttribute("siteContextParam", "oid");
        request.setAttribute("siteContextParamValue", getSite(request).getExternalId());

        request.setAttribute("unitId", getSite(request).getUnit().getExternalId());
        request.setAttribute("announcementsActionName", "/manageUnitAnnouncements.do");
    }

    private Integer getId(String id) {
        if (id == null || id.equals("")) {
            return null;
        }

        try {
            return new Integer(id);
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    protected String getItemLocationForFile(HttpServletRequest request, Item item, Section section) {
        return null;
    }

    @Override
    protected UnitSite getSite(HttpServletRequest request) {
        return getDomainObject(request, "oid");
    }

    protected Unit getUnit(HttpServletRequest request) {
        UnitSite site = getSite(request);
        return site == null ? null : site.getUnit();
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("start");
    }

    public ActionForward introduction(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IViewState viewState = RenderUtils.getViewState();
        if (viewState != null && viewState.isValid() && !viewState.skipUpdate()) {
            request.setAttribute("successful", true);
        }

        return mapping.findForward("editIntroduction");
    }

    public ActionForward sideBanner(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IViewState viewState = RenderUtils.getViewState();
        if (viewState != null && viewState.isValid() && !viewState.skipUpdate()) {
            request.setAttribute("successful", true);
        }

        return mapping.findForward("editSideBanner");
    }

    public ActionForward manageConfiguration(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("editConfiguration");
    }

    public ActionForward updateInstitutionLogo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IViewState viewState = RenderUtils.getViewState("institutionLogo");
        if (viewState != null && viewState.isValid()) {
            request.setAttribute("institutionLogoChanged", true);
        }

        request.setAttribute("bean", new SimpleFileBean());
        return mapping.findForward("editLogo");
    }

    public ActionForward updateI18n(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IViewState viewState = RenderUtils.getViewState("i18n");
        if (viewState != null && viewState.isValid()) {
            request.setAttribute("i18nChanged", true);
        }

        return mapping.findForward("editConfiguration");
    }

    public ActionForward updateConfiguration(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IViewState viewState = RenderUtils.getViewState("visualization");
        if (viewState != null && viewState.isValid()) {
            request.setAttribute("visualizationChanged", true);
        }

        return mapping.findForward("editConfiguration");
    }

    public ActionForward changeLayout(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        UnitSite site = getSite(request);
        String layoutParamenter = request.getParameter("layout");

        if (layoutParamenter == null) {
            return prepare(mapping, actionForm, request, response);
        }

        UnitSiteLayoutType layout = UnitSiteLayoutType.valueOf(layoutParamenter);
        ChangeUnitSiteLayout.runChangeUnitSiteLayout(site, layout);

        return mapping.findForward("editConfiguration");
    }

    public ActionForward chooseLogo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IViewState viewState = RenderUtils.getViewState("personalizedLogo");
        if (viewState != null && viewState.isValid()) {
            request.setAttribute("successful", true);
        }

        request.setAttribute("bean", new SimpleFileBean());
        return mapping.findForward("editLogo");
    }

    public ActionForward uploadLogo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        UnitSite site = getSite(request);

        IViewState viewState = RenderUtils.getViewState("logoUpload");
        SimpleFileBean bean = (SimpleFileBean) ((MetaSlot) viewState.getMetaObject()).getMetaObject().getObject();

        if (bean == null || bean.getFile() == null) {
            return chooseLogo(mapping, actionForm, request, response);
        }

        RenderUtils.invalidateViewState("logoUpload");
        File file = FileUtils.copyToTemporaryFile(bean.getFile());
        try {
            UploadUnitSiteLogo.runUploadUnitSiteLogo(site, file, bean.getName());
        } finally {
            file.delete();
        }
        request.setAttribute("successful", true);
        return chooseLogo(mapping, actionForm, request, response);
    }

    public ActionForward manageBanners(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        UnitSite site = getSite(request);
        request.setAttribute("banners", site.getBanners());

        request.setAttribute("bannerBean", new BannerBean());
        return mapping.findForward("editBanners");
    }

    public ActionForward editBanner(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        UnitSiteBanner banner = getBanner(request);

        if (banner != null) {
            request.setAttribute("editBanner" + banner.getExternalId(), true);
            request.setAttribute("editBannerBean", new BannerBean(banner));
        }

        return manageBanners(mapping, actionForm, request, response);
    }

    private UnitSiteBanner getBanner(HttpServletRequest request) {
        UnitSite site = getSite(request);
        String bannerId = request.getParameter("bannerID");

        for (UnitSiteBanner banner : site.getBanners()) {
            if (banner.getExternalId().equals(bannerId)) {
                return banner;
            }
        }

        return null;
    }

    public ActionForward updateBanner(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        UnitSite site = getSite(request);
        BannerBean bean = getRenderedObject("editBanner");

        if (bean == null) {
            manageBanners(mapping, actionForm, request, response);
        }

        UnitSiteBanner banner = getBanner(request);
        SimpleFileBean main = bean.getMainImage();
        SimpleFileBean back = bean.getBackgroundImage();
        File mainFile = main.getFile() == null ? null : FileUtils.copyToTemporaryFile(main.getFile());
        File backgroundFile = back.getFile() == null ? null : FileUtils.copyToTemporaryFile(back.getFile());

        try {
            UpdateUnitSiteBanner.runUpdateUnitSiteBanner(site, banner, mainFile, main.getName(), backgroundFile, back.getName(),
                    bean.getRepeat(), bean.getColor(), bean.getLink(), bean.getWeight());
        } finally {
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

    public ActionForward removeBanner(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        UnitSite site = getSite(request);
        String bannerId = request.getParameter("bannerID");

        for (UnitSiteBanner banner : site.getBanners()) {
            if (banner.getExternalId().equals(bannerId)) {
                DeleteUnitSiteBanner.runDeleteUnitSiteBanner(site, banner);
                break;
            }
        }

        return manageBanners(mapping, actionForm, request, response);
    }

    public ActionForward addBanner(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        UnitSite site = getSite(request);
        BannerBean bean = getRenderedObject("newBanner");

        if (bean == null || bean.getMainImage().getFile() == null) {
            manageBanners(mapping, actionForm, request, response);
        }

        RenderUtils.invalidateViewState("newBanner");

        SimpleFileBean main = bean.getMainImage();
        SimpleFileBean background = bean.getBackgroundImage();
        File mainFile = FileUtils.copyToTemporaryFile(main.getFile());
        File backgroundFile = background.getFile() == null ? null : FileUtils.copyToTemporaryFile(background.getFile());
        try {
            CreateUnitSiteBanner.runCreateUnitSiteBanner(site, mainFile, main.getName(), backgroundFile, background.getName(),
                    bean.getRepeat(), bean.getColor(), bean.getLink(), bean.getWeight());
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

    public ActionForward topNavigation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        UnitSite site = getSite(request);
        request.setAttribute("customLinks", site.getSortedTopLinks());

        return mapping.findForward("editTopNavigation");
    }

    public ActionForward editTopLink(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("editLink" + request.getParameter("linkID"), true);
        return topNavigation(mapping, actionForm, request, response);
    }

    public ActionForward removeTopLink(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        UnitSite site = getSite(request);
        String linkId = request.getParameter("linkID");

        for (UnitSiteLink link : site.getTopLinks()) {
            if (link.getExternalId().equals(linkId)) {
                DeleteUnitSiteLink.runDeleteUnitSiteLink(site, link);
                break;
            }
        }

        return topNavigation(mapping, actionForm, request, response);
    }

    public ActionForward createTopLink(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        RenderUtils.invalidateViewState();
        return topNavigation(mapping, actionForm, request, response);
    }

    public ActionForward footerNavigation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        UnitSite site = getSite(request);
        request.setAttribute("customLinks", site.getSortedFooterLinks());

        return mapping.findForward("editFooterNavigation");
    }

    public ActionForward editFooterLink(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("editLink" + request.getParameter("linkID"), true);
        return footerNavigation(mapping, actionForm, request, response);
    }

    public ActionForward removeFooterLink(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        UnitSite site = getSite(request);
        String linkId = request.getParameter("linkID");

        for (UnitSiteLink link : site.getFooterLinks()) {
            if (link.getExternalId().equals(linkId)) {
                DeleteUnitSiteLink.runDeleteUnitSiteLink(site, link);
                break;
            }
        }

        return footerNavigation(mapping, actionForm, request, response);
    }

    public ActionForward createFooterLink(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        RenderUtils.invalidateViewState();
        return footerNavigation(mapping, actionForm, request, response);
    }

    public ActionForward organizeTopLinks(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        UnitSite site = getSite(request);
        request.setAttribute("customLinks", site.getSortedTopLinks());

        return mapping.findForward("organizeTopLinks");
    }

    public ActionForward organizeFooterLinks(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        UnitSite site = getSite(request);
        request.setAttribute("customLinks", site.getSortedFooterLinks());

        return mapping.findForward("organizeFooterLinks");
    }

    public ActionForward saveTopLinksOrder(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        RenderUtils.invalidateViewState();
        saveLinksOrder(request, true);
        return topNavigation(mapping, actionForm, request, response);
    }

    public ActionForward saveFooterLinksOrder(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        RenderUtils.invalidateViewState();
        saveLinksOrder(request, false);
        return footerNavigation(mapping, actionForm, request, response);
    }

    protected void saveLinksOrder(HttpServletRequest request, boolean top) throws FenixServiceException {
        UnitSite site = getSite(request);
        String orderString = request.getParameter("linksOrder");

        List<UnitSiteLink> initialLinks =
                new ArrayList<UnitSiteLink>(top ? site.getSortedTopLinks() : site.getSortedFooterLinks());
        List<UnitSiteLink> orderedLinks = new ArrayList<UnitSiteLink>();

        String[] nodes = orderString.split(",");
        for (String node : nodes) {
            String[] parts = node.split("-");

            Integer itemIndex = getId(parts[0]);
            orderedLinks.add(initialLinks.get(itemIndex - 1));
        }

        RearrangeUnitSiteLinks.runRearrangeUnitSiteLinks(site, top, orderedLinks);
    }

    public ActionForward chooseIntroductionSections(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IViewState viewState = RenderUtils.getViewState();
        if (viewState != null && viewState.isValid() && !viewState.skipUpdate()) {
            request.setAttribute("successful", true);
        }

        UnitSite site = getSite(request);
        Section sideSection = site.getSideSection();

        if (sideSection != null && !sideSection.getChildrenSections().isEmpty()) {
            request.setAttribute("canChoose", true);
        }

        return mapping.findForward("chooseIntroductionSections");
    }

    public ActionForward functionalitySection(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        section(mapping, actionForm, request, response);

        return mapping.findForward("functionalitySection");
    }

    public ActionForward editFunctionalitySection(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        selectSection(request);

        return mapping.findForward("editFunctionalitySection");
    }

    //
    // Functions
    //
    public ActionForward manageFunctions(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Unit unit = getUnit(request);

        SortedSet<Person> persons = collectFunctionPersons(unit, new TreeSet<Person>(Person.COMPARATOR_BY_NAME_AND_ID));
        request.setAttribute("people", wrapPersons(persons, unit));

        request.setAttribute("addUserBean", new VariantBean());

        return mapping.findForward("manageFunctions");
    }

    public ActionForward managePersonFunctions(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = getSelectedPerson(request);

        if (person != null) {
            request.setAttribute("personBean", new PersonFunctionsBean(person, getUnit(request)));
            return mapping.findForward("changePersonFunctions");
        } else {
            return manageFunctions(mapping, actionForm, request, response);
        }
    }

    private Person getSelectedPerson(HttpServletRequest request) {
        String id = request.getParameter("personID");
        if (id != null) {
            return (Person) FenixFramework.getDomainObject(id);
        }

        VariantBean bean = getRenderedObject("addUserBean");
        if (bean == null) {
            return null;
        }

        String username = bean.getString();
        User login = StringUtils.isEmpty(username) ? null : User.findByUsername(username);

        if (login == null) {
            addActionMessage("addPersonError", request, "site.functions.addPerson.noUsername");
            return null;
        }

        return login.getPerson();
    }

    public ActionForward manageExistingFunctions(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("tree", Collections.singleton(getUnit(request)));
        return mapping.findForward("manageExistingFunctions");
    }

    private List<PersonFunctionsBean> wrapPersons(SortedSet<Person> persons, Unit unit) {
        List<PersonFunctionsBean> result = new ArrayList<PersonFunctionsBean>();

        for (Person person : persons) {
            result.add(new PersonFunctionsBean(person, unit));
        }

        return result;
    }

    private SortedSet<Person> collectFunctionPersons(Unit unit, TreeSet<Person> persons) {
        YearMonthDay today = new YearMonthDay();

        AccountabilityTypeEnum[] types =
                new AccountabilityTypeEnum[] { AccountabilityTypeEnum.WORKING_CONTRACT, AccountabilityTypeEnum.RESEARCH_CONTRACT };

        List<Contract> contacts = unit.getContracts(today, null, types);
        for (Contract contract : contacts) {
            persons.add(contract.getPerson());
        }

        for (Function function : unit.getFunctions()) {
            for (PersonFunction pf : function.getPersonFunctions()) {
                if (pf.isActive(today)) {
                    persons.add(pf.getPerson());
                }
            }
        }

        for (Unit sub : unit.getSubUnits()) {
            collectFunctionPersons(sub, persons);
        }

        return persons;
    }

    public ActionForward addFunction(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Unit unit = getTargetUnit(request);

        request.setAttribute("target", unit);
        request.setAttribute("bean", new VariantBean());

        return mapping.findForward("addFunction");
    }

    public ActionForward createFunction(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        UnitSite site = getSite(request);
        Unit unit = getTargetUnit(request);

        VariantBean bean = getRenderedObject("create");
        if (bean != null) {
            CreateVirtualFunction.runCreateVirtualFunction(site, unit, bean.getMLString());
        }

        return manageExistingFunctions(mapping, actionForm, request, response);
    }

    private Unit getTargetUnit(HttpServletRequest request) {
        return getDomainObject(request, "unitID");
    }

    public ActionForward prepareEditFunction(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Function function = getTargetFunction(request);

        VariantBean bean = new VariantBean();
        bean.setMLString(function.getTypeName());

        request.setAttribute("bean", bean);
        request.setAttribute("function", function);

        return mapping.findForward("editFunction");
    }

    public ActionForward editFunction(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Function function = getTargetFunction(request);

        VariantBean bean = getRenderedObject("edit");
        if (bean != null) {
            EditVirtualFunction.runEditVirtualFunction(getSite(request), function, bean.getMLString());
        }

        return manageExistingFunctions(mapping, actionForm, request, response);
    }

    public ActionForward deleteFunction(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Function function = getTargetFunction(request);
        request.setAttribute("function", function);

        return mapping.findForward("confirmDeleteFunction");
    }

    public ActionForward confirmDeleteFunction(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        if (request.getParameter("cancel") == null) {
            DeleteVirtualFunction.runDeleteVirtualFunction(getSite(request), getTargetFunction(request));
        }

        return manageExistingFunctions(mapping, actionForm, request, response);
    }

    private Function getTargetFunction(HttpServletRequest request) {
        return getDomainObject(request, "functionID");
    }

    public ActionForward organizeFunctions(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Unit unit = getTargetUnit(request);

        request.setAttribute("target", unit);
        request.setAttribute("functions", unit.getOrderedActiveFunctions());

        return mapping.findForward("organizeFunctions");
    }

    public ActionForward changeFunctionsOrder(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String orderString = request.getParameter("functionsOrder");
        Unit unit = getTargetUnit(request);

        if (unit == null) {
            return manageExistingFunctions(mapping, actionForm, request, response);
        }

        List<Function> initialFunctions = new ArrayList<Function>(unit.getOrderedActiveFunctions());
        List<Function> orderedFunctions = new ArrayList<Function>();

        String[] nodes = orderString.split(",");
        for (String node : nodes) {
            String[] parts = node.split("-");

            Integer functionIndex = getId(parts[0]);
            orderedFunctions.add(initialFunctions.get(functionIndex - 1));
        }

        RearrangeUnitSiteFunctions.runRearrangeUnitSiteFunctions(getSite(request), unit, orderedFunctions);
        return manageExistingFunctions(mapping, actionForm, request, response);
    }

    public ActionForward addPersonFunction(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = getSelectedPerson(request);
        Function function = getTargetFunction(request);

        request.setAttribute("person", person);
        request.setAttribute("function", function);

        return mapping.findForward("createPersonFunction");
    }

    public ActionForward editPersonFunction(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        PersonFunction function = getPersonFunction(request);
        request.setAttribute("personFunction", function);

        return mapping.findForward("editPersonFunction");
    }

    private PersonFunction getPersonFunction(HttpServletRequest request) {
        return getDomainObject(request, "personFunctionID");
    }

    public ActionForward removePersonFunction(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        PersonFunction personFunction = getPersonFunction(request);
        DeleteUnitSitePersonFunction.runDeleteUnitSitePersonFunction(getSite(request), personFunction);

        return managePersonFunctions(mapping, actionForm, request, response);
    }

    //
    // Unit site managers
    //

    public ActionForward chooseManagers(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        UnitSite site = getSite(request);

        request.setAttribute("managersBean", new UnitSiteManagerBean());
        request.setAttribute("managers", site.getManagers());

        return mapping.findForward("chooseManagers");
    }

    public ActionForward removeManager(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        UnitSite site = getSite(request);

        String managerId = request.getParameter("managerID");
        for (Person manager : site.getManagers()) {
            if (manager.getExternalId().equals(managerId)) {
                try {
                    removeUnitSiteManager(site, manager);
                } catch (DomainException e) {
                    addActionMessage("error", request, e.getKey(), e.getArgs());
                }
            }
        }

        return chooseManagers(mapping, actionForm, request, response);
    }

    public ActionForward addManager(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        UnitSiteManagerBean bean = getRenderedObject("add");

        if (bean == null) {
            return chooseManagers(mapping, actionForm, request, response);
        }

        Person person = null;

        String alias = bean.getAlias();
        if (alias != null) {
            person = Person.readPersonByUsername(alias);

            if (person == null) {
                addActionMessage("addError", request, "error.site.managers.add.alias.notFound");
            }
        } else {
            String documentId = bean.getIdNumber();

            if (documentId != null) {
                Set<Person> persons = new TreeSet<Person>(Person.readByDocumentIdNumber(documentId));

                if (persons.isEmpty()) {
                    person = null;
                } else {
                    // TODO: show a selection list
                    int i;
                    Iterator<Person> iter = persons.iterator();
                    for (i = new Random().nextInt() % persons.size(); iter.hasNext() && i > 0; i--) {
                        iter.next();
                    }
                    person = iter.next();
                }

                if (person == null) {
                    addActionMessage("addError", request, "error.site.managers.add.idNumber.notFound");
                }
            }
        }

        if (person != null) {
            try {
                UnitSite site = getSite(request);

                addUnitSiteManager(site, person);
                RenderUtils.invalidateViewState("add");
            } catch (DomainException e) {
                addActionMessage("addError", request, e.getKey(), e.getArgs());
            }
        }

        return chooseManagers(mapping, actionForm, request, response);
    }

    protected void removeUnitSiteManager(UnitSite site, Person person) throws FenixServiceException {
        RemoveUnitSiteManager.runRemoveUnitSiteManager(site, person);
    }

    protected void addUnitSiteManager(UnitSite site, Person person) throws FenixServiceException {
        AddUnitSiteManager.runAddUnitSiteManager(site, person);
    }

    @Override
    protected String getAuthorNameForFile(HttpServletRequest request) {
        Unit unit = getUnit(request);

        if (unit != null) {
            return unit.getName();
        } else {
            return getLoggedPerson(request).getName();
        }
    }

    public ActionForward analytics(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IViewState viewState = RenderUtils.getViewState("siteAnalytics");

        if (viewState != null && viewState.isValid()) {
            UnitSite site = getSite(request);

            if (site.getGoogleAnalyticsCode() != null) {
                request.setAttribute("codeAccepted", true);
            } else {
                request.setAttribute("codeRemoved", true);
            }
        }

        return mapping.findForward("analytics");
    }

}
