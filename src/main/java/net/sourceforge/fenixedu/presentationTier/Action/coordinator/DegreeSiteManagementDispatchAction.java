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
package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.CreateCurrentDegreeInfo;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteManagementDA;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Tânia Pousão Created on 31/Out/2003
 */
@Mapping(module = "coordinator", path = "/degreeSiteManagement", input = "/degreeSiteManagement.do?method=subMenu&page=0",
        formBean = "degreeInfoForm", functionality = DegreeCoordinatorIndex.class)
@Forwards({ @Forward(name = "editOK", path = "/coordinator/degreeSite/editOK.jsp"),
        @Forward(name = "organizeItems", path = "/commons/sites/organizeItems.jsp"),
        @Forward(name = "degreeSiteMenu", path = "/coordinator/degreeSite/help.jsp"),
        @Forward(name = "viewHistoric", path = "/coordinator/degreeSite/viewHistoric.jsp"),
        @Forward(name = "organizeFiles", path = "/commons/sites/organizeFiles.jsp"),
        @Forward(name = "edit-fileItem-name", path = "/commons/sites/editFileItemDisplayName.jsp"),
        @Forward(name = "editSectionPermissions", path = "/commons/sites/editSectionPermissions.jsp"),
        @Forward(name = "confirmSectionDelete", path = "/commons/sites/confirmSectionDelete.jsp"),
        @Forward(name = "editItemPermissions", path = "/commons/sites/editItemPermissions.jsp"),
        @Forward(name = "createSection", path = "/commons/sites/createSection.jsp"),
        @Forward(name = "section", path = "/commons/sites/section.jsp"),
        @Forward(name = "editSection", path = "/commons/sites/editSection.jsp"),
        @Forward(name = "uploadFile", path = "/commons/sites/uploadFile.jsp"),
        @Forward(name = "viewInformation", path = "/coordinator/degreeSite/viewDegreeInfo.jsp"),
        @Forward(name = "sectionsManagement", path = "/commons/sites/sectionsManagement.jsp"),
        @Forward(name = "createItem", path = "/commons/sites/createItem.jsp"),
        @Forward(name = "viewDescriptionCurricularPlan", path = "/coordinator/degreeSite/viewDescriptionCurricularPlan.jsp"),
        @Forward(name = "editFile", path = "/commons/sites/editFile.jsp"),
        @Forward(name = "addInstitutionSection", path = "/commons/sites/addInstitutionSection.jsp"),
        @Forward(name = "editItem", path = "/commons/sites/editItem.jsp") })
public class DegreeSiteManagementDispatchAction extends SiteManagementDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        if (degreeCurricularPlan != null) {
            request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);
        }
        request.setAttribute("siteActionName", "/degreeSiteManagement.do");
        request.setAttribute("siteContextParam", "degreeCurricularPlanID");
        request.setAttribute("siteContextParamValue", degreeCurricularPlan.getExternalId());
        ActionForward forward = super.execute(mapping, actionForm, request, response);
        request.setAttribute("coordinator$actual$page", forward.getPath());
        return new ActionForward("/degreeSite/siteFrame.jsp");
    }

    @Override
    protected Site getSite(HttpServletRequest request) {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        if (degreeCurricularPlan != null) {
            return degreeCurricularPlan.getDegree().getSite();
        } else {
            return null;
        }
    }

    public DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
        String parameter = request.getParameter("degreeCurricularPlanID");

        if (parameter == null) {
            return null;
        }

        try {
            return FenixFramework.getDomainObject(parameter);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Unit getUnit(HttpServletRequest request) {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        if (degreeCurricularPlan == null) {
            return null;
        } else {
            return degreeCurricularPlan.getDegree().getUnit();
        }
    }

    public ActionForward subMenu(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String degreeCurricularPlanId = request.getParameter("degreeCurricularPlanID");
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);
        return mapping.findForward("degreeSiteMenu");
    }

    public ActionForward viewInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        RequestUtils.getAndSetStringToRequest(request, "info");

        String degreeCurricularPlanID = RequestUtils.getAndSetStringToRequest(request, "degreeCurricularPlanID");
        DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanID);

        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        DegreeInfo currentDegreeInfo = currentExecutionYear.getDegreeInfo(degreeCurricularPlan.getDegree());

        if (currentDegreeInfo == null && currentExecutionYear.hasNextExecutionYear()) {
            currentDegreeInfo = currentExecutionYear.getNextExecutionYear().getDegreeInfo(degreeCurricularPlan.getDegree());
        }

        if (currentDegreeInfo == null) {
            final User userView = Authenticate.getUser();

            if (!userView.getPerson().isCoordinatorFor(degreeCurricularPlan, currentExecutionYear)
                    && !userView.getPerson().isCoordinatorFor(degreeCurricularPlan, currentExecutionYear.getNextExecutionYear())) {
                final ActionErrors errors = new ActionErrors();
                errors.add("notAuthorized", new ActionError("error.exception.notAuthorized2"));
                saveErrors(request, errors);
                return new ActionForward(mapping.getInput());
            }

            currentDegreeInfo = CreateCurrentDegreeInfo.run(degreeCurricularPlan.getDegree());
        }

        request.setAttribute("currentDegreeInfo", currentDegreeInfo);

        return mapping.findForward("viewInformation");
    }

    public ActionForward editDegreeInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        RequestUtils.getAndSetStringToRequest(request, "info");
        RequestUtils.getAndSetStringToRequest(request, "degreeCurricularPlanID");

        return mapping.findForward("editOK");
    }

    public ActionForward viewDescriptionCurricularPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        String degreeCurricularPlanID = RequestUtils.getAndSetStringToRequest(request, "degreeCurricularPlanID");
        DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanID);
        if (degreeCurricularPlan == null) {
            final ActionErrors errors = new ActionErrors();
            errors.add("noDegreeCurricularPlan", new ActionError("error.coordinator.chosenDegree"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);

        return mapping.findForward("viewDescriptionCurricularPlan");
    }

    public ActionForward editDescriptionDegreeCurricularPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        RequestUtils.getAndSetStringToRequest(request, "degreeCurricularPlanID");
        return mapping.findForward("editOK");
    }

    public ActionForward viewHistoric(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        // read execution degree
        String degreeCurricularPlanID = RequestUtils.getAndSetStringToRequest(request, "degreeCurricularPlanID");
        DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanID);

        if (degreeCurricularPlan.hasAnyExecutionDegrees()) {
            final User userView = Authenticate.getUser();
            request.setAttribute("executionDegrees", userView.getPerson().getCoordinatedExecutionDegrees(degreeCurricularPlan));
        }

        return mapping.findForward("viewHistoric");
    }

    @Override
    protected String getAuthorNameForFile(HttpServletRequest request) {
        Unit unit = getUnit(request);
        if (unit == null) {
            return null;
        } else {
            return unit.getName();
        }
    }

    @Override
    protected String getItemLocationForFile(HttpServletRequest request, Item item, Section section) {
        return getSite(request).getReversePath();
    }

}