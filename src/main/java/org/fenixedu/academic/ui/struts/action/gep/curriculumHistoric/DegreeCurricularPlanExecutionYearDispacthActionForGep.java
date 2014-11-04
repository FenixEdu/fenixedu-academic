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
package org.fenixedu.academic.ui.struts.action.gep.curriculumHistoric;

import org.fenixedu.academic.ui.struts.action.commons.curriculumHistoric.DegreeCurricularPlanExecutionYearDispacthAction;
import org.fenixedu.academic.ui.struts.action.gep.GepApplication.GepPortalApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = GepPortalApp.class, path = "curriculum-historic", titleKey = "link.curriculumHistoric",
        bundle = "CurriculumHistoricResources")
@Mapping(module = "gep", path = "/chooseExecutionYearAndDegreeCurricularPlan",
        input = "/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare&page=0")
@Forwards({ @Forward(name = "chooseExecutionYear", path = "/commons/curriculumHistoric/chooseDegreeCPlanExecutionYear.jsp"),
        @Forward(name = "showActiveCurricularCourses", path = "/commons/curriculumHistoric/showActiveCurricularCourseScopes.jsp") })
public class DegreeCurricularPlanExecutionYearDispacthActionForGep extends DegreeCurricularPlanExecutionYearDispacthAction {
}