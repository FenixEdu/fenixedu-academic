package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.researchActivity;

import java.text.Collator;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.research.activity.Event;

import org.apache.commons.beanutils.BeanComparator;

public class EditEventDA extends EditResearchActivityDA {
    
    @Override
    protected List getObjects() {
	List<Event> events = rootDomainObject.getEvents();
	Collections.sort(events, new BeanComparator("name", Collator.getInstance()));
        return events;
    }

}
