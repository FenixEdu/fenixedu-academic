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
package org.fenixedu.academic.ui.struts.action.candidate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.candidacy.Candidacy;
import org.fenixedu.academic.domain.candidacy.CandidacyOperationType;
import org.fenixedu.academic.domain.candidacy.CandidacySituation;
import org.fenixedu.academic.domain.candidacy.CandidacySituationType;
import org.fenixedu.academic.domain.candidacy.DegreeCandidacy;
import org.fenixedu.academic.domain.candidacy.IMDCandidacy;
import org.fenixedu.academic.ui.struts.action.base.FenixAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsApplication;

@StrutsApplication(bundle = "CandidateResources", descriptionKey = "portal.candidate", path = "candidate",
        titleKey = "portal.candidate", accessGroup = "role(CANDIDATE)", hint = "Candidate")
@Mapping(module = "candidate", path = "/index")
@Forwards(value = {
        @Forward(name = "showCandidacyDetails", path = "/candidate/degreeCandidacyManagement.do?method=showCandidacyDetails"),
        @Forward(name = "fillData", path = "/candidate/degreeCandidacyManagement.do?method=doOperation"),
        @Forward(name = "showWelcome", path = "/candidate/index.jsp") })
public class CandidateApplication extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Candidacy candidacy =
                getUserView(request).getPerson().getCandidaciesSet().stream().filter(Candidacy::isActive)
                        .filter(c -> !c.isConcluded()).findFirst().get();
        if (candidacy != null) {
            if (candidacy instanceof DegreeCandidacy || candidacy instanceof IMDCandidacy) {
                request.setAttribute("candidacyID", candidacy.getExternalId());
                final CandidacySituation activeCandidacySituation = candidacy.getActiveCandidacySituation();
                if (activeCandidacySituation != null
                        && CandidacySituationType.STAND_BY == activeCandidacySituation.getCandidacySituationType()) {
                    request.setAttribute("operationType", CandidacyOperationType.FILL_PERSONAL_DATA.toString());
                    return mapping.findForward("fillData");
                } else {
                    return mapping.findForward("showCandidacyDetails");
                }
            }
        }

        return mapping.findForward("showWelcome");
    }

    @StrutsApplication(bundle = "CandidateResources", path = "candidacies", titleKey = "link.candidacies",
            accessGroup = "role(CANDIDATE)", hint = "Candidate")
    public static class CandidateCandidaciesApp {
    }

}
