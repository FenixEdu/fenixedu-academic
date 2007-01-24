package net.sourceforge.fenixedu.domain.time.calendarStructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.chronologies.AcademicChronology;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class AcademicCalendar extends AcademicCalendar_Base {

    private transient AcademicChronology academicChronology;

    @Checked("AcademicCalendarPredicates.checkPermissionsToManageAcademicCalendar")
    public AcademicCalendar(MultiLanguageString title, MultiLanguageString description) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setTitle(title);
	setDescription(description);
    }

    @Checked("AcademicCalendarPredicates.checkPermissionsToManageAcademicCalendar")
    public void edit(MultiLanguageString title, MultiLanguageString description) {
	setTitle(title);
	setDescription(description);
    }
             
    @Checked("AcademicCalendarPredicates.checkPermissionsToManageAcademicCalendar")
    public void delete() {
	if(!getEntries().isEmpty()) {
	    throw new DomainException("error.academicCalendar.has.entries");
	}
	removeRootDomainObject();	
	deleteDomainObject();
    }  

    @Override
    public void setTitle(MultiLanguageString title) {
	if (title.isEmpty()) {
	    throw new DomainException("error.AcademicCalendar.empty.title");
	}
	super.setTitle(title);
    }
    
    public AcademicChronology getAcademicChronology() {
	if (academicChronology == null) {
	    academicChronology = new AcademicChronology(this);
	}
	return academicChronology;
    }
    
    public MultiLanguageString getType() {
	MultiLanguageString type = new MultiLanguageString();
	String key = "label." + getClass().getSimpleName() + ".type";
	type.setContent(Language.pt, ResourceBundle.getBundle("resources/ManagerResources", new Locale("pt", "PT")).getString(key));	
	return type;
    }

    public List<AcademicCalendarEntry> getEntriesOrderByDate(){
	Set<AcademicCalendarEntry> result = new TreeSet(AcademicCalendarEntry.COMPARATOR_BEGIN_DATE);
	result.addAll(getEntries());
	return new ArrayList<AcademicCalendarEntry>(result);
    }
    
    public List<? extends AcademicCalendarEntry> getAllEntriesOrderByDate(
	    Class<? extends AcademicCalendarEntry> entryClass,
	    Class<? extends AcademicCalendarEntry> parentEntryClass) {

	Set<AcademicCalendarEntry> result = new TreeSet(AcademicCalendarEntry.COMPARATOR_BEGIN_DATE);
	for (AcademicCalendarEntry entry : getEntriesSet()) {
	    if (entry.getClass().equals(entryClass)
		    && (parentEntryClass == null || (parentEntryClass != null && entry.getParentEntry()
			    .getClass().equals(parentEntryClass)))) {
		result.add(entry);
	    }
	    result.addAll(entry.getAllSubEntries(entry, entryClass, parentEntryClass));
	}
	return new ArrayList(result);
    }
    
    public AcademicCalendarEntry getEntryByInstant(long instant,
	    Class<? extends AcademicCalendarEntry> entryClass,
	    Class<? extends AcademicCalendarEntry> parentEntryClass) {
	
	List<AcademicCalendarEntry> allEntriesByType = (List<AcademicCalendarEntry>) getAllEntriesOrderByDate(
		entryClass, parentEntryClass);
	for (AcademicCalendarEntry entry : allEntriesByType) {
	    if (entry.containsInstant(instant)) {
		return entry;
	    }
	}
	return null;
    }

    public Integer getEntryValueByInstant(long instant, Class<? extends AcademicCalendarEntry> entryClass,
	    Class<? extends AcademicCalendarEntry> parentEntryClass) {
	
	Integer counter = 0;
	List<AcademicCalendarEntry> allEntriesByType = (List<AcademicCalendarEntry>) getAllEntriesOrderByDate(
		entryClass, parentEntryClass);
	for (AcademicCalendarEntry entry : allEntriesByType) {
	    counter++;
	    if (entry.containsInstant(instant)) {
		return counter;
	    }
	}
	return null;
    }   
}
