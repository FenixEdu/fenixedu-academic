package net.sourceforge.fenixedu.domain.time.calendarStructure;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import net.sourceforge.fenixedu.util.renderer.GanttDiagramEvent;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.DateTime;
import org.joda.time.Interval;

public abstract class AcademicCalendarEntry extends AcademicCalendarEntry_Base implements GanttDiagramEvent{

    public static final Comparator COMPARATOR_BEGIN_DATE = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BEGIN_DATE).addComparator(new BeanComparator("begin"));
	((ComparatorChain) COMPARATOR_BEGIN_DATE).addComparator(new BeanComparator("idInternal"));
    }
    
    public abstract boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry);
    
    public abstract boolean exceededNumberOfSubEntries(AcademicCalendarEntry childEntry);

    @Checked("AcademicCalendarPredicates.checkPermissionsToManageAcademicCalendarEntry")
    protected AcademicCalendarEntry() {
	super();	
	setRootDomainObject(RootDomainObject.getInstance());
	setOjbConcreteClass(getClass().getName());	
    }
    
    @Checked("AcademicCalendarPredicates.checkPermissionsToManageAcademicCalendarEntry")
    public void edit(MultiLanguageString title, MultiLanguageString description, DateTime begin, DateTime end) {
	setTitle(title);
	setDescription(description);
	setTimeInterval(begin, end, getParentEntry());
    }
    
    @Checked("AcademicCalendarPredicates.checkPermissionsToManageAcademicCalendarEntry")
    public void delete() {
	if(!getChildEntries().isEmpty()) {
	    throw new DomainException("error.academicCalendarEntry.has.childs");
	}
	removeAcademicCalendar();
	removeParentEntry();
	removeRootDomainObject();
	deleteDomainObject();
    }
    
    protected void init(AcademicCalendar academicCalendar, AcademicCalendarEntry parentEntry,
	    MultiLanguageString title, MultiLanguageString description, DateTime begin,
	    DateTime end) {
	
	if(parentEntry == null && academicCalendar == null) {
	    throw new DomainException("error.AcademicCalendarEntry.empty.parentEntry.and.academicCalendar");
	}
	setParentEntry(parentEntry);	
	setAcademicCalendar(academicCalendar);
	setTitle(title);
	setDescription(description);
	setTimeInterval(begin, end, parentEntry);	
    }          
   
    @Override
    public void setParentEntry(AcademicCalendarEntry parentEntry) {
	if(parentEntry != null && isParentEntryInvalid(parentEntry)) {
	    throw new DomainException("error.academicCalendarEntry.invalid.parent.entry");
	}
	if(parentEntry != null && parentEntry.exceededNumberOfSubEntries(this)) {
	    throw new DomainException("error.academicCalendarEntry.number.of.subEntries.exceeded");
	}
	super.setParentEntry(parentEntry);
    } 
    
    @Override
    public void setBegin(DateTime begin) {
	setTimeInterval(begin, getEnd(), getParentEntry());
    }

    @Override
    public void setEnd(DateTime end) {
	setTimeInterval(getBegin(), end, getParentEntry());
    }
  
    @Override
    public void setTitle(MultiLanguageString title) {
	if (title.isEmpty()) {
	    throw new DomainException("error.AcademicCalendarEntry.empty.title");
	}
	super.setTitle(title);    
    }
    
    public String getPresentationTimeInterval() {
	return getBegin().toString("dd-MM-yyyy HH:mm") + " - " + getEnd().toString("dd-MM-yyyy HH:mm");
    }
    
    public MultiLanguageString getType() {
	MultiLanguageString type = new MultiLanguageString();
	String key = "label." + getClass().getSimpleName() + ".type";
	type.setContent(Language.pt, ResourceBundle.getBundle("resources/ManagerResources", new Locale("pt", "PT")).getString(key));	
	return type;
    }
           
    @Override
    public AcademicCalendar getAcademicCalendar() {
	if(super.getAcademicCalendar() != null) {
	    return super.getAcademicCalendar();
	}
	if(getParentEntry() == null) {
	    return null;
	}
	return getParentEntry().getAcademicCalendar();
    }

    public Set<? extends AcademicCalendarEntry> getAllSubEntries(AcademicCalendarEntry parentEntry,
	    Class<? extends AcademicCalendarEntry> entryClass,
	    Class<? extends AcademicCalendarEntry> parentEntryClass) {

	Set<AcademicCalendarEntry> result = new HashSet();
	for (AcademicCalendarEntry subEntry : parentEntry.getChildEntriesSet()) {
	    if (subEntry.getClass().equals(entryClass)
		    && (parentEntryClass == null || (parentEntryClass != null && subEntry
			    .getParentEntry().getClass().equals(parentEntryClass)))) {
		result.add(subEntry);
	    }
	}
	for (AcademicCalendarEntry subEntry : parentEntry.getChildEntriesSet()) {
	    result.addAll(getAllSubEntries(subEntry, entryClass, parentEntryClass));
	}
	return result;
    }
    
    public List<AcademicCalendarEntry> getSubEntries(Class<? extends AcademicCalendarEntry> entryClass){
	List<AcademicCalendarEntry> result = new ArrayList<AcademicCalendarEntry>();
	for (AcademicCalendarEntry subEntry : getChildEntriesSet()) {
	    if (subEntry.getClass().equals(entryClass)) {
		result.add(subEntry);
	    }
	}
	return result;
    }
        
    public List<AcademicCalendarEntry> getSubEntriesOrderByDate(){
	Set<AcademicCalendarEntry> result = new TreeSet<AcademicCalendarEntry>(COMPARATOR_BEGIN_DATE);
	result.addAll(getChildEntries());
	return new ArrayList<AcademicCalendarEntry>(result);
    }
    
    public long getDurationMillis() {
	return getEnd().getMillis() - getBegin().getMillis();
    }

    public boolean containsInstant(long instant) {
	return (getBegin().getMillis() <= instant && getEnd().getMillis() >= instant) ? true : false;
    }
    
    public boolean isAcademicYear() {
	return false;
    }
    
    public boolean isAcademicSemester() {
	return false;
    }
    
    public boolean isAcademicTrimester() {
	return false;
    }
    
    public boolean isLessonsPerid() {
	return false;
    }
    
    public boolean isExamsPeriod() {
	return false;
    }
    
    public boolean isEnrolmentsPeriod() {
	return false;
    }
    
    private void setTimeInterval(DateTime begin, DateTime end, AcademicCalendarEntry parentEntry) {
	
	if (begin == null) {
	    throw new DomainException("error.AcademicCalendarEntry.empty.begin.dateTime");
	}
	if (end == null) {
	    throw new DomainException("error.AcademicCalendarEntry.empty.end.dateTime");
	}
	if (!end.isAfter(begin)) {
	    throw new DomainException("error.begin.after.end");
	}	
	
	if(parentEntry != null) {	    
	    if(parentEntry.getBegin().isAfter(begin) || parentEntry.getEnd().isBefore(end)) {
		throw new DomainException("error.AcademicCalendarEntry.invalid.dates");
	    }
	    for (AcademicCalendarEntry childEntry : parentEntry.getChildEntries()) {
		if(!childEntry.equals(this) && childEntry.entriesTimeIntervalIntersection(begin, end)) {
		    throw new DomainException("error.AcademicCalendarEntry.dates.intersection");
		}
	    }
	}
	
	super.setBegin(begin);
	super.setEnd(end);
    }
    
    private boolean entriesTimeIntervalIntersection(DateTime begin, DateTime end) {
	return (!this.getBegin().isAfter(end) && !this.getEnd().isBefore(begin));
    }
    
    public List<Interval> getEventSortedIntervalsForGanttDiagram() {
	List<Interval> result = new ArrayList<Interval>();
	result.add(new Interval(getBegin(), getEnd()));
	return result;
    }
    
    public MultiLanguageString getEventNameForGanttDiagram() {
	return new MultiLanguageString(getType() + ": " + getTitle().getContent());
    }
    
    public int getEventOffsetForGanttDiagram() {
	if(getParentEntry() == null) {
	    return 0;
	}
	return getParentEntry().getEventOffsetForGanttDiagram() + 1;
    }
    
    public String getEventObservationsForGanttDiagram() {
	return "-"; 
    }
    
    public String getEventPeriodForGanttDiagram() {
	return getBegin().toString("dd/MM/yyyy HH:mm") + " - " + getEnd().toString("dd/MM/yyyy HH:mm"); 
    }
    
    public String getEventIdentifierForGanttDiagram() {
	return getIdInternal().toString();
    }
}
