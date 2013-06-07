package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.CreateCurrentDegreeInfo;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteManagementDA;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Tânia Pousão Created on 31/Out/2003
 */
@Mapping(module = "coordinator", path = "/degreeSiteManagement", input = "/degreeSiteManagement.do?method=subMenu&page=0",
        attribute = "degreeInfoForm", formBean = "degreeInfoForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "editOK", path = "editOK", tileProperties = @Tile(
                title = "private.coordinator.management.courses.management.scientificcommittee")),
        @Forward(name = "organizeItems", path = "manage-degree-organizeItems"),
        @Forward(name = "degreeSiteMenu", path = "degreeSiteMenu"), @Forward(name = "viewHistoric", path = "viewHistoric"),
        @Forward(name = "organizeFiles", path = "manage-degree-organizeFiles"),
        @Forward(name = "edit-fileItem-name", path = "manage-degree-editFileItemName"),
        @Forward(name = "editSectionPermissions", path = "manage-degree-editSectionPermissions"),
        @Forward(name = "confirmSectionDelete", path = "manage-degree-confirmSectionDelete"),
        @Forward(name = "editItemPermissions", path = "manage-degree-editItemPermissions"),
        @Forward(name = "createSection", path = "manage-degree-createSection"),
        @Forward(name = "section", path = "manage-degree-section"),
        @Forward(name = "editSection", path = "manage-degree-editSection"),
        @Forward(name = "uploadFile", path = "manage-degree-uploadFile"),
        @Forward(name = "viewInformation", path = "viewInformation"),
        @Forward(name = "sectionsManagement", path = "manage-degree-sectionsManagement"),
        @Forward(name = "createItem", path = "manage-degree-createItem"),
        @Forward(name = "viewDescriptionCurricularPlan", path = "viewDescriptionCurricularPlan"),
        @Forward(name = "editFile", path = "manage-degree-editFile"),
        @Forward(name = "editItem", path = "manage-degree-editItem") })
public class DegreeSiteManagementDispatchAction extends SiteManagementDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        CoordinatedDegreeInfo.setCoordinatorContext(request);
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        if (degreeCurricularPlan != null) {
            request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);
        }

        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected Site getSite(HttpServletRequest request) {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        if (degreeCurricularPlan != null) {
            return degreeCurricularPlan.getDegree().getSite();
        } else {
            return null;
        }
    }

    public DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
        String parameter = request.getParameter("degreeCurricularPlanID");

        if (parameter == null) {
            return null;
        }

        try {
            return AbstractDomainObject.fromExternalId(parameter);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Unit getUnit(HttpServletRequest request) {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        if (degreeCurricularPlan == null) {
            return null;
        } else {
            return degreeCurricularPlan.getDegree().getUnit();
        }
    }

    public ActionForward subMenu(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String degreeCurricularPlanId = request.getParameter("degreeCurricularPlanID");
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);
        return mapping.findForward("degreeSiteMenu");
    }

    public ActionForward viewInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        RequestUtils.getAndSetStringToRequest(request, "info");

        String degreeCurricularPlanID = RequestUtils.getAndSetStringToRequest(request, "degreeCurricularPlanID");
        DegreeCurricularPlan degreeCurricularPlan = AbstractDomainObject.fromExternalId(degreeCurricularPlanID);

        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        DegreeInfo currentDegreeInfo = currentExecutionYear.getDegreeInfo(degreeCurricularPlan.getDegree());

        if (currentDegreeInfo == null && currentExecutionYear.hasNextExecutionYear()) {
            currentDegreeInfo = currentExecutionYear.getNextExecutionYear().getDegreeInfo(degreeCurricularPlan.getDegree());
        }

        if (currentDegreeInfo == null) {
            final IUserView userView = UserView.getUser();

            if (!userView.getPerson().isCoordinatorFor(degreeCurricularPlan, currentExecutionYear)
                    && !userView.getPerson().isCoordinatorFor(degreeCurricularPlan, currentExecutionYear.getNextExecutionYear())) {
                final ActionErrors errors = new ActionErrors();
                errors.add("notAuthorized", new ActionError("error.exception.notAuthorized2"));
                saveErrors(request, errors);
                return new ActionForward(mapping.getInput());
            }

            currentDegreeInfo = CreateCurrentDegreeInfo.run(degreeCurricularPlan.getDegree());
        }

        request.setAttribute("currentDegreeInfo", currentDegreeInfo);

        return mapping.findForward("viewInformation");
    }

    public ActionForward editDegreeInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        RequestUtils.getAndSetStringToRequest(request, "info");
        RequestUtils.getAndSetStringToRequest(request, "degreeCurricularPlanID");

        return mapping.findForward("editOK");
    }

    public ActionForward viewDescriptionCurricularPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        String degreeCurricularPlanID = RequestUtils.getAndSetStringToRequest(request, "degreeCurricularPlanID");
        DegreeCurricularPlan degreeCurricularPlan = AbstractDomainObject.fromExternalId(degreeCurricularPlanID);
        if (degreeCurricularPlan == null) {
            final ActionErrors errors = new ActionErrors();
            errors.add("noDegreeCurricularPlan", new ActionError("error.coordinator.chosenDegree"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);

        return mapping.findForward("viewDescriptionCurricularPlan");
    }

    public ActionForward editDescriptionDegreeCurricularPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        RequestUtils.getAndSetStringToRequest(request, "degreeCurricularPlanID");
        return mapping.findForward("editOK");
    }

    public ActionForward viewHistoric(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        // read execution degree
        String degreeCurricularPlanID = RequestUtils.getAndSetStringToRequest(request, "degreeCurricularPlanID");
        DegreeCurricularPlan degreeCurricularPlan = AbstractDomainObject.fromExternalId(degreeCurricularPlanID);

        if (degreeCurricularPlan.hasAnyExecutionDegrees()) {
            final IUserView userView = UserView.getUser();
            request.setAttribute("executionDegrees", userView.getPerson().getCoordinatedExecutionDegrees(degreeCurricularPlan));
        }

        return mapping.findForward("viewHistoric");
    }

    @Override
    protected String getAuthorNameForFile(HttpServletRequest request) {
        Unit unit = getUnit(request);
        if (unit == null) {
            return null;
        } else {
            return unit.getName();
        }
    }

    @Override
    protected String getItemLocationForFile(HttpServletRequest request, Item item, Section section) {
        return getSite(request).getReversePath();
    }

}