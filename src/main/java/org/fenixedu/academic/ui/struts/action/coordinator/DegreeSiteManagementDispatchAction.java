/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.coordinator;

import static org.fenixedu.bennu.core.security.Authenticate.getUser;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.DegreeInfo;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.utils.RequestUtils;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import com.google.gson.JsonParser;

/**
 * @author Tânia Pousão Created on 31/Out/2003
 */
@Mapping(module = "coordinator", path = "/degreeSiteManagement", input = "/degreeSiteManagement.do?method=subMenu&page=0",
        formBean = "degreeInfoForm", functionality = DegreeCoordinatorIndex.class)
@Forwards({ @Forward(name = "editOK", path = "/coordinator/degreeSite/editOK.jsp"),
        @Forward(name = "viewHistoric", path = "/coordinator/degreeSite/viewHistoric.jsp"),
        @Forward(name = "viewInformation", path = "/coordinator/degreeSite/viewDegreeInfo.jsp"),
        @Forward(name = "viewDescriptionCurricularPlan", path = "/coordinator/degreeSite/viewDescriptionCurricularPlan.jsp") })
public class DegreeSiteManagementDispatchAction extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        if (degreeCurricularPlan != null) {
            request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);
        }
        request.setAttribute("siteUrl", degreeCurricularPlan.getDegree().getSiteUrl());
        request.setAttribute("siteActionName", "/degreeSiteManagement.do");
        request.setAttribute("siteContextParam", "degreeCurricularPlanID");
        request.setAttribute("siteContextParamValue", degreeCurricularPlan.getExternalId());
        ActionForward forward = super.execute(mapping, actionForm, request, response);
        request.setAttribute("coordinator$actual$page", forward.getPath());
        return new ActionForward("/degreeSite/siteFrame.jsp");
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
            if (!getUser().getPerson().isCoordinatorFor(degreeCurricularPlan, currentExecutionYear)
                    && !getUser().getPerson().isCoordinatorFor(degreeCurricularPlan, currentExecutionYear.getNextExecutionYear())) {
                addErrorMessage(request, "notAuthorized", "error.exception.notAuthorized2");
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
            addErrorMessage(request, "noDegreeCurricularPlan", "error.coordinator.chosenDegree");
            return new ActionForward(mapping.getInput());
        }
        request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);

        LocalizedString.Builder builder = new LocalizedString.Builder();
        CoreConfiguration.supportedLocales().forEach(l -> {
            if (l.getLanguage().equals("en")) {
                builder.with(l, degreeCurricularPlan.getDescriptionEn());
            } else {
                builder.with(l, degreeCurricularPlan.getDescription());
            }
        });

        LocalizedString degreeCurricularPlanLS = builder.build();

        request.setAttribute("degreeCurricularPlanDescription", degreeCurricularPlanLS);

        return mapping.findForward("viewDescriptionCurricularPlan");
    }

    public ActionForward editDescriptionDegreeCurricularPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);

        String degreeCurricularPlanDescription = request.getParameter("degreeCurricularPlanDescription");
        LocalizedString localizedDescription = LocalizedString.fromJson(new JsonParser().parse(degreeCurricularPlanDescription));
        String degreeCurricularPlanDescriptionDef = "";
        String degreeCurricularPlanDescriptionEn = "";

        for (Locale l : CoreConfiguration.supportedLocales()) {
            if (l.getLanguage().equals("en")) {
                degreeCurricularPlanDescriptionEn = localizedDescription.getContent(l);
            } else {
                degreeCurricularPlanDescriptionDef = localizedDescription.getContent(l);
            }
        }

        updateDegreeCurricularPlanDescription(degreeCurricularPlanDescriptionDef, degreeCurricularPlan,
                degreeCurricularPlanDescriptionEn);

        RequestUtils.getAndSetStringToRequest(request, "degreeCurricularPlanID");
        return mapping.findForward("editOK");
    }

    @Atomic(mode = TxMode.WRITE)
    private void updateDegreeCurricularPlanDescription(String degreeCurricularPlanDescriptionPt,
            DegreeCurricularPlan degreeCurricularPlan, String degreeCurricularPlanDescriptionEn) {
        degreeCurricularPlan.setDescription(degreeCurricularPlanDescriptionPt);
        degreeCurricularPlan.setDescriptionEn(degreeCurricularPlanDescriptionEn);
    }

    public ActionForward viewHistoric(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        // read execution degree
        String degreeCurricularPlanID = RequestUtils.getAndSetStringToRequest(request, "degreeCurricularPlanID");
        DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanID);

        if (!degreeCurricularPlan.getExecutionDegreesSet().isEmpty()) {
            final User userView = getUser();
            request.setAttribute("executionDegrees", userView.getPerson().getCoordinatedExecutionDegrees(degreeCurricularPlan));
        }

        return mapping.findForward("viewHistoric");
    }

    public static class CreateCurrentDegreeInfo {
        @Atomic
        public static DegreeInfo run(Degree degree) {
            return degree.createCurrentDegreeInfo();
        }

    }

}