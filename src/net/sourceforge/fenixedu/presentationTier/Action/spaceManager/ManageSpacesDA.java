package net.sourceforge.fenixedu.presentationTier.Action.spaceManager;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.space.OldBuilding;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.space.SpaceComparator;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ManageSpacesDA extends FenixDispatchAction {

    public ActionForward viewSpaces(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	final SortedSet<Space> spaces = new TreeSet<Space>(SpaceComparator.SPACE_COMPARATOR_BY_CLASS);

    	for (final Space space : rootDomainObject.getSpacesSet()) {
    		if (!(space instanceof OldBuilding) && !(space instanceof OldRoom) && !space.hasSuroundingSpace()) {
    			spaces.add(space);
    		}
    	}

        request.setAttribute("spaces", spaces);
        return mapping.findForward("ShowSpaces");
    }

    private FactoryExecutor getFactoryObject() {
    	return (FactoryExecutor) RenderUtils.getViewState().getMetaObject().getObject();
    }

    public ActionForward executeFactoryMethod(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    		throws Exception {
        final Object[] args = { getFactoryObject() };
        final Object serviceResult = executeService(request, "ExecuteFactoryMethod", args);

        final Space space;
        final SpaceInformation spaceInformation;
        if (serviceResult instanceof Space) {
        	space = (Space) serviceResult;
        	spaceInformation = space.getSpaceInformation();
        } else if (serviceResult instanceof SpaceInformation) {
        	spaceInformation = (SpaceInformation) serviceResult;
        	space = spaceInformation.getSpace();
        } else {
        	return viewSpaces(mapping, form, request, response);
        }
        request.setAttribute("selectedSpace", space);
        request.setAttribute("selectedSpaceInformation", spaceInformation);
        return mapping.findForward("ManageSpace");
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

    public ActionForward viewSpaceInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Space space = getSpaceAndSetSelectedSpace(request);
        request.setAttribute("spaces", space.getContainedSpaces());
        return mapping.findForward("ViewSpaceInformation");
    }

    public ActionForward prepareEditSpace(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        viewSpaceInformation(mapping, form, request, response);
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

    public ActionForward prepareCreateSpaceInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    		throws Exception {
        final String spaceInformationIDString = request.getParameter("spaceInformationID");
        final Integer spaceInformationID = Integer.valueOf(spaceInformationIDString);

        final SpaceInformation spaceInformation = rootDomainObject.readSpaceInformationByOID(spaceInformationID);
        final FactoryExecutor factoryExecutor = spaceInformation.getSpaceFactoryEditor();

        request.setAttribute("SpaceFactoryEditor", factoryExecutor);

        return mapping.findForward("CreateSpaceInformation");
    }

    public ActionForward deleteSpace(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final IUserView userView = getUserView(request);

        final String spaceIDString = request.getParameter("spaceID");
        final Integer spaceID = Integer.valueOf(spaceIDString);
        final Space space = (Space) readDomainObject(request, Space.class, spaceID);
        final Space surroundingSpace = space.getSuroundingSpace();

        final Object[] args = { spaceID };
        ServiceUtils.executeService(userView, "DeleteSpace", args);

        if (surroundingSpace == null) {
            return viewSpaces(mapping, form, request, response);
        } else {
            final SpaceInformation newSpaceInformation = surroundingSpace.getOrderedSpaceInformations().last();
            request.setAttribute("selectedSpace", surroundingSpace);
            request.setAttribute("selectedSpaceInformation", newSpaceInformation);
            request.setAttribute("spaces", surroundingSpace.getContainedSpaces());

            return mapping.findForward("ManageSpace");
        }
    }

    public ActionForward deleteSpaceInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final IUserView userView = getUserView(request);

        final String spaceInformationIDString = request.getParameter("spaceInformationID");
        final Integer spaceInformationID = Integer.valueOf(spaceInformationIDString);
        
        final SpaceInformation oldSpaceInformation = (SpaceInformation)
                readDomainObject(request, SpaceInformation.class, spaceInformationID);
        final Space space = oldSpaceInformation.getSpace();

        final Object[] args = { spaceInformationID };
        ServiceUtils.executeService(userView, "DeleteSpaceInformation", args);

        final SpaceInformation newSpaceInformation = space.getOrderedSpaceInformations().last();

        request.setAttribute("selectedSpace", space);
        request.setAttribute("selectedSpaceInformation", newSpaceInformation);
        request.setAttribute("spaces", space.getContainedSpaces());

        return mapping.findForward("ManageSpace");
    }

}