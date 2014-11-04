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
package org.fenixedu.academic.ui.struts.action.masterDegree.administrativeOffice;

import org.fenixedu.academic.ui.struts.action.commons.ChooseExecutionYearToCandidateStudyPlanDA;

import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(path = "/displayCandidateListToMakeStudyPlan", module = "masterDegreeAdministrativeOffice",
        input = "/candidate/displayCandidateListToMakeStudyPlan_bd.jsp", formBean = "chooseSecondMasterDegreeForm",
        functionality = ChooseExecutionYearToCandidateStudyPlanDA.class)
@Forwards({
        @Forward(name = "PrepareSuccess",
                path = "/masterDegreeAdministrativeOffice/candidate/displayCandidateListToMakeStudyPlan_bd.jsp"),
        @Forward(name = "PrepareSecondChooseMasterDegreeReady",
                path = "/masterDegreeAdministrativeOffice/candidate/secondChooseMasterDegree_bd.jsp"),
        @Forward(name = "ChooseReady",
                path = "/masterDegreeAdministrativeOffice/displayCourseListToStudyPlan.do?method=chooseMasterDegree&page=0"),
        @Forward(name = "ChooseSuccess",
                path = "/masterDegreeAdministrativeOffice/displayCourseListToStudyPlan.do?method=prepareSelectCourseList&page=0"),
        @Forward(name = "PrintReady", path = "/masterDegreeAdministrativeOffice/candidate/studyPlanTemplate.jsp"),
        @Forward(name = "BackError", path = "/masterDegreeAdministrativeOffice/candidate/backErrorPage_bd.jsp") })
public class DisplayCandidateListToMakeStudyPlan extends MakeCandidateStudyPlanDispatchAction {

}
