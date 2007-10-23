package net.sourceforge.fenixedu.domain.time.calendarStructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import net.sourceforge.fenixedu.util.renderer.GanttDiagramEvent;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.iterators.EntrySetMapIterator;
import org.joda.time.DateTime;
import org.joda.time.Interval;

public abstract class AcademicCalendarEntry extends AcademicCalendarEntry_Base implements GanttDiagramEvent {

    protected abstract AcademicCalendarEntry makeAnEntryCopyInDifferentCalendar(AcademicCalendarEntry parentEntry, boolean virtual);
    protected abstract boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry);
    protected abstract boolean exceededNumberOfChildEntries(AcademicCalendarEntry childEntry);
    protected abstract boolean areIntersectionsPossible();
    protected abstract boolean areOutOfBoundsPossible();

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

	if(!getRootEntry().equals(rootEntry)) {
	    throw new DomainException("error.AcademicCalendarEntry.different.rootEntry");
	}
	
	if(!getChildEntries().isEmpty()) {
	    throw new DomainException("error.AcademicCalendarEntry.has.childs");
	}

	getBasedEntries().clear();

	super.setParentEntry(null);
	super.setTemplateEntry(null);	
	removeRootDomainObject();
	deleteDomainObject();
    }

    @Checked("AcademicCalendarPredicates.checkPermissionsToManageAcademicCalendarEntry")
    public AcademicCalendarEntry edit(MultiLanguageString title, MultiLanguageString description, DateTime begin, DateTime end, 
	    AcademicCalendarRootEntry rootEntry, SeasonType seasonType, AcademicCalendarEntry templateEntry) {

	if(isRoot() || rootEntry == null) {
	    throw new DomainException("error.unsupported.operation");
	}

	if(!rootEntry.equals(getRootEntry())) {	    	    
	    AcademicCalendarEntry newParentEntry = createVirtualPathUntil(getParentEntry(), rootEntry);	    
	    AcademicCalendarEntry newEntry = makeAnEntryCopyInDifferentCalendar(newParentEntry, false);
	    newEntry.edit(title, description, begin, end, rootEntry, seasonType, templateEntry);
	    return newEntry;
	    
	} else {
	    setTitle(title);
	    setDescription(description);
	    setTimeInterval(begin, end, getParentEntry());
	    return this;
	}
    }

    protected void initEntry(AcademicCalendarEntry parentEntry, MultiLanguageString title, MultiLanguageString description, 
	    DateTime begin, DateTime end, AcademicCalendarRootEntry rootEntry) {

	if(rootEntry == null || parentEntry == null) {
	    throw new DomainException("error.unsupported.operation");
	}

	parentEntry = !parentEntry.getRootEntry().equals(rootEntry) ? createVirtualPathUntil(parentEntry, rootEntry) : parentEntry;

	setParentEntry(parentEntry);			
	setTitle(title);
	setDescription(description);
	setTimeInterval(begin, end, parentEntry);   
    }          

    protected void initVirtualOrRedefinedEntry(AcademicCalendarEntry parentEntry, AcademicCalendarEntry templateEntry) {		
	setParentEntry(parentEntry);	
	setTemplateEntry(templateEntry);
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
		    parentEntry = entryToMakeCopy.makeAnEntryCopyInDifferentCalendar(parentEntry, true);		
		} else {
		    parentEntry = virtualOrRedefinedEntry;
		}
	    }	
	    return parentEntry;

	} else {
	    return rootEntryDestination;
	}	
    }

    @Override
    public void setTemplateEntry(AcademicCalendarEntry templateEntry) {
	if(templateEntry != null && (!templateEntry.getClass().equals(getClass()) || getBasedEntries().contains(templateEntry))) {
	    throw new DomainException("error.AcademicCalendarEntry.invalid.template.entry");
	}
	super.setTemplateEntry(templateEntry);
    }

    @Override
    public void setParentEntry(AcademicCalendarEntry parentEntry) {
	if(parentEntry == null) {
	    throw new DomainException("error.AcademicCalendarEntry.empty.parentEntry");
	}
	if(isParentEntryInvalid(parentEntry)) {
	    throw new DomainException("error.AcademicCalendarEntry.invalid.parent.entry");
	}
	if(parentEntry.exceededNumberOfChildEntries(this)) {
	    throw new DomainException("error.AcademicCalendarEntry.number.of.subEntries.exceeded");
	}
	super.setParentEntry(parentEntry);
    } 

    @Override
    public void setTitle(MultiLanguageString title) {
	if (title == null || title.isEmpty()) {
	    throw new DomainException("error.AcademicCalendarEntry.empty.title");
	}
	super.setTitle(title);    
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
	    if(!parentEntry.areOutOfBoundsPossible()) {
		if(parentEntry.getBegin().isAfter(begin) || parentEntry.getEnd().isBefore(end)) {
		    throw new DomainException("error.AcademicCalendarEntry.invalid.dates");
		}
	    }
	    if(!parentEntry.areIntersectionsPossible()) {
		for (AcademicCalendarEntry childEntry : parentEntry.getChildEntries()) {
		    if(!childEntry.equals(this) && childEntry.entriesTimeIntervalIntersection(begin, end)) {
			throw new DomainException("error.AcademicCalendarEntry.dates.intersection");
		    }
		}
	    }
	}

	super.setBegin(begin);
	super.setEnd(end);
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

    public String getPresentationTimeInterval() {
	return getBegin().toString("dd-MM-yyyy HH:mm") + " - " + getEnd().toString("dd-MM-yyyy HH:mm");
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

    public Set<? extends AcademicCalendarEntry> getAllChildEntries(AcademicCalendarEntry parentEntry,
	    Class<? extends AcademicCalendarEntry> entryClass,
	    Class<? extends AcademicCalendarEntry> parentEntryClass) {

	Set<AcademicCalendarEntry> result = new HashSet<AcademicCalendarEntry>();
	for (AcademicCalendarEntry subEntry : parentEntry.getChildEntriesSet()) {
	    if (subEntry.getClass().equals(entryClass)
		    && (parentEntryClass == null || (parentEntryClass != null && subEntry
			    .getParentEntry().getClass().equals(parentEntryClass)))) {
		result.add(subEntry);
	    }
	}
	for (AcademicCalendarEntry subEntry : parentEntry.getChildEntriesSet()) {
	    result.addAll(getAllChildEntries(subEntry, entryClass, parentEntryClass));
	}
	return result;
    }

    public Set<AcademicCalendarEntry> getChildEntriesWithTemplateEntriesOrderByDate(DateTime begin, DateTime end) {
	Set<AcademicCalendarEntry> result = new TreeSet<AcademicCalendarEntry>(COMPARATOR_BEGIN_DATE);	
	Set<AcademicCalendarEntry> templateEntriesToRemove = new HashSet<AcademicCalendarEntry>();		
	for (AcademicCalendarEntry entry : getChildEntries()) {
	    if(entry.isRedefined() || entry.isVirtual()) {		
		templateEntriesToRemove.add(entry.getTemplateEntry());		
	    }
	    if(entry.belongsToPeriod(begin, end)) {
		result.add(entry);
	    }	    
	}	
	if(hasTemplateEntry()) {	    
	    for (AcademicCalendarEntry entry : getTemplateEntry().getChildEntriesWithTemplateEntriesOrderByDate(begin, end)) {
		if(!templateEntriesToRemove.contains(entry) && entry.belongsToPeriod(begin, end)) {
		    result.add(entry);
		}
	    }	    	   	    
	}
	return result;
    }

    public Set<AcademicCalendarEntry> getChildEntriesOrderByDate(){
	Set<AcademicCalendarEntry> result = new TreeSet<AcademicCalendarEntry>(COMPARATOR_BEGIN_DATE);
	result.addAll(getChildEntries());
	return result;
    }

    public Set<AcademicCalendarEntry> getChildEntriesOrderByDate(DateTime begin, DateTime end){	
	Set<AcademicCalendarEntry> result = new TreeSet<AcademicCalendarEntry>(AcademicCalendarEntry.COMPARATOR_BEGIN_DATE);	
	for (AcademicCalendarEntry academicCalendarEntry : getChildEntriesSet()) {
	    if(academicCalendarEntry.belongsToPeriod(begin, end)) {
		result.add(academicCalendarEntry);
	    }
	}	
	return result;
    }

    public List<AcademicCalendarEntry> getChildEntries(Class<? extends AcademicCalendarEntry> entryClass){
	List<AcademicCalendarEntry> result = new ArrayList<AcademicCalendarEntry>();
	for (AcademicCalendarEntry subEntry : getChildEntriesSet()) {
	    if (subEntry.getClass().equals(entryClass)) {
		result.add(subEntry);
	    }
	}
	return result;
    }   

    public long getDurationMillis() {
	return getEnd().getMillis() - getBegin().getMillis();
    }

    public boolean belongsToPeriod(DateTime begin, DateTime end) {
	return !getBegin().isAfter(end) && !getEnd().isBefore(begin);
    }

    public boolean containsInstant(long instant) {
	return (getBegin().getMillis() <= instant && getEnd().getMillis() >= instant) ? true : false;
    } 

    private boolean entriesTimeIntervalIntersection(DateTime begin, DateTime end) {
	return (!this.getBegin().isAfter(end) && !this.getEnd().isBefore(begin));
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
}
