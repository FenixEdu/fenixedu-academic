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
package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.delegates;

import net.sourceforge.fenixedu.presentationTier.Action.commons.delegates.DelegatesManagementDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.PedagogicalCouncilApp.PedagogicalDelegatesApp;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = PedagogicalDelegatesApp.class, path = "manage", titleKey = "link.delegatesManagement")
@Mapping(module = "pedagogicalCouncil", path = "/delegatesManagement")
@Forwards(
        value = {
                @Forward(name = "showGGAEDelegates", path = "/pedagogicalCouncil/findDelegates.do?method=searchByDelegateType"),
                @Forward(
                        name = "showPossibleDelegates",
                        path = "/pedagogicalCouncil/electionsPeriodsManagement.do?method=showVotingResults&forwardTo=showPossibleDelegates"),
                @Forward(name = "createEditDelegates", path = "/pedagogicalCouncil/delegates/createEditDelegates.jsp"),
                @Forward(name = "prepareViewGGAEDelegates",
                        path = "/pedagogicalCouncil/delegatesManagement.do?method=prepareViewGGAEDelegates"),
                @Forward(name = "prepareViewDelegates",
                        path = "/pedagogicalCouncil/delegatesManagement.do?method=prepareViewDelegates"),
                @Forward(name = "createEditGGAEDelegates", path = "/pedagogicalCouncil/delegates/createEditGGAEDelegates.jsp") })
public class DelegatesManagementDispatchActionForPedagogicalCouncil extends DelegatesManagementDispatchAction {
}