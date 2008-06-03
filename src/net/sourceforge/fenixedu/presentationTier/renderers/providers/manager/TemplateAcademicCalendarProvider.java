package net.sourceforge.fenixedu.presentationTier.renderers.providers.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.manager.academicCalendarManagement.CalendarEntryBean;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarRootEntry;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class TemplateAcademicCalendarProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {				
	
	CalendarEntryBean rootEntry = (CalendarEntryBean) source;	
	Set<AcademicCalendarRootEntry> academicCalendars = RootDomainObject.getInstance().getAcademicCalendarsSet();
	List<AcademicCalendarRootEntry> result = new ArrayList<AcademicCalendarRootEntry>();
	result.addAll(academicCalendars);
	
	if(rootEntry.getEntry() != null) {
	    result.remove(rootEntry.getEntry());
	}
	
	return result;
    }

    public Converter getConverter() {	
	return new DomainObjectKeyConverter();
    }
}
