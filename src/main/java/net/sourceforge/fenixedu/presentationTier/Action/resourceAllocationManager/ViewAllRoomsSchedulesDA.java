package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.Building;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.RAMApplication.RAMSchedulesApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

import com.google.common.collect.Ordering;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Luis Cruz e Sara Ribeiro
 */
@StrutsFunctionality(app = RAMSchedulesApp.class, path = "room-schedules", titleKey = "link.schedules.listAllByRoom")
@Mapping(path = "/viewAllRoomsSchedulesDA", module = "resourceAllocationManager")
@Forwards({ @Forward(name = "choose", path = "/resourceAllocationManager/choosePavillionsToViewRoomsSchedules.jsp"),
    @Forward(name = "list", path = "/resourceAllocationManager/viewAllRoomsSchedules.jsp") })
public class ViewAllRoomsSchedulesDA extends FenixDispatchAction {

    public static class ChooseBuildingBean implements Serializable {

        private static final long serialVersionUID = -663198492313971329L;

        private AcademicInterval academicInterval;
        private List<Building> selectedBuildings;

        public ChooseBuildingBean() {
            this.academicInterval = AcademicInterval.readDefaultAcademicInterval(AcademicPeriod.SEMESTER);
        }

        public AcademicInterval getAcademicInterval() {
            return academicInterval;
        }

        public void setAcademicInterval(AcademicInterval academicInterval) {
            this.academicInterval = academicInterval;
        }

        public List<Building> getAvailableBuildings() {
            return Ordering.from(Space.COMPARATOR_BY_PRESENTATION_NAME).sortedCopy(Building.getAllActiveBuildings());
        }

        public List<AcademicInterval> getAvailableIntervals() {
            return Ordering.from(AcademicInterval.COMPARATOR_BY_BEGIN_DATE).reverse()
                    .sortedCopy(AcademicInterval.readAcademicIntervals(AcademicPeriod.SEMESTER));
        }

        public List<Building> getSelectedBuildings() {
            return selectedBuildings;
        }

        public void setSelectedBuildings(List<Building> selectedBuildings) {
            this.selectedBuildings = selectedBuildings;
        }
    }

    @EntryPoint
    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ChooseBuildingBean bean = getRenderedObject();
        if (bean == null) {
            bean = new ChooseBuildingBean();
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("bean", bean);
        return mapping.findForward("choose");
    }

    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ChooseBuildingBean bean = getRenderedObject();

        List<AllocatableSpace> rooms = new ArrayList<AllocatableSpace>();
        for (Building building : bean.getSelectedBuildings()) {
            rooms.addAll(building.getAllActiveSubRoomsForEducation());
        }

        final List<RoomLessonsBean> beans = new ArrayList<RoomLessonsBean>();
        for (final AllocatableSpace room : rooms) {
            if (room.containsIdentification()) {
                final List<Lesson> lessons = room.getAssociatedLessons(bean.getAcademicInterval());
                final List<InfoLesson> infoLessons = new ArrayList<InfoLesson>(lessons.size());
                for (Lesson lesson : lessons) {
                    infoLessons.add(InfoLesson.newInfoFromDomain(lesson));
                }
                beans.add(new RoomLessonsBean(room, infoLessons));
            }
        }

        request.setAttribute("academicInterval", bean.getAcademicInterval());
        request.setAttribute("beans", beans);
        return mapping.findForward("list");
    }

    public static class RoomLessonsBean {
        private final AllocatableSpace room;
        private final List<InfoLesson> lessons;

        public RoomLessonsBean(AllocatableSpace room, List<InfoLesson> lessons) {
            super();
            this.room = room;
            this.lessons = lessons;
        }

        public AllocatableSpace getRoom() {
            return room;
        }

        public List<InfoLesson> getLessons() {
            return lessons;
        }

    }

}