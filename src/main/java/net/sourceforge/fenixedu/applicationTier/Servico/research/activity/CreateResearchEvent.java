package net.sourceforge.fenixedu.applicationTier.Servico.research.activity;


import net.sourceforge.fenixedu.domain.research.activity.EventType;
import net.sourceforge.fenixedu.domain.research.activity.ResearchEvent;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreateResearchEvent {

    @Checked("ResultPredicates.author")
    @Service
    public static ResearchEvent run(String name, EventType eventType, ScopeType locationType, String url) {

        ResearchEvent event = new ResearchEvent(name, eventType, locationType);
        event.setUrl(url);

        return event;
    }
}