package net.sourceforge.fenixedu.presentationTier.Action.spaceManager;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
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

	private Space getSpaceAndSetSelectedSpace(HttpServletRequest request) throws FenixFilterException, FenixServiceException {
		final IUserView userView = getUserView(request);
        final String spaceIDString = request.getParameter("spaceID");
        final Integer spaceID = spaceIDString != null ? Integer.valueOf(spaceIDString) : null;
        final Object[] args = { Space.class, spaceID };
        final Space space = (Space) ServiceUtils.executeService(userView, "ReadDomainObject", args);
        request.setAttribute("selectedSpace", space);
        return space;
	}

    public ActionForward manageSpace(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Space space = getSpaceAndSetSelectedSpace(request);
        request.setAttribute("spaces", space.getContainedSpaces());
        return mapping.findForward("ManageSpace");
    }

    public ActionForward prepareEditSpace(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Space space = getSpaceAndSetSelectedSpace(request);
        request.setAttribute("spaces", space.getContainedSpaces());
        return mapping.findForward("EditSpace");
    }

    public ActionForward showCreateSpaceForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("ShowCreateSpaceForm");
    }

    public ActionForward showCreateSubSpaceForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        getSpaceAndSetSelectedSpace(request);
        return mapping.findForward("ShowCreateSubSpaceForm");
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
        final String suroundingSpaceIDString = (String) dynaActionForm.get("suroundingSpaceID");
        final Integer suroundingSpaceID = Integer.valueOf(suroundingSpaceIDString);
        final String spaceName = (String) dynaActionForm.get("spaceName");

        final Object[] args = { suroundingSpaceID, spaceName };
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

    public ActionForward editCampus(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final IUserView userView = getUserView(request);

        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String spaceIDString = (String) dynaActionForm.get("spaceID");
        final Integer spaceID = spaceIDString != null && spaceIDString.length() > 0 ?
                Integer.valueOf(spaceIDString) : null;
        final String spaceName = (String) dynaActionForm.get("spaceName");

        final Object[] args = { spaceID, spaceName };
        ServiceUtils.executeService(userView, "EditCampus", args);

        return mapping.findForward("ShowSpaces");
    }

    public ActionForward editBuilding(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final IUserView userView = getUserView(request);

        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String spaceIDString = (String) dynaActionForm.get("spaceID");
        final Integer spaceID = spaceIDString != null && spaceIDString.length() > 0 ?
                Integer.valueOf(spaceIDString) : null;
        final String spaceName = (String) dynaActionForm.get("spaceName");

        final Object[] args = { spaceID, spaceName };
        ServiceUtils.executeService(userView, "EditBuilding", args);

        return mapping.findForward("ShowSpaces");
    }

}