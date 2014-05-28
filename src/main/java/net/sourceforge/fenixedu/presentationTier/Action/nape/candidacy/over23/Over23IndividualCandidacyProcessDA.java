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
package net.sourceforge.fenixedu.presentationTier.Action.nape.candidacy.over23;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/caseHandlingOver23IndividualCandidacyProcess", module = "nape",
        formBeanClass = Over23IndividualCandidacyProcessDA.CandidacyForm.class, functionality = Over23CandidacyProcessDA.class)
@Forwards({ @Forward(name = "intro", path = "/nape/candidacy/mainCandidacyProcess.jsp"),
        @Forward(name = "list-allowed-activities", path = "/nape/candidacy/over23/listIndividualCandidacyActivities.jsp") })
public class Over23IndividualCandidacyProcessDA extends
        net.sourceforge.fenixedu.presentationTier.Action.candidacy.over23.Over23IndividualCandidacyProcessDA {
}
