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
package org.fenixedu.academic.ui.struts.action.nape.candidacy.over23;

import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(path = "/caseHandlingOver23IndividualCandidacyProcess", module = "nape",
        formBeanClass = Over23IndividualCandidacyProcessDA.CandidacyForm.class, functionality = Over23CandidacyProcessDA.class)
@Forwards({ @Forward(name = "intro", path = "/nape/candidacy/mainCandidacyProcess.jsp"),
        @Forward(name = "list-allowed-activities", path = "/nape/candidacy/over23/listIndividualCandidacyActivities.jsp") })
public class Over23IndividualCandidacyProcessDA extends
        org.fenixedu.academic.ui.struts.action.candidacy.over23.Over23IndividualCandidacyProcessDA {
}
