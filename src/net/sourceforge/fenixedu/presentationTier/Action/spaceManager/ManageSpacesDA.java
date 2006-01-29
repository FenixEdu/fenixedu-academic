package net.sourceforge.fenixedu.presentationTier.Action.spaceManager;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ManageSpacesDA extends FenixDispatchAction {

    public ActionForward viewSpaces(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final IUserView userView = getUserView(request);

        final Object[] args = { Space.class };
        final Collection<Space> spaces = (Collection<Space>) ServiceUtils.executeService(userView, "ReadAllDomainObjects", args);
        request.setAttribute("spaces", spaces);

        return mapping.findForward("ShowSpaces");
    }

    public ActionForward manageSpace(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final IUserView userView = getUserView(request);

        final String spaceID = request.getParameter("spaceID");

        final Object[] args = { Space.class, Integer.valueOf(spaceID) };
        final Space space = (Space) ServiceUtils.executeService(userView, "ReadDomainObject", args);
        request.setAttribute("selectedSpace", space);
        request.setAttribute("spaces", space.getContainedSpaces());

        return mapping.findForward("ManageSpace");
    }

    public ActionForward showCreateSpaceForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("ShowCreateSpaceForm");
    }

    public ActionForward createCampus(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final IUserView userView = getUserView(request);

        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String spaceName = (String) dynaActionForm.get("spaceName");

        final Object[] args = { spaceName };
        ServiceUtils.executeService(userView, "CreateCampus", args);

        return mapping.findForward("ShowSpaces");
    }

    public ActionForward createBuilding(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final IUserView userView = getUserView(request);

        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String spaceName = (String) dynaActionForm.get("spaceName");

        final Object[] args = { spaceName };
        ServiceUtils.executeService(userView, "CreateBuilding", args);

        return mapping.findForward("ShowSpaces");
    }

    public ActionForward deleteSpace(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final IUserView userView = getUserView(request);

        final String spaceID = request.getParameter("spaceID");

        final Object[] args = { Integer.valueOf(spaceID) };
        ServiceUtils.executeService(userView, "DeleteSpace", args);

        return viewSpaces(mapping, form, request, response);
    }

}