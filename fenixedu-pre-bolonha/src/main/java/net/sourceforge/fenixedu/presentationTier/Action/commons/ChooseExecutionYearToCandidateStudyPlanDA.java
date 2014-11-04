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
package org.fenixedu.academic.ui.struts.action.commons;

import org.fenixedu.academic.ui.struts.action.masterDegree.administrativeOffice.MasterDegreeOfficeApplication.MasterDegreeCandidatesApp;

import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = MasterDegreeCandidatesApp.class, path = "study-plan",
        titleKey = "link.masterDegree.administrativeOffice.makeStudyPlan")
@Mapping(path = "/chooseExecutionYearToCandidateStudyPlan", module = "masterDegreeAdministrativeOffice",
        input = "/chooseExecutionYear_bd.jsp", formBean = "chooseExecutionYearForm")
@Forwards({
        @Forward(name = "DisplayMasterDegreeList",
                path = "/masterDegreeAdministrativeOffice/candidate/displayMasterDegreesToEditCandidates_bd.jsp"),
        @Forward(
                name = "MasterDegreeReady",
                path = "/masterDegreeAdministrativeOffice/candidate/displayCurricularPlanByChosenMasterDegreeToEditCandidates_bd.jsp"),
        @Forward(name = "PrepareSuccess", path = "/masterDegreeAdministrativeOffice/chooseExecutionYear_bd.jsp"),
        @Forward(
                name = "ChooseSuccess",
                path = "/masterDegreeAdministrativeOffice/displayCandidateListToMakeStudyPlan.do?method=prepareSelectCandidates&page=0") })
public class ChooseExecutionYearToCandidateStudyPlanDA extends ChooseExecutionYearDispatchAction {

}
