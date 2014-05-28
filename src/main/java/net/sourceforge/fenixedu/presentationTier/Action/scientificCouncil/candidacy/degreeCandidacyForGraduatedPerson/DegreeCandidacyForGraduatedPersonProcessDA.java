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
package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.candidacy.degreeCandidacyForGraduatedPerson;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.CandidacyProcessDA;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.DegreeCoordinatorIndex;
import net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.ScientificCouncilApplication.ScientificApplicationsApp;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = ScientificApplicationsApp.class, path = "degree-candidacy-for-graduated",
        titleKey = "title.application.name.degreeCandidacyForGraduatedPerson")
@Mapping(path = "/caseHandlingDegreeCandidacyForGraduatedPersonProcess", module = "scientificCouncil",
        formBeanClass = CandidacyProcessDA.CandidacyProcessForm.class)
@Forwards(@Forward(name = "intro", path = "/scientificCouncil/candidacy/mainCandidacyProcess.jsp"))
public class DegreeCandidacyForGraduatedPersonProcessDA extends
        net.sourceforge.fenixedu.presentationTier.Action.candidacy.graduatedPerson.DegreeCandidacyForGraduatedPersonProcessDA {

    public DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
        final String degreeCurricularPlanOID = DegreeCoordinatorIndex.findDegreeCurricularPlanID(request);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanOID);

        if (degreeCurricularPlanOID != null) {
            return FenixFramework.getDomainObject(degreeCurricularPlanOID);
        }

        return null;
    }

}
