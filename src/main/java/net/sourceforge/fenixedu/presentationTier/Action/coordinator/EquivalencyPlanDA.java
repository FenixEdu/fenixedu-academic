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

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.DeleteEquivalencePlanEntry;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EquivalencePlan;
import net.sourceforge.fenixedu.domain.EquivalencePlanEntry;
import net.sourceforge.fenixedu.domain.EquivalencePlanEntry.EquivalencePlanEntryCreator;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
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
            final Set<DegreeType> degreeTypes = new HashSet<DegreeType>();
            degreeTypes.add(DegreeType.BOLONHA_DEGREE);
            degreeTypes.add(DegreeType.BOLONHA_MASTER_DEGREE);
            degreeTypes.add(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
            request.setAttribute("degreeCurricularPlans", DegreeCurricularPlan.getDegreeCurricularPlans(degreeTypes));
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
