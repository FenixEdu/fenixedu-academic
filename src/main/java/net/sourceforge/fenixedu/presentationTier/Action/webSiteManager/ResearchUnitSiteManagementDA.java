package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement.CreateResearchContract;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement.DeleteResearchContract;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ResearchUnitSite;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchContract;
import net.sourceforge.fenixedu.presentationTier.Action.publico.LoginRequestManagement;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(module = "webSiteManager", path = "/manageResearchUnitSite", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "confirmDeleteFunction", path = "researchSite-confirmDeleteFunction"),
        @Forward(name = "editContract", path = "researchSite-edit-contract"),
        @Forward(name = "changePersonFunctions", path = "researchSite-changePersonFunctions"),
        @Forward(name = "createPersonFunction", path = "researchSite-createPersonFunction"),
        @Forward(name = "editSideBanner", path = "researchSite-edit-side-banner"),
        @Forward(name = "externalPersonExtraInfo", path = "researchSite-external-person-extra-info"),
        @Forward(name = "confirmSectionDelete", path = "manage-researchSite-confirmSectionDelete"),
        @Forward(name = "editSection", path = "manage-researchSite-editSection"),
        @Forward(name = "editFooterNavigation", path = "researchSite-edit-footer-navigation"),
        @Forward(name = "chooseManagers", path = "researchSite-chooseManagers"),
        @Forward(name = "uploadFile", path = "manage-researchSite-uploadFile"),
        @Forward(name = "editConfiguration", path = "researchSite-edit-configuration"),
        @Forward(name = "managePeople", path = "researchSite-manage-people"),
        @Forward(name = "organizeTopLinks", path = "researchSite-edit-organizeTopLinks"),
        @Forward(name = "organizeFooterLinks", path = "researchSite-edit-organizeFooterLinks"),
        @Forward(name = "editFile", path = "manage-researchSite-editFile"),
        @Forward(name = "editItem", path = "manage-researchSite-editItem"),
        @Forward(name = "addInstitutionSection", path = "researchSite-addInstitutionSection"),
        @Forward(name = "organizeFunctions", path = "researchSite-organizeFunctions"),
        @Forward(name = "organizeItems", path = "manage-researchSite-organizeItems"),
        @Forward(name = "editResearchSite", path = "edit-research-site"),
        @Forward(name = "editBanners", path = "researchSite-edit-banners"),
        @Forward(name = "manageExistingFunctions", path = "researchSite-manageExistingFunctions"),
        @Forward(name = "organizeFiles", path = "manage-researchSite-organizeFiles"),
        @Forward(name = "edit-fileItem-name", path = "manage-researchSite-editFileItemName"),
        @Forward(name = "addFunction", path = "researchSite-addFunction"),
        @Forward(name = "editSectionPermissions", path = "manage-researchSite-editSectionPermissions"),
        @Forward(name = "analytics", path = "researchSite-analytics"),
        @Forward(name = "editFunction", path = "researchSite-editFunction"),
        @Forward(name = "chooseIntroductionSections", path = "researchSite-chooseIntroductionSections"),
        @Forward(name = "section", path = "manage-researchSite-section"),
        @Forward(name = "createSection", path = "manage-researchSite-createSection"),
        @Forward(name = "editItemPermissions", path = "manage-researchSite-editItemPermissions"),
        @Forward(name = "editIntroduction", path = "researchSite-edit-introduction"),
        @Forward(name = "editLogo", path = "researchSite-edit-logo"),
        @Forward(name = "editTopNavigation", path = "researchSite-edit-top-navigation"),
        @Forward(name = "createItem", path = "manage-researchSite-createItem"),
        @Forward(name = "sectionsManagement", path = "manage-researchSite-sectionsManagement"),
        @Forward(name = "manageFunctions", path = "researchSite-manageFunctions"),
        @Forward(name = "editPersonFunction", path = "researchSite-editPersonFunction") })
public class ResearchUnitSiteManagementDA extends CustomUnitSiteManagementDA {

    @Override
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("editResearchSite");
    }

    public ActionForward managePeople(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        ResearchContractBean bean = new ResearchContractBean();
        bean.setUnit(getSite(request).getUnit());
        request.setAttribute("bean", bean);
        return mapping.findForward("managePeople");
    }

    public ActionForward managePeoplePostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        ResearchContractBean bean =
                (ResearchContractBean) RenderUtils.getViewState("createPersonContract").getMetaObject().getObject();
        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState();
        return mapping.findForward("managePeople");
    }

    public ActionForward addPersonWrapper(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        ResearchContractBean bean =
                (ResearchContractBean) RenderUtils.getViewState("createPersonContract").getMetaObject().getObject();
        return (request.getParameter("createPerson") != null || (bean.getPerson() != null && StringUtils.isEmpty(bean.getPerson()
                .getEmail()))) ? prepareAddNewPerson(mapping, actionForm, request, response) : addPerson(mapping, actionForm,
                request, response);
    }

    public ActionForward prepareAddNewPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        IViewState viewState = RenderUtils.getViewState();
        if (viewState != null && getSite(request).hasManagers(getLoggedPerson(request))) {
            ResearchContractBean bean = (ResearchContractBean) viewState.getMetaObject().getObject();
            Person person = bean.getPerson();
            if (person != null) {
                bean.setDocumentType(person.getIdDocumentType());
                bean.setDocumentIDNumber(person.getDocumentIdNumber());
            }
            request.setAttribute("bean", bean);
            return mapping.findForward("externalPersonExtraInfo");
        }
        return managePeople(mapping, actionForm, request, response);
    }

    public ActionForward addNewPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        IViewState viewState = RenderUtils.getViewState("extraInfo");
        if (viewState != null && getSite(request).hasManagers(getLoggedPerson(request))) {
            ResearchContractBean bean = (ResearchContractBean) viewState.getMetaObject().getObject();
            try {
                CreateResearchContract.run(bean, getLoggedPerson(request), LoginRequestManagement.getRequestURL(request));
            } catch (FenixServiceException e) {
                addActionMessage(request, e.getMessage());
                return managePeople(mapping, actionForm, request, response);
            } catch (DomainException e) {
                addActionMessage(request, e.getMessage());
                return managePeople(mapping, actionForm, request, response);
            }
        }
        return managePeople(mapping, actionForm, request, response);
    }

    public ActionForward addPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        IViewState viewState = RenderUtils.getViewState("createPersonContract");
        if (viewState != null && getSite(request).hasManagers(getLoggedPerson(request))) {
            ResearchContractBean bean = (ResearchContractBean) viewState.getMetaObject().getObject();
            if (bean.getPerson() == null) {
                return managePeoplePostBack(mapping, actionForm, request, response);
            }
            try {
                CreateResearchContract.run(bean, getLoggedPerson(request), LoginRequestManagement.getRequestURL(request));
            } catch (DomainException e) {
                addActionMessage(request, e.getMessage());
                return managePeople(mapping, actionForm, request, response);
            }
            RenderUtils.invalidateViewState();
        }
        return managePeople(mapping, actionForm, request, response);
    }

    public ActionForward removePerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        ResearchContract contract = getDomainObject(request, "cid");
        try {
            DeleteResearchContract.run(contract);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
        }

        return managePeople(mapping, actionForm, request, response);
    }

    public ActionForward editContract(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String contractID = request.getParameter("cid");

        if (contractID != null) {
            ResearchContract contract = AbstractDomainObject.fromExternalId(contractID);
            request.setAttribute("contract", contract);
            return mapping.findForward("editContract");
        }

        return managePeople(mapping, actionForm, request, response);
    }

    @Override
    protected ResearchUnitSite getSite(HttpServletRequest request) {
        return (ResearchUnitSite) super.getSite(request);
    }

    @Override
    protected String getAuthorNameForFile(HttpServletRequest request) {
        return getSite(request).getUnit().getName();
    }

    @Override
    protected String getItemLocationForFile(HttpServletRequest request, Item item, Section section) {
        return null;
    }

}