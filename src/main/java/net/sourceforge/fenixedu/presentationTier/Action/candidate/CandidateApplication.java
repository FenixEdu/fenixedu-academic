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
package net.sourceforge.fenixedu.presentationTier.Action.candidate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyOperationType;
import net.sourceforge.fenixedu.domain.candidacy.CandidacySituation;
import net.sourceforge.fenixedu.domain.candidacy.CandidacySituationType;
import net.sourceforge.fenixedu.domain.candidacy.DegreeCandidacy;
import net.sourceforge.fenixedu.domain.candidacy.IMDCandidacy;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.StrutsApplication;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

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

        if (getUserView(request).getPerson().getCandidaciesSet().size() == 1) {
            final Candidacy candidacy = getUserView(request).getPerson().getCandidaciesSet().iterator().next();

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
