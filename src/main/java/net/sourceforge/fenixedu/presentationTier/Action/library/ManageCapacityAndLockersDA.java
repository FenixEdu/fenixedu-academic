package net.sourceforge.fenixedu.presentationTier.Action.library;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.space.RoomSubdivision;
import net.sourceforge.fenixedu.domain.space.RoomSubdivisionInformation;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.Atomic;

@StrutsFunctionality(app = LibraryApplication.class, path = "manage-capacity-and-lockers",
        titleKey = "label.manage.capacity.lockers")
@Mapping(path = "/manageCapacityAndLockers", module = "library")
@Forwards(@Forward(name = "libraryUpdateCapacityAndLockers", path = "/library/operator/libraryUpdateCapacityAndLockers.jsp"))
public class ManageCapacityAndLockersDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareUpdateCapacityAndLockers(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("libraryInformation", new LibraryInformation());
        return mapping.findForward("libraryUpdateCapacityAndLockers");
    }

    public ActionForward selectLibraryToUpdate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        LibraryInformation libraryInformation = getRenderedObject("libraryInformation");

        Space library = libraryInformation.getLibrary();

        if (library != null) {
            libraryInformation.setCapacity(library.getSpaceInformation().getCapacity());
            libraryInformation.setLockers(library.getActiveContainedSpacesCount());
        }

        RenderUtils.invalidateViewState();
        request.setAttribute("libraryInformation", libraryInformation);
        return mapping.findForward("libraryUpdateCapacityAndLockers");
    }

    @Atomic
    public ActionForward updateCapacityAndLockers(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        LibraryInformation libraryInformation = getRenderedObject("libraryUpdate");

        Space library = libraryInformation.getLibrary();
        setCapacity(library, libraryInformation.getCapacity());
        setLockers(library, libraryInformation.getLockers(), new YearMonthDay());

        libraryInformation.setCapacity(libraryInformation.getLibrary().getSpaceInformation().getCapacity());
        libraryInformation.setLockers(libraryInformation.getLibrary().getActiveContainedSpacesCount());

        request.setAttribute("libraryInformation", libraryInformation);
        return mapping.findForward("libraryUpdateCapacityAndLockers");
    }

    public ActionForward handleInvalidCapacityOrLockers(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        LibraryInformation libraryInformation = getRenderedObject("libraryUpdate");
        request.setAttribute("libraryInformation", libraryInformation);
        request.setAttribute("libraryUpdate", libraryInformation);
        return mapping.findForward("libraryUpdateCapacityAndLockers");
    }

    private void setCapacity(Space library, int capacity) {
        library.getSpaceInformation().setCapacity(capacity);
    }

    private void setLockers(Space library, int lockers, YearMonthDay today) {
        int highestLocker = 0;
        for (Space space : library.getActiveContainedSpaces()) {
            RoomSubdivisionInformation info = (RoomSubdivisionInformation) space.getSpaceInformation();
            int lockerNumber = Integer.parseInt(info.getIdentification());
            if (lockerNumber > lockers) {
                space.getMostRecentSpaceInformation().setValidUntil(today);
            } else {
                setCapacity(space, 1);
            }
            if (lockerNumber > highestLocker) {
                highestLocker = lockerNumber;
            }
        }
        if (highestLocker < lockers) {
            for (int i = highestLocker + 1; i <= lockers; i++) {
                RoomSubdivision room =
                        new RoomSubdivision(library, StringUtils.leftPad(Integer.toString(i), String.valueOf(lockers).length(),
                                '0'), today, null);
                setCapacity(room, 1);
            }
        }
    }

}
