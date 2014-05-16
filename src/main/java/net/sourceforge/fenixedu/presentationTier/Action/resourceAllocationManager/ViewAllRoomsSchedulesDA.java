package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.space.SpaceUtils;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.RAMApplication.RAMSchedulesApp;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.fenixedu.spaces.domain.Space;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

import com.google.common.collect.Ordering;

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
        private List<Space> selectedBuildings;

        public ChooseBuildingBean() {
            this.academicInterval = AcademicInterval.readDefaultAcademicInterval(AcademicPeriod.SEMESTER);
        }

        public AcademicInterval getAcademicInterval() {
            return academicInterval;
        }

        public void setAcademicInterval(AcademicInterval academicInterval) {
            this.academicInterval = academicInterval;
        }

        public List<Space> getAvailableBuildings() {
            return Ordering.from(SpaceUtils.COMPARATOR_BY_PRESENTATION_NAME).sortedCopy(SpaceUtils.buildings());
        }

        public List<AcademicInterval> getAvailableIntervals() {
            return Ordering.from(AcademicInterval.COMPARATOR_BY_BEGIN_DATE).reverse()
                    .sortedCopy(AcademicInterval.readAcademicIntervals(AcademicPeriod.SEMESTER));
        }

        public List<Space> getSelectedBuildings() {
            return selectedBuildings;
        }

        public void setSelectedBuildings(List<Space> selectedBuildings) {
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

        List<Space> rooms = new ArrayList<Space>();
        for (Space building : bean.getSelectedBuildings()) {
            rooms.addAll(SpaceUtils.getAllActiveSubRoomsForEducation(building));
        }

        final List<RoomLessonsBean> beans = new ArrayList<RoomLessonsBean>();
        for (final Space room : rooms) {
            if (!StringUtils.isEmpty(room.getName())) {
                final List<Lesson> lessons = SpaceUtils.getAssociatedLessons(room, bean.getAcademicInterval());
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
        private final Space room;
        private final List<InfoLesson> lessons;

        public RoomLessonsBean(Space room, List<InfoLesson> lessons) {
            super();
            this.room = room;
            this.lessons = lessons;
        }

        public Space getRoom() {
            return room;
        }

        public List<InfoLesson> getLessons() {
            return lessons;
        }

    }

}