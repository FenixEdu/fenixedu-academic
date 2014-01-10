package net.sourceforge.fenixedu.applicationTier.Servico.research.activity;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.research.activity.EventType;
import net.sourceforge.fenixedu.domain.research.activity.ResearchEvent;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;
import net.sourceforge.fenixedu.predicates.ResultPredicates;
import pt.ist.fenixframework.Atomic;

public class CreateResearchEvent {

    @Atomic
    public static ResearchEvent run(String name, EventType eventType, ScopeType locationType, String url) {
        check(ResultPredicates.author);

        ResearchEvent event = new ResearchEvent(name, eventType, locationType);
        event.setUrl(url);

        return event;
    }
}