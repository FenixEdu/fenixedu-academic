/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.resourceAllocationManager;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.WrittenEvaluation;
import org.fenixedu.academic.domain.WrittenTest;
import org.fenixedu.academic.domain.space.EventSpaceOccupation;
import org.fenixedu.academic.domain.space.LessonInstanceSpaceOccupation;
import org.fenixedu.academic.domain.space.LessonSpaceOccupation;
import org.fenixedu.academic.domain.space.SpaceUtils;
import org.fenixedu.academic.domain.space.WrittenEvaluationSpaceOccupation;
import org.fenixedu.academic.dto.resourceAllocationManager.OccupationType;
import org.fenixedu.academic.dto.resourceAllocationManager.SearchOccupationEventsBean;
import org.fenixedu.academic.dto.resourceAllocationManager.SpaceOccupationEventBean;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.RAMApplication.RAMEvaluationsApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.occupation.Occupation;
import org.joda.time.DateTime;
import org.joda.time.Interval;

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
                    for (WrittenEvaluation eval : evalOccupation.getWrittenEvaluationsSet()) {
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
            return occupation.getSubject();
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