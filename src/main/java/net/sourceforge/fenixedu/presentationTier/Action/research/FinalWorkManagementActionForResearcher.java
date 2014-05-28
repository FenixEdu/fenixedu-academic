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
package net.sourceforge.fenixedu.presentationTier.Action.research;

import net.sourceforge.fenixedu.presentationTier.Action.research.ResearcherApplication.ResearcherFinalWorkApp;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.FinalWorkManagementAction;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = ResearcherFinalWorkApp.class, path = "final-work-management",
        titleKey = "link.manage.finalWork.candidacies")
@Mapping(path = "/finalWorkManagement", module = "researcher", formBean = "finalWorkInformationForm",
        input = "/finalWorkManagement.do?method=prepareFinalWorkInformation")
public class FinalWorkManagementActionForResearcher extends FinalWorkManagementAction {

}
