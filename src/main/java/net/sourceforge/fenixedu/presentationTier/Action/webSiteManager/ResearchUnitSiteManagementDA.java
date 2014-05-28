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

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "webSiteManager", path = "/manageResearchUnitSite", functionality = ListSitesAction.class)
@Forwards({ @Forward(name = "editContract", path = "/webSiteManager/researchUnitSite/editContract.jsp"),
        @Forward(name = "externalPersonExtraInfo", path = "/webSiteManager/researchUnitSite/addInfoForNewExternalPerson.jsp"),
        @Forward(name = "managePeople", path = "/webSiteManager/researchUnitSite/managePeople.jsp"),
        @Forward(name = "editResearchSite", path = "/webSiteManager/commons/start.jsp") })
public class ResearchUnitSiteManagementDA extends CustomUnitSiteManagementDA {

    @Override
    protected void setContext(HttpServletRequest request) {
        request.setAttribute("siteActionName", "/manageResearchUnitSite.do");
        request.setAttribute("siteContextParam", "oid");
        request.setAttribute("siteContextParamValue", getSite(request).getExternalId());
        request.setAttribute("siteId", getSite(request).getExternalId());
        request.setAttribute("announcementsActionName", "/manageResearchUnitAnnouncements.do");
        request.setAttribute("researchUnit", true);
    }

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
        if (viewState != null && getSite(request).getManagersSet().contains(getLoggedPerson(request))) {
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
        if (viewState != null && getSite(request).getManagersSet().contains(getLoggedPerson(request))) {
            ResearchContractBean bean = (ResearchContractBean) viewState.getMetaObject().getObject();
            try {
                CreateResearchContract.run(bean);
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
        if (viewState != null && getSite(request).getManagersSet().contains(getLoggedPerson(request))) {
            ResearchContractBean bean = (ResearchContractBean) viewState.getMetaObject().getObject();
            if (bean.getPerson() == null) {
                return managePeoplePostBack(mapping, actionForm, request, response);
            }
            try {
                CreateResearchContract.run(bean);
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
            ResearchContract contract = FenixFramework.getDomainObject(contractID);
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