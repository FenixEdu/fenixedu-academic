package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.research.activity.ResearchActivityStage;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;

public class ScientificJournalStageRenderer extends ResearchActivityStageRenderer {

    @Override
    public ResearchActivityStage getResearchActivityStage() {
	ScientificJournal scientificJournal = (ScientificJournal) getContext().getParentContext().getMetaObject().getObject();
	return scientificJournal.getStage();
    }

}
