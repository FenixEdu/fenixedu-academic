package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.OccupationType;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.SearchOccupationEventsBean;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.SpaceOccupationEventBean;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.space.EventSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.LessonInstanceSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.LessonSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.SpaceUtils;
import net.sourceforge.fenixedu.domain.space.WrittenEvaluationSpaceOccupation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.RAMApplication.RAMEvaluationsApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.occupation.Occupation;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.Atomic;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

@StrutsFunctionality(app = RAMEvaluationsApp.class, path = "search-occupations", titleKey = "title.search.occupations")
@Mapping(module = "resourceAllocationManager", path = "/searchOccupations", parameter = "method")
@Forwards(value = { @Forward(name = "prepareSearchEvents", path = "/resourceAllocationManager/searchSpaceOccupations.jsp") })
public class SearchOccupationsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareSearchEvents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        SearchOccupationEventsBean bean = new SearchOccupationEventsBean();
        request.setAttribute("bean", bean);
        return mapping.findForward("prepareSearchEvents");
    }

    public ActionForward searchSpaceEvents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        SearchOccupationEventsBean bean = getRenderedObject();
        if (bean.getStart().isAfter(bean.getEnd())) {
            request.setAttribute("startAfterEnd", true);
        } else {
            Collection<SpaceOccupationEventBean> results = run(bean);
            request.setAttribute("results", results);
        }
        request.setAttribute("bean", bean);
        return mapping.findForward("prepareSearchEvents");
    }

    public static Collection<SpaceOccupationEventBean> run(SearchOccupationEventsBean bean) {
        return run(bean.getBuilding(), bean.getStart().toDateTimeAtStartOfDay(), bean.getEnd().toDateTimeAtStartOfDay(),
                bean.getTypes());
    }

    private static OccupationType getType(Occupation occupation) {
        if (occupation instanceof LessonSpaceOccupation || occupation instanceof LessonInstanceSpaceOccupation) {
            return OccupationType.LESSON;
        }

        if (occupation instanceof WrittenEvaluationSpaceOccupation) {
            return OccupationType.EVALUATION;
        }

        if (occupation.getClass().equals(Occupation.class)) {
            return OccupationType.GENERIC;
        }

        return null;
    }

    private static Set<Space> getAllChildren(Space space) {
        Set<Space> children = new HashSet<>();
        for (Space child : space.getChildren()) {
            if (SpaceUtils.isRoom(child) || SpaceUtils.isRoomSubdivision(child)) {
                children.add(child);
            }
            children.addAll(getAllChildren(child));
        }
        return children;
    }

    @Atomic
    public static Collection<SpaceOccupationEventBean> run(Space building, DateTime start, DateTime end,
            List<OccupationType> types) {
        final Set<SpaceOccupationEventBean> beans = new HashSet<SpaceOccupationEventBean>();
        end = end.plusDays(1);
        final Interval searchInterval = new Interval(start, end);
        for (Space space : getAllChildren(building)) {
            final Map<Occupation, Collection<Interval>> map = getEventSpaceOccupations(space, searchInterval).asMap();
            final Set<Occupation> spaceOccupations = map.keySet();
            if (spaceOccupations.isEmpty()) {
                continue;
            }
            for (Occupation occupation : spaceOccupations) {
                OccupationType occupationType = getType(occupation);
                if (!types.contains(occupationType)) {
                    continue;
                }
                if (occupation instanceof WrittenEvaluationSpaceOccupation) {
                    WrittenEvaluationSpaceOccupation evalOccupation = (WrittenEvaluationSpaceOccupation) occupation;
                    for (WrittenEvaluation eval : evalOccupation.getWrittenEvaluations()) {
                        final Interval durationInterval = eval.getDurationInterval();
                        if (searchInterval.overlaps(durationInterval)) {
                            String desc;
                            if (eval instanceof WrittenTest) {
                                desc =
                                        String.format("(%s) %s - %s", eval.getEvaluationType(), eval.getName(),
                                                ((WrittenTest) eval).getDescription());
                            } else {
                                desc = String.format("(%s) %s", eval.getEvaluationType(), eval.getName());
                            }
                            beans.add(new SpaceOccupationEventBean(space, durationInterval, desc, occupationType));
                        }
                    }
                } else {
                    final String desc = getPresentationString(occupation);
                    for (Interval interval : map.get(occupation)) {
                        beans.add(new SpaceOccupationEventBean(space, interval, desc, occupationType));
                    }
                }
            }
        }
        return beans;
    }

    private static String getPresentationString(Occupation occupation) {
        if (occupation.getClass().equals(Occupation.class)) {
            return occupation.getSummary();
        }
        if (occupation instanceof EventSpaceOccupation) {
            return ((EventSpaceOccupation) occupation).getPresentationString();
        }

        return "";
    }

    private static Multimap<Occupation, Interval> getEventSpaceOccupations(Space space, Interval searchInterval) {
        Multimap<Occupation, Interval> result = HashMultimap.create();
        for (Occupation occupation : space.getOccupationSet()) {
            for (Interval occupationInterval : occupation.getIntervals()) {
                if (occupationInterval.overlaps(searchInterval)) {
                    result.put(occupation, occupationInterval);
                }
            }
        }
        return result;
    }
}