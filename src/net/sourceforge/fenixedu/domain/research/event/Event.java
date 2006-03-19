package net.sourceforge.fenixedu.domain.research.event;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.YearMonthDay;

public class Event extends Event_Base {

	public Event(YearMonthDay endDate, YearMonthDay startDate, String eventLocation, Boolean feeOrFree,
			EventType type, MultiLanguageString name) {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
		setEndDate(endDate);
		setStartDate(startDate);
		setEventLocation(eventLocation);
		setFeeOrFree(feeOrFree);
		setName(name);
	}

	public EventTranslation getEventTranslation(Language language) {
		if (getName().getContent(language) != null) {
			return new EventTranslation(language, getName().getContent(language));
		}
		else{
			throw new DomainException("errors.event.inexistantTranslation");
		}
	}

	public class EventTranslation {
		private Language language;
		private String name;

		public EventTranslation(Language language, String name) {
			this.language = language;
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Language getLanguage() {
			return language;
		}

		public void setLanguage(Language language) {
			this.language = language;
		}
	}
}
