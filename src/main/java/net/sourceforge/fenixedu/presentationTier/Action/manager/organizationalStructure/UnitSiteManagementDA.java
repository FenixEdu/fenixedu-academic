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
package net.sourceforge.fenixedu.presentationTier.Action.manager.organizationalStructure;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement.CreateUnitSite;
import net.sourceforge.fenixedu.dataTransferObject.VariantBean;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications.ManagerOrganizationalStructureApp;
import net.sourceforge.fenixedu.presentationTier.Action.webSiteManager.CustomUnitSiteManagementDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

@StrutsFunctionality(app = ManagerOrganizationalStructureApp.class, path = "unit-site", titleKey = "title.unitSite.manage.sites")
@Mapping(module = "manager", path = "/unitSiteManagement")
@Forwards({
        @Forward(name = "chooseManagers", path = "/manager/organizationalStructureManagament/unitSites/editSiteManagers.jsp"),
        @Forward(name = "createEntryPoint", path = "/manager/organizationalStructureManagament/unitSites/createEntryPoint.jsp"),
        @Forward(name = "showUnits", path = "/manager/organizationalStructureManagament/unitSites/showUnits.jsp") })
public class UnitSiteManagementDA extends CustomUnitSiteManagementDA {

    @Override
    protected Unit getUnit(HttpServletRequest request) {
        Unit unit = super.getUnit(request);

        if (unit != null) {
            return unit;
        }

        return getDomainObject(request, "unitID");
    }

    @Override
    protected void setContext(HttpServletRequest request) {
    }

    @Override
    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("units", Collections.singleton(Bennu.getInstance().getInstitutionUnit()));
        return mapping.findForward("showUnits");
    }

    public ActionForward createSite(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
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
//        Portal.getRootPortal().addContentJump(site, multilanguagebean.getMLString());
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