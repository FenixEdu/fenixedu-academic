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
package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.SelectCandidatesDispatchAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/displayListToSelectCandidates", module = "coordinator",
        input = "/candidate/displayCandidateListBySituation_bd.jsp", formBean = "chooseCandidateSituationForm",
        functionality = DegreeCoordinatorIndex.class)
@Forwards({
        @Forward(name = "PrepareSuccess", path = "/coordinator/candidate/displayCandidateListBySituation_bd.jsp"),
        @Forward(name = "CancelConfirmation",
                path = "/coordinator/displayListToSelectCandidates.do?method=prepareSelectCandidates"),
        @Forward(name = "RequestConfirmation", path = "/coordinator/candidate/warning_bd.jsp"),
        @Forward(name = "FinalPresentation", path = "/coordinator/candidate/displayCandidatesFinalList_bd.jsp"),
        @Forward(name = "ChooseSuccess", path = "/coordinator/displayListToSelectCandidates.do?method=preparePresentation&page=0"),
        @Forward(name = "OrderCandidates",
                path = "/coordinator/displayListToSelectCandidates.do?method=getSubstituteCandidates&page=0"),
        @Forward(name = "Cancel", path = "/coordinator/displayListToSelectCandidates.do?method=prepareSelectCandidates&page=0"),
        @Forward(name = "OrderCandidatesReady", path = "/coordinator/candidate/displayChosenSelection_bd.jsp"),
        @Forward(name = "NumerusClaususNotDefined", path = "/coordinator/candidate/numerusClaususNotDefined_bd.jsp"),
        @Forward(name = "BackError", path = "/coordinator/candidate/backErrorPage_bd.jsp"),
        @Forward(name = "PrintReady", path = "/coordinator/candidate/approvalDispatchTemplate.jsp") })
public class SelectCandidatesDispatchActionForCoordinator extends SelectCandidatesDispatchAction {

}
