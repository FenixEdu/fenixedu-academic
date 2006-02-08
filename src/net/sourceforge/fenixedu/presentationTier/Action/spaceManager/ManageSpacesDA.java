package net.sourceforge.fenixedu.presentationTier.Action.spaceManager;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;
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
        final String spaceInformationIDString = (request.getParameterMap().containsKey("spaceInformationID") ?
        		request.getParameter("spaceInformationID") : (String) request.getAttribute("spaceInformationID"));
        final Integer spaceInformationID = spaceInformationIDString != null ? Integer.valueOf(spaceInformationIDString) : null;
        final Object[] args = { SpaceInformation.class, spaceInformationID };
        final SpaceInformation spaceInformation = (SpaceInformation) ServiceUtils.executeService(userView, "ReadDomainObject", args);
        final Space space = spaceInformation.getSpace();
        request.setAttribute("selectedSpace", space);
        request.setAttribute("selectedSpaceInformation", spaceInformation);
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
        final Integer suroundingSpaceID = (suroundingSpaceIDString != null && suroundingSpaceIDString.length() > 0) ?
        	Integer.valueOf(suroundingSpaceIDString) : null;
        final String spaceName = (String) dynaActionForm.get("spaceName");

        final Object[] args = { suroundingSpaceID, spaceName };
        ServiceUtils.executeService(userView, "CreateBuilding", args);

        if (suroundingSpaceIDString == null || suroundingSpaceIDString.length() == 0) {
        	return mapping.findForward("ShowSpaces");
        } else {
        	request.setAttribute("spaceInformationID", suroundingSpaceIDString);
        	return manageSpace(mapping, form, request, response);
        }
    }

    public ActionForward createFloor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final IUserView userView = getUserView(request);

        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String suroundingSpaceIDString = (String) dynaActionForm.get("suroundingSpaceID");
        final Integer suroundingSpaceID = (suroundingSpaceIDString != null && suroundingSpaceIDString.length() > 0) ?
                Integer.valueOf(suroundingSpaceIDString) : null;
        final String levelString = (String) dynaActionForm.get("level");
        final Integer level = levelString != null && levelString.length() > 0 ?
                Integer.valueOf(levelString) : null;

        final Object[] args = { suroundingSpaceID, level };
        ServiceUtils.executeService(userView, "CreateFloor", args);

        request.setAttribute("spaceInformationID", suroundingSpaceIDString);
        return manageSpace(mapping, form, request, response);
    }

    public ActionForward createRoom(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final IUserView userView = getUserView(request);

        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String suroundingSpaceIDString = (String) dynaActionForm.get("suroundingSpaceID");
        final Integer suroundingSpaceID = (suroundingSpaceIDString != null && suroundingSpaceIDString.length() > 0) ?
            Integer.valueOf(suroundingSpaceIDString) : null;
        final String spaceName = (String) dynaActionForm.get("spaceName");

        final Object[] args = { suroundingSpaceID, spaceName };
        ServiceUtils.executeService(userView, "CreateRoom", args);

        request.setAttribute("spaceInformationID", suroundingSpaceIDString);
        return manageSpace(mapping, form, request, response);
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
        final String spaceInformationIDString = (String) dynaActionForm.get("spaceInformationID");
        final Boolean asNewVersion = (Boolean) dynaActionForm.get("asNewVersion");
        final Integer spaceInformationID = spaceInformationIDString != null && spaceInformationIDString.length() > 0 ?
                Integer.valueOf(spaceInformationIDString) : null;
        final String spaceName = (String) dynaActionForm.get("spaceName");

        final Object[] args = { spaceInformationID, asNewVersion, spaceName };
        ServiceUtils.executeService(userView, "EditCampus", args);

        return manageSpace(mapping, form, request, response);
    }

    public ActionForward editBuilding(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final IUserView userView = getUserView(request);

        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String spaceInformationIDString = (String) dynaActionForm.get("spaceInformationID");
        final Boolean asNewVersion = (Boolean) dynaActionForm.get("asNewVersion");
        final Integer spaceInformationID = spaceInformationIDString != null && spaceInformationIDString.length() > 0 ?
                Integer.valueOf(spaceInformationIDString) : null;
        final String spaceName = (String) dynaActionForm.get("spaceName");

        final Object[] args = { spaceInformationID, asNewVersion, spaceName };
        ServiceUtils.executeService(userView, "EditBuilding", args);

        return manageSpace(mapping, form, request, response);
    }

    public ActionForward editFloor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final IUserView userView = getUserView(request);

        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String spaceInformationIDString = (String) dynaActionForm.get("spaceInformationID");
        final Boolean asNewVersion = (Boolean) dynaActionForm.get("asNewVersion");
        final Integer spaceInformationID = spaceInformationIDString != null && spaceInformationIDString.length() > 0 ?
                Integer.valueOf(spaceInformationIDString) : null;
        final String levelString = (String) dynaActionForm.get("level");
        final Integer level = levelString != null && levelString.length() > 0 ?
                        Integer.valueOf(levelString) : null;

        final Object[] args = { spaceInformationID, asNewVersion, level };
        ServiceUtils.executeService(userView, "EditFloor", args);

        return manageSpace(mapping, form, request, response);
    }

    public ActionForward editRoom(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final IUserView userView = getUserView(request);

        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String spaceInformationIDString = (String) dynaActionForm.get("spaceInformationID");
        final Boolean asNewVersion = (Boolean) dynaActionForm.get("asNewVersion");
        final Integer spaceInformationID = spaceInformationIDString != null && spaceInformationIDString.length() > 0 ?
                Integer.valueOf(spaceInformationIDString) : null;
        final String spaceName = (String) dynaActionForm.get("spaceName");

        final Object[] args = { spaceInformationID, asNewVersion, spaceName };
        ServiceUtils.executeService(userView, "EditRoom", args);

        return manageSpace(mapping, form, request, response);
    }

}