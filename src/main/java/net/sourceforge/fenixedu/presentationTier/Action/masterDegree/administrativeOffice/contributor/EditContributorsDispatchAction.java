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
/*
 * Created on 14/Mar/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.contributor;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AcademicAdministrationApplication.AcademicAdminContributorsApp;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = AcademicAdminContributorsApp.class, path = "edit",
        titleKey = "link.masterDegree.administrativeOffice.editContributor", accessGroup = "academic(MANAGE_CONTRIBUTORS)")
@Mapping(path = "/editContributors", module = "academicAdministration", formBean = "chooseContributorForm")
@Forwards({ @Forward(name = "PrepareReady", path = "/academicAdminOffice/contributor/chooseContributor.jsp"),
        @Forward(name = "ActionReady", path = "/academicAdministration/editContributor.do?method=prepareEdit&amp;page=0"),
        @Forward(name = "ChooseContributor", path = "/academicAdminOffice/contributor/selectContributorFromList.jsp") })
@Exceptions(value = { @ExceptionHandling(
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException.class,
        key = "resources.Action.exceptions.ExistingActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class EditContributorsDispatchAction extends ListContributorsDispatchAction {

    @Override
    protected void setActionToRequest(HttpServletRequest request) {
        request.setAttribute(PresentationConstants.CONTRIBUTOR_ACTION, "label.action.contributor.edit");
    }
}