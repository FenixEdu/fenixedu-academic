package net.sourceforge.fenixedu.domain.time.calendarStructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.chronologies.AcademicChronology;
import net.sourceforge.fenixedu.domain.time.chronologies.dateTimeFields.AcademicSemesterOfAcademicYearDateTimeFieldType;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import net.sourceforge.fenixedu.util.renderer.GanttDiagramEvent;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.DateTime;
import org.joda.time.Interval;

public abstract class AcademicCalendarEntry extends AcademicCalendarEntry_Base implements GanttDiagramEvent {

    protected abstract AcademicCalendarEntry createVirtualEntry(AcademicCalendarEntry parentEntry);      
    protected abstract boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry);
    protected abstract boolean exceededNumberOfChildEntries(AcademicCalendarEntry childEntry);
    protected abstract boolean areIntersectionsPossible(AcademicCalendarEntry entryToAdd);
    protected abstract boolean areOutOfBoundsPossible(AcademicCalendarEntry entryToAdd);
    
    protected void afterRedefineEntry() {}
    protected void beforeRedefineEntry() {}

    private transient AcademicChronology academicChronology;

    public static final Comparator<AcademicCalendarEntry> COMPARATOR_BEGIN_DATE = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BEGIN_DATE).addComparator(new BeanComparator("begin"));
	((ComparatorChain) COMPARATOR_BEGIN_DATE).addComparator(DomainObject.COMPARATOR_BY_ID);
    }

    @Checked("AcademicCalendarPredicates.checkPermissionsToManageAcademicCalendarEntry")
    protected AcademicCalendarEntry() {
	super();	
	setRootDomainObject(RootDomainObject.getInstance());	
    }

    @Checked("AcademicCalendarPredicates.checkPermissionsToManageAcademicCalendarEntry")
    public void delete(AcademicCalendarRootEntry rootEntry) {
	if(canBeDeleted(rootEntry)) {
	    getBasedEntries().clear();
	    super.setParentEntry(null);
	    super.setTemplateEntry(null);	
	    removeRootDomainObject();
	    deleteDomainObject();
	}	
	throw new DomainException("error.now.its.impossible.delete.entry.but.in.the.future.will.be.possible");
    }

    @Checked("AcademicCalendarPredicates.checkPermissionsToManageAcademicCalendarEntry")
    public AcademicCalendarEntry edit(MultiLanguageString title, MultiLanguageString description, DateTime begin, DateTime end, 
	    AcademicCalendarRootEntry rootEntry, SeasonType seasonType, AcademicCalendarEntry templateEntry) {

	if(isRoot() || rootEntry == null) {
	    throw new DomainException("error.unsupported.operation");
	}

	if(!rootEntry.equals(getRootEntry())) {	    	    	    	

	    if(getBegin().isEqual(begin) && getEnd().isEqual(end)) {
		throw new DomainException("error.AcademicCalendarEntry.unchanged.dates");	    
	    }	    	    

	    AcademicCalendarEntry newParentEntry = createVirtualPathUntil(getParentEntry(), rootEntry);	    
	    AcademicCalendarEntry newEntry = createVirtualEntry(newParentEntry);
	    newEntry.edit(title, description, begin, end, rootEntry, seasonType, templateEntry);	    
	    return newEntry;

	} else {

	    boolean isRedefinedEntry = isVirtual();
	    
	    if(isRedefinedEntry) {
		beforeRedefineEntry();
		redefineParentEntry();				
	    }
	    
	    setTitle(title);
	    setDescription(description);
	    setTimeInterval(begin, end, getParentEntry());	    

	    if(isRedefinedEntry) {		
		afterRedefineEntry();
	    }	
	    
	    return this;
	}
    }
       
    protected void initEntry(AcademicCalendarEntry parentEntry, MultiLanguageString title, MultiLanguageString description, 
	    DateTime begin, DateTime end, AcademicCalendarRootEntry rootEntry) {

	if(rootEntry == null || parentEntry == null) {
	    throw new DomainException("error.unsupported.operation");
	}

	parentEntry = !parentEntry.getRootEntry().equals(rootEntry) ? createVirtualPathUntil(parentEntry, rootEntry) : parentEntry;
	
	setParentEntry(parentEntry, false);
	setTitle(title);
	setDescription(description);
	setTimeInterval(begin, end, parentEntry);		
    }          

    protected void initVirtualEntry(AcademicCalendarEntry parentEntry, AcademicCalendarEntry templateEntry) {			
	setParentEntry(parentEntry, true);	
	setTemplateEntry(templateEntry);
    }

    private AcademicCalendarEntry createVirtualPathUntil(AcademicCalendarEntry entry, AcademicCalendarRootEntry rootEntryDestination) {	
	if(!entry.isRoot()) {

	    List<AcademicCalendarEntry> entryPath = entry.getEntryFullPath();
	    entryPath.remove(0);//remove root entry

	    AcademicCalendarEntry parentEntry = rootEntryDestination;
	    AcademicCalendarEntry virtualOrRedefinedEntry = rootEntryDestination;

	    for (AcademicCalendarEntry entryToMakeCopy : entryPath) {
		if(virtualOrRedefinedEntry != null) {
		    virtualOrRedefinedEntry = entryToMakeCopy.getVirtualOrRedefinedEntryIn((AcademicCalendarRootEntry) rootEntryDestination);					
		} 
		if(virtualOrRedefinedEntry == null) {
		    parentEntry = entryToMakeCopy.createVirtualEntry(parentEntry);		
		} else {
		    parentEntry = virtualOrRedefinedEntry;
		}
	    }	
	    return parentEntry;

	} else {
	    return rootEntryDestination;
	}	
    }

    private AcademicCalendarEntry getVirtualOrRedefinedEntryIn(AcademicCalendarRootEntry rootEntry) {
	if(rootEntry != null) {
	    List<AcademicCalendarEntry> basedEntries = getBasedEntries();
	    for (AcademicCalendarEntry entry : basedEntries) {
		if(entry.getRootEntry().equals(rootEntry)) {
		    return entry;
		}
	    }
	}
	return null;
    }

    private boolean canBeDeleted(AcademicCalendarRootEntry rootEntry) {
	if(!getRootEntry().equals(rootEntry)) {
	    throw new DomainException("error.AcademicCalendarEntry.different.rootEntry");
	}
	if(!getChildEntries().isEmpty()) {
	    throw new DomainException("error.AcademicCalendarEntry.has.childs");
	}	
	return true;
    }

    @Override
    public void setTemplateEntry(AcademicCalendarEntry templateEntry) {
	if(templateEntry != null && (!templateEntry.getClass().equals(getClass()) || getBasedEntries().contains(templateEntry))) {
	    throw new DomainException("error.AcademicCalendarEntry.invalid.template.entry");
	}	
	if(!isRoot()) {
	    AcademicCalendarRootEntry rootEntry = getRootEntry();
	    if(rootEntry.getTemplateEntry() == null || !rootEntry.getTemplateEntry().equals(templateEntry.getRootEntry())) {
		throw new DomainException("error.AcademicCalendarEntry.invalid.template.entry");
	    }
	}	   	
	super.setTemplateEntry(templateEntry);
    }
              
    protected void setParentEntry(AcademicCalendarEntry parentEntry, boolean virtualEntry) {
	if(parentEntry == null) {
	    throw new DomainException("error.AcademicCalendarEntry.empty.parentEntry");
	}
	if(isParentEntryInvalid(parentEntry)) {
	    throw new DomainException("error.AcademicCalendarEntry.invalid.parent.entry", getClass().getSimpleName(), parentEntry.getClass().getSimpleName());
	}
	if(!virtualEntry && parentEntry.exceededNumberOfChildEntries(this)) {
	    throw new DomainException("error.AcademicCalendarEntry.number.of.subEntries.exceeded");
	}
	super.setParentEntry(parentEntry);
    } 
    
    private void redefineParentEntry() {
	AcademicCalendarEntry parentEntry = getParentEntry();
	super.setParentEntry(null);
	setParentEntry(parentEntry, false);
    }   

    @Override
    public void setParentEntry(AcademicCalendarEntry parentEntry) {
        throw new DomainException("error.unsupported.operation");
    }
    
    @Override
    public void setBegin(DateTime begin) {
	throw new DomainException("error.unsupported.operation");
    }
    
    @Override
    public void setEnd(DateTime end) {
	throw new DomainException("error.unsupported.operation");
    }

    @Override
    public void setTitle(MultiLanguageString title) {
	if (title == null || title.isEmpty()) {
	    throw new DomainException("error.AcademicCalendarEntry.empty.title");
	}
	super.setTitle(title);    
    }

    @Override
    public DateTime getEnd() {
	if(isVirtual()) {
	    return getTemplateEntry().getEnd();
	}
	return super.getEnd();
    }

    @Override
    public DateTime getBegin() {
	if(isVirtual()) {
	    return getTemplateEntry().getBegin();
	}
	return super.getBegin();
    }

    @Override
    public MultiLanguageString getTitle() {
	if(isVirtual() && !isRoot()) {
	    return getTemplateEntry().getTitle();
	}
	return super.getTitle();
    }

    @Override
    public MultiLanguageString getDescription() {
	if(isVirtual() && !isRoot()) {
	    return getTemplateEntry().getDescription();
	}
	return super.getDescription();
    }
    
    protected void setTimeInterval(DateTime begin, DateTime end, AcademicCalendarEntry parentEntry) {

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
	    if(!parentEntry.areOutOfBoundsPossible(this)) {
		if(parentEntry.getBegin().isAfter(begin) || parentEntry.getEnd().isBefore(end)) {
		    throw new DomainException("error.AcademicCalendarEntry.invalid.dates");
		}
	    }
	    if(!parentEntry.areIntersectionsPossible(this)) {
		for (AcademicCalendarEntry childEntry : parentEntry.getChildEntriesWithTemplateEntries(getClass())) {
		    if(!childEntry.equals(this) && childEntry.entriesTimeIntervalIntersection(begin, end)) {
			throw new DomainException("error.AcademicCalendarEntry.dates.intersection");
		    }
		}
	    }
	}

	super.setBegin(begin);
	super.setEnd(end);
    }
    
    public AcademicCalendarEntry getOriginalTemplateEntry() {
	if(isVirtual()) {
	    return getTemplateEntry().getOriginalTemplateEntry();	    
	}
	return this;
    }
    
    public boolean isRedefined() {
	return hasTemplateEntry() && super.getBegin() != null;
    }

    public boolean isVirtual() {
	return hasTemplateEntry() && super.getBegin() == null;
    }

    public EntryState getEntryState() {
	return isVirtual() ? EntryState.VIRTUAL : isRedefined() ? EntryState.REDEFINED : EntryState.ORIGINAL;
    }

    public static enum EntryState {

	VIRTUAL, REDEFINED, ORIGINAL;

	public String getName() {
	    return name();
	}
    }

    public List<AcademicCalendarEntry> getEntryFullPath() {
	List<AcademicCalendarEntry> result = new ArrayList<AcademicCalendarEntry>();	
	result.add(this);	
	AcademicCalendarEntry parentEntry = getParentEntry();
	while(parentEntry != null) {	    
	    result.add(0, parentEntry);
	    parentEntry = parentEntry.getParentEntry();
	}	
	return result;
    }
    
    public String getPresentationTimeInterval() {
	if(!isRoot()) {
	    return getBegin().toString("dd-MM-yyyy HH:mm") + " - " + getEnd().toString("dd-MM-yyyy HH:mm");
	} else {
	    DateTime begin = getBegin();	    
	    return  begin != null ? begin.toString("dd-MM-yyyy HH:mm") + " - " + "**-**-**** **:**" : "";
	}
    }

    public MultiLanguageString getType() {
	MultiLanguageString type = new MultiLanguageString();
	String key = "label." + getClass().getSimpleName() + ".type";
	type.setContent(Language.pt, ResourceBundle.getBundle("resources/ManagerResources", new Locale("pt", "PT")).getString(key));	
	return type;
    }

    public AcademicCalendarRootEntry getAcademicCalendar() {	
	return getRootEntry();
    }

    public AcademicCalendarRootEntry getRootEntry() {
	if(isRoot()) {
	    return (AcademicCalendarRootEntry) this;
	}
	return getParentEntry().getRootEntry();
    }

    public AcademicChronology getAcademicChronology() {
	if(academicChronology == null) {
	    academicChronology = getRootEntry().getAcademicChronology();
	}
	return academicChronology;
    }

    public List<AcademicCalendarEntry> getChildEntriesWithTemplateEntries() {	
	List<AcademicCalendarEntry> result = new ArrayList<AcademicCalendarEntry>();
	getChildEntriesWithTemplateEntries(null, result, null, null, null);
	return result;
    }  

    public List<AcademicCalendarEntry> getChildEntriesWithTemplateEntries(Long instant) {	
	List<AcademicCalendarEntry> result = new ArrayList<AcademicCalendarEntry>();
	getChildEntriesWithTemplateEntries(instant, result, null, null, null);	
	return result;			
    }       
    
    public List<AcademicCalendarEntry> getChildEntriesWithTemplateEntries(Class<? extends AcademicCalendarEntry> subEntryClass) {
	if(subEntryClass == null) {
	    return Collections.emptyList();
	}		
	List<AcademicCalendarEntry> allChildEntries = new ArrayList<AcademicCalendarEntry>();
	getChildEntriesWithTemplateEntries(null, allChildEntries, null, null, subEntryClass);
	return allChildEntries;
    }   

    public List<AcademicCalendarEntry> getChildEntriesWithTemplateEntriesOrderByDate(DateTime begin, DateTime end) {	
	List<AcademicCalendarEntry> result = getChildEntriesWithTemplateEntries(begin, end, null);		
	Collections.sort(result, COMPARATOR_BEGIN_DATE);
	return result;
    } 

    public List<AcademicCalendarEntry> getChildEntriesWithTemplateEntries(DateTime begin, DateTime end, Class<? extends AcademicCalendarEntry> subEntryClass) {		
	List<AcademicCalendarEntry> result = new ArrayList<AcademicCalendarEntry>();
	getChildEntriesWithTemplateEntries(null, result, begin, end, subEntryClass);			
	return result;
    }  
    
    private List<AcademicCalendarEntry> getChildEntriesWithTemplateEntries(Long instant, DateTime begin, DateTime end, Class<? extends AcademicCalendarEntry> subEntryClass) {		
	List<AcademicCalendarEntry> result = new ArrayList<AcademicCalendarEntry>();
	getChildEntriesWithTemplateEntries(instant, result, begin, end, subEntryClass);			
	return result;
    }  

    protected List<AcademicCalendarEntry> getChildEntriesWithTemplateEntries(Long instant, List<AcademicCalendarEntry> result, 
	    DateTime begin, DateTime end, Class<? extends AcademicCalendarEntry> subEntryClass) {

	boolean hasTemplateEntry = hasTemplateEntry();	
	List<AcademicCalendarEntry> templateEntries = hasTemplateEntry ? new ArrayList<AcademicCalendarEntry>() : null;

	for (AcademicCalendarEntry subEntry : getChildEntries()) {	
	    if((subEntryClass == null || subEntry.getClass().equals(subEntryClass)) &&
		    (instant == null || subEntry.containsInstant(instant)) &&
		    (begin == null || subEntry.belongsToPeriod(begin, end))) {		

		result.add(subEntry);	    	    		
	    }
	    if(hasTemplateEntry && subEntry.hasTemplateEntry()) {		
		templateEntries.add(subEntry.getTemplateEntry());		
	    }
	}

	if(hasTemplateEntry) {	    
	    for (AcademicCalendarEntry entry : getTemplateEntry().getChildEntriesWithTemplateEntries(instant, begin, end, subEntryClass)) {
		if(!templateEntries.contains(entry)) {
		    result.add(entry);	    	    
		}
	    }	    	   	    
	}

	return result;
    }  

    public List<AcademicCalendarEntry> getChildEntriesWithTemplateEntries(Class<? extends AcademicCalendarEntry> subEntryClass, Class<? extends AcademicCalendarEntry> parentEntryClass) {
	return getChildEntriesWithTemplateEntries(null, subEntryClass, parentEntryClass);		
    }   

    public List<AcademicCalendarEntry> getChildEntriesWithTemplateEntries(Long instant, Class<? extends AcademicCalendarEntry> subEntryClass, Class<? extends AcademicCalendarEntry> parentEntryClass) {
	if(subEntryClass == null || parentEntryClass == null) {
	    return Collections.emptyList();
	}
	List<AcademicCalendarEntry> allChildEntries = new ArrayList<AcademicCalendarEntry>();
	getFirstChildEntriesWithTemplateEntries(instant, subEntryClass, parentEntryClass, allChildEntries);	
	return allChildEntries;	
    }   

    private void getFirstChildEntriesWithTemplateEntries(Long instant, Class<? extends AcademicCalendarEntry> subEntryClass,
	    Class<? extends AcademicCalendarEntry> parentEntryClass, List<AcademicCalendarEntry> childrenEntriesList) {

	if (getClass().equals(parentEntryClass)) {
	    getChildEntriesWithTemplateEntries(instant, childrenEntriesList, null, null, subEntryClass);

	} else {
	    if(!hasTemplateEntry()) {
		for (AcademicCalendarEntry subEntry : getChildEntries()) {
		    if(instant == null || subEntry.containsInstant(instant)) {
			subEntry.getFirstChildEntriesWithTemplateEntries(instant, subEntryClass, parentEntryClass, childrenEntriesList);
		    }
		} 
	    } else {
		for (AcademicCalendarEntry subEntry : getChildEntriesWithTemplateEntries(instant)) {
		    subEntry.getFirstChildEntriesWithTemplateEntries(instant, subEntryClass, parentEntryClass, childrenEntriesList);
		}
	    }
	}	
    }   
   
    public AcademicCalendarEntry getEntryForCalendar(final AcademicCalendarRootEntry academicCalendar) {

	AcademicCalendarRootEntry rootEntry = getRootEntry();
	if(rootEntry.equals(academicCalendar)) {
	    return this;
	}

	for (final AcademicCalendarEntry basedEntry : getBasedEntriesSet()) {
	    final AcademicCalendarEntry basedEntryFor = basedEntry.getEntryForCalendar(academicCalendar);
	    if (basedEntryFor != null) {
		return basedEntryFor;
	    }
	}

	for (AcademicCalendarEntry otherRoot = academicCalendar; otherRoot != null; otherRoot = otherRoot.getTemplateEntry()) {
	    if (otherRoot == rootEntry) {
		return this;
	    }
	}

	return null;
    }   

    public int getAcademicSemesterOfAcademicYear(final AcademicChronology academicChronology) {	
	return getBegin().withChronology(academicChronology).get(AcademicSemesterOfAcademicYearDateTimeFieldType.academicSemesterOfAcademicYear());	
    }

    public long getDurationMillis() {
	return getEnd().getMillis() - getBegin().getMillis();
    }

    public boolean belongsToPeriod(DateTime begin, DateTime end) {
	return !getBegin().isAfter(end) && !getEnd().isBefore(begin);
    }

    public boolean containsInstant(long instant) {
	return getBegin().getMillis() <= instant && getEnd().getMillis() >= instant;
    } 

    private boolean entriesTimeIntervalIntersection(DateTime begin, DateTime end) {
	return !getBegin().isAfter(end) && !getEnd().isBefore(begin);
    }

    public List<Interval> getGanttDiagramEventSortedIntervals() {
	List<Interval> result = new ArrayList<Interval>();
	result.add(new Interval(getBegin(), getEnd()));
	return result;
    }

    public MultiLanguageString getGanttDiagramEventName() {
	return getTitle();
    }

    public int getGanttDiagramEventOffset() {
	if(getParentEntry() == null) {
	    return 0;
	}
	return getParentEntry().getGanttDiagramEventOffset() + 1;
    }

    public String getGanttDiagramEventObservations() {
	return "-"; 
    }

    public String getGanttDiagramEventPeriod() {
	return getBegin().toString("dd/MM/yyyy HH:mm") + " - " + getEnd().toString("dd/MM/yyyy HH:mm"); 
    }

    public String getGanttDiagramEventIdentifier() {
	return getIdInternal().toString();
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

    public boolean isGradeSubmissionPeriod() {
	return false;
    }

    public boolean isRoot() {
	return false;
    }         

    public boolean isTeacherCreditsFilling() {
	return false;
    }
}
