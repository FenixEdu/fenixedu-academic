package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.research.activity.ResearchActivityStage;
import net.sourceforge.fenixedu.domain.research.activity.ResearchEvent;

public class EventStageRenderer extends ResearchActivityStageRenderer {

    @Override
    public ResearchActivityStage getResearchActivityStage() {
	ResearchEvent event = (ResearchEvent) getContext().getParentContext().getMetaObject().getObject();
	return event.getStage();
    }

}
