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
package org.fenixedu.academic.ui.struts.action.scientificCouncil.candidacy.degreeTransfer;

import org.fenixedu.academic.ui.struts.FenixActionForm;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(path = "/caseHandlingDegreeTransferIndividualCandidacyProcess", module = "scientificCouncil",
        formBeanClass = FenixActionForm.class, functionality = DegreeTransferCandidacyProcessDA.class)
@Forwards({
        @Forward(name = "intro",
                path = "/scientificCouncil/caseHandlingDegreeTransferCandidacyProcess.do?method=listProcessAllowedActivities"),
        @Forward(name = "list-allowed-activities",
                path = "/scientificCouncil/candidacy/degreeTransfer/listIndividualCandidacyActivities.jsp") })
public class DegreeTransferIndividualCandidacyProcessDA extends
        org.fenixedu.academic.ui.struts.action.candidacy.degreeTransfer.DegreeTransferIndividualCandidacyProcessDA {

}
