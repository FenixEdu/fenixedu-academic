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
package org.fenixedu.academic.ui.struts.action.research.researchUnit;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.domain.organizationalStructure.ResearchUnit;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.ui.struts.action.commons.UnitFunctionalities;
import org.fenixedu.academic.ui.struts.action.research.ResearcherApplication.ResearcherResearchUnitApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = ResearcherResearchUnitApp.class, path = "choose-unit", titleKey = "label.researchUnits")
@Mapping(module = "researcher", path = "/researchUnitFunctionalities")
@Forwards(value = { @Forward(name = "uploadFile", path = "/commons/unitFiles/uploadFile.jsp"),
        @Forward(name = "manageFiles", path = "/commons/unitFiles/manageFiles.jsp"),
        @Forward(name = "editUploaders", path = "/commons/PersistentMemberGroups/configureUploaders.jsp"),
        @Forward(name = "managePersistedGroups", path = "/researcher/researchUnit/managePersistedGroups.jsp"),
        @Forward(name = "ShowUnitFunctionalities", path = "/researcher/researchUnit/showUnitFunctionalities.jsp"),
        @Forward(name = "editPublicationCollaborators", path = "/researcher/researchUnit/managePublicationCollaborators.jsp"),
        @Forward(name = "editFile", path = "/commons/unitFiles/editFile.jsp"),
        @Forward(name = "editPersistedGroup", path = "/commons/PersistentMemberGroups/editPersistedGroup.jsp"),
        @Forward(name = "createPersistedGroup", path = "/commons/PersistentMemberGroups/createPersistedGroup.jsp"),
        @Forward(name = "chooseUnit", path = "/researcher/researchUnit/chooseUnit.jsp") })
public class ResearchUnitFunctionalities extends UnitFunctionalities {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        request.setAttribute("functionalityAction", "researchUnitFunctionalities");
        request.setAttribute("module", "researcher");
        return super.execute(mapping, form, request, response);
    }

    @EntryPoint
    public ActionForward chooseUnit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Unit unit = getUnit(request);
        if (unit != null) {
            return prepare(mapping, form, request, response);
        }
        List<ResearchUnit> units = ResearchUnit.getWorkingResearchUnitsAndParents(AccessControl.getPerson());
        if (units.size() == 1) {
            request.setAttribute("unit", units.iterator().next());
            return prepare(mapping, form, request, response);
        }
        Collections.sort(units);
        request.setAttribute("units", units);
        return mapping.findForward("chooseUnit");
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        return mapping.findForward("ShowUnitFunctionalities");
    }

    public ActionForward configurePublicationCollaborators(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        return mapping.findForward("editPublicationCollaborators");
    }
}
