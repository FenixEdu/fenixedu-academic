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

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.EquivalencePlan;
import org.fenixedu.academic.domain.EquivalencePlanEntry;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.service.services.coordinator.DeleteEquivalencePlanEntry;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "coordinator", path = "/degreeCurricularPlan/equivalencyPlan", functionality = DegreeCoordinatorIndex.class)
@Forwards(@Forward(name = "showPlan", path = "/coordinator/degreeCurricularPlan/showEquivalencyPlan.jsp"))
public class EquivalencyPlanDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);

        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        if (degreeCurricularPlan == null) {
            request.setAttribute("degreeCurricularPlans", DegreeCurricularPlan.getDegreeCurricularPlans(DegreeType.oneOf(
                    DegreeType::isBolonhaDegree, DegreeType::isBolonhaMasterDegree, DegreeType::isIntegratedMasterDegree)));
        } else {
            request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);
            final DegreeCurricularPlanBean degreeCurricularPlanBean = new DegreeCurricularPlanBean(degreeCurricularPlan);
            request.setAttribute("degreeCurricularPlanBean", degreeCurricularPlanBean);
        }
        return super.execute(mapping, actionForm, request, response);
    }

    @EntryPoint
    public ActionForward showPlan(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("showPlan");
    }

    public ActionForward showTable(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("viewTable", Boolean.TRUE);
        final DegreeModule degreeModule = getDegreeModule(request);
        if (degreeModule != null) {
            final EquivalencePlan equivalencePlan = getEquivalencePlan(request);
            final Set<EquivalencePlanEntry> equivalencePlanEntries =
                    degreeModule.getNewDegreeModuleEquivalencePlanEntries(equivalencePlan);
            request.setAttribute("equivalencePlanEntries", equivalencePlanEntries);
        }
        return mapping.findForward("showPlan");
    }

    public ActionForward prepareAddEquivalency(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        EquivalencePlanEntryCreator equivalencePlanEntryCreator = getRenderedObject();
        final EquivalencePlan equivalencePlan;
        if (equivalencePlanEntryCreator == null) {
            equivalencePlan = getEquivalencePlan(request);
            final DegreeModule degreeModule = getDegreeModule(request);
            equivalencePlanEntryCreator = new EquivalencePlanEntryCreator(equivalencePlan);
            equivalencePlanEntryCreator.setDestinationDegreeModuleToAdd(degreeModule);
            equivalencePlanEntryCreator.addDestination(degreeModule);
        } else {
            equivalencePlan = equivalencePlanEntryCreator.getEquivalencePlan();
            equivalencePlanEntryCreator.setDestinationDegreeModuleToAdd(null);
            equivalencePlanEntryCreator.setOriginDegreeModuleToAdd(null);
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("equivalencePlan", equivalencePlan);
        request.setAttribute("equivalencePlanEntryCreator", equivalencePlanEntryCreator);
        return mapping.findForward("addEquivalency");
    }

    public ActionForward deleteEquivalency(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final EquivalencePlanEntry equivalencePlanEntry = getEquivalencePlanEntry(request);
        DeleteEquivalencePlanEntry.runDeleteEquivalencePlanEntry(equivalencePlanEntry);
        return mapping.findForward("showPlan");
    }

    private EquivalencePlanEntry getEquivalencePlanEntry(HttpServletRequest request) {
        final String equivalencePlanEntryIDString = request.getParameter("equivalencePlanEntryID");
        return FenixFramework.getDomainObject(equivalencePlanEntryIDString);
    }

    private DegreeModule getDegreeModule(HttpServletRequest request) {
        final String degreeModuleIDString = request.getParameter("degreeModuleID");
        return FenixFramework.getDomainObject(degreeModuleIDString);
    }

    private EquivalencePlan getEquivalencePlan(HttpServletRequest request) {
        final String equivalencePlanIDString = request.getParameter("equivalencePlanID");
        return FenixFramework.getDomainObject(equivalencePlanIDString);
    }

    private DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
        final String degreeCurricularPlanIDString = request.getParameter("degreeCurricularPlanID");
        return FenixFramework.getDomainObject(degreeCurricularPlanIDString);
    }

}
