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