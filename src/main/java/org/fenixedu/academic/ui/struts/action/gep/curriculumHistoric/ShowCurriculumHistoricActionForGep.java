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

import org.fenixedu.academic.ui.struts.action.commons.curriculumHistoric.ShowCurriculumHistoricAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(module = "gep", path = "/showCurriculumHistoric",
        input = "/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare&page=0",
        functionality = DegreeCurricularPlanExecutionYearDispacthActionForGep.class)
@Forwards(@Forward(name = "show-report", path = "/commons/curriculumHistoric/showCurriculumHistoricReport.jsp"))
public class ShowCurriculumHistoricActionForGep extends ShowCurriculumHistoricAction {
}