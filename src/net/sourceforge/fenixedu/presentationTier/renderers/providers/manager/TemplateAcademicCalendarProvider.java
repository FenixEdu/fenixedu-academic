package net.sourceforge.fenixedu.presentationTier.renderers.providers.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarRootEntry;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class TemplateAcademicCalendarProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {				
	AcademicCalendarRootEntry rootEntry = (AcademicCalendarRootEntry) source;	
	Set<AcademicCalendarRootEntry> academicCalendars = RootDomainObject.getInstance().getAcademicCalendarsSet();
	List<AcademicCalendarRootEntry> result = new ArrayList<AcademicCalendarRootEntry>();
	result.addAll(academicCalendars);
	result.remove(rootEntry);
	return result;
    }

    public Converter getConverter() {	
	return new DomainObjectKeyConverter();
    }
}
