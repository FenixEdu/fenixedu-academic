package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.activity.ResearchActivityStage;

public class EventStageRenderer extends ResearchActivityStageRenderer{

    @Override
    public ResearchActivityStage getResearchActivityStage() {
	Event event = (Event) getContext().getParentContext().getMetaObject().getObject();
	return event.getStage();
    }

}
