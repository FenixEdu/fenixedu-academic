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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.candidacy;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.dto.administrativeOffice.candidacy.DFACandidacyBean;
import org.fenixedu.academic.domain.candidacy.DFACandidacy;
import org.fenixedu.academic.ui.struts.action.masterDegree.administrativeOffice.MasterDegreeOfficeApplication.MasterDegreeDfaApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@StrutsFunctionality(app = MasterDegreeDfaApp.class, path = "list-candidacies",
        titleKey = "link.masterDegree.administrativeOffice.dfaCandidacy.listCandidacies")
@Mapping(path = "/listDFACandidacy", module = "masterDegreeAdministrativeOffice", input = "/candidacy/listCandidacies.jsp")
@Forwards(@Forward(name = "listCandidacies", path = "/masterDegreeAdministrativeOffice/candidacy/listCandidacies.jsp"))
public class ListDFACandidaciesDA extends DFACandidacyDispatchAction {

    @EntryPoint
    public ActionForward prepareListCandidacies(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        DFACandidacyBean candidacyBean = new DFACandidacyBean();
        request.setAttribute("candidacyBean", candidacyBean);
        return mapping.findForward("listCandidacies");
    }

    public ActionForward listCandidacies(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        DFACandidacyBean dfaCandidacyBean = (DFACandidacyBean) RenderUtils.getViewState().getMetaObject().getObject();
        Set<DFACandidacy> candidacies = dfaCandidacyBean.getExecutionDegree().getDfaCandidacies();
        request.setAttribute("candidacies", candidacies);
        request.setAttribute("candidacyBean", dfaCandidacyBean);
        return mapping.findForward("listCandidacies");
    }

}
