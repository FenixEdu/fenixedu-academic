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

@StrutsFunctionality(app = MasterDegreeCandidatesApp.class, path = "visualize-candidates",
        titleKey = "link.masterDegree.administrativeOffice.visualizeCandidateInformations")
@Mapping(path = "/chooseExecutionYearToVisualizeCandidates", module = "masterDegreeAdministrativeOffice",
        input = "/chooseExecutionYear_bd.jsp", formBean = "chooseExecutionYearForm")
@Forwards(@Forward(name = "ChooseSuccess",
        path = "/masterDegreeAdministrativeOffice/visualizeCandidates.do?method=prepareChoose&action=visualize"))
public class ChooseExecutionYearToVisualizeCandidatesDA extends ChooseExecutionYearDispatchAction {

}
