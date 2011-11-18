package net.sourceforge.fenixedu.applicationTier.Servico.space;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.spaceManager.OccupationType;
import net.sourceforge.fenixedu.dataTransferObject.spaceManager.SearchSpaceEventsBean;
import net.sourceforge.fenixedu.dataTransferObject.spaceManager.SpaceOccupationEventBean;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.Building;
import net.sourceforge.fenixedu.domain.space.EventSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.WrittenEvaluationSpaceOccupation;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import pt.ist.fenixWebFramework.services.Service;

public class SearchSpaceEvents extends FenixService {

    public static Collection<SpaceOccupationEventBean> run(SearchSpaceEventsBean bean) {
	return run(bean.getBuilding(), new DateTime(bean.getStart()), new DateTime(bean.getEnd()), bean.getTypes());
    }

    private static OccupationType getType(EventSpaceOccupation occupation) {
	if (occupation.isLessonSpaceOccupation() || occupation.isLessonInstanceSpaceOccupation()) {
	    return OccupationType.LESSON;
	}
	if (occupation.isGenericEventSpaceOccupation()) {
	    return OccupationType.GENERIC;
	}
	if (occupation.isWrittenEvaluationSpaceOccupation()) {
	    return OccupationType.EVALUATION;
	}
	return null;
    }

    @Service
    public static Collection<SpaceOccupationEventBean> run(Building building, DateTime start, DateTime end,
	    List<OccupationType> types) {
	final Set<SpaceOccupationEventBean> beans = new TreeSet<SpaceOccupationEventBean>(SpaceOccupationEventBean.COMPARATOR);
	end = end.plusDays(1);
	final Interval searchInterval = new Interval(start, end);
	for (AllocatableSpace space : building.getAllActiveContainedAllocatableSpaces()) {
	    final Map<EventSpaceOccupation, List<Interval>> map = space.getEventSpaceOccupations(start, end);
	    final Set<EventSpaceOccupation> spaceOccupations = map.keySet();
	    if (spaceOccupations.isEmpty()) {
		continue;
	    }
	    for (EventSpaceOccupation occupation : spaceOccupations) {
		OccupationType occupationType = getType(occupation);
		if (!types.contains(occupationType)) {
		    continue;
		}
		if (occupation.isWrittenEvaluationSpaceOccupation()) {
		    WrittenEvaluationSpaceOccupation evalOccupation = (WrittenEvaluationSpaceOccupation) occupation;
		    for (WrittenEvaluation eval : evalOccupation.getWrittenEvaluations()) {
			final Interval durationInterval = eval.getDurationInterval();
			if (searchInterval.overlaps(durationInterval)) {
			    String desc;
			    if (eval instanceof WrittenTest) {
				desc = String.format("(%s) %s - %s", eval.getEvaluationType(), eval.getName(),
					((WrittenTest) eval).getDescription());
			    } else {
				desc = String.format("(%s) %s", eval.getEvaluationType(), eval.getName());
			    }
			    beans.add(new SpaceOccupationEventBean(space, durationInterval, desc, occupationType));
			}
		    }
		} else {
		    final String desc = occupation.getPresentationString();
		    for (Interval interval : map.get(occupation)) {
			beans.add(new SpaceOccupationEventBean(space, interval, desc, occupationType));
		    }
		}
	    }
	}
	return beans;
    }
}
