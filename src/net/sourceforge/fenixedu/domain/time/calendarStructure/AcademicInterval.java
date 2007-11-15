package net.sourceforge.fenixedu.domain.time.calendarStructure;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.chronologies.AcademicChronology;
import net.sourceforge.fenixedu.domain.time.chronologies.dateTimeFields.AcademicSemesterDateTimeFieldType;
import net.sourceforge.fenixedu.domain.time.chronologies.dateTimeFields.AcademicYearDateTimeFieldType;

import org.apache.commons.lang.StringUtils;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.joda.time.base.AbstractInterval;

public class AcademicInterval extends AbstractInterval {
           
    private Integer academicCalendarIdInternal;
    private Integer entryIdInternal;       
    private String entryClassName;    
    
    private transient AcademicCalendarEntry academicCalendarEntry;  
    private transient AcademicChronology academicChronology;
    
    
    public AcademicInterval(Integer entryIdInternal, String entryClassName, Integer academicCalendarIdInternal) {
	setEntryIdInternal(entryIdInternal);	
	setEntryClassName(entryClassName);
	setAcademicCalendarIdInternal(academicCalendarIdInternal);
    }
    
    public AcademicInterval(AcademicCalendarEntry entry) {
	setEntryIdInternal(entry.getIdInternal());	
	setEntryClassName(entry.getClass().getName());
    }
    
    public AcademicChronology getAcademicChronology() {
	return (AcademicChronology) getChronology();
    }

    @Override
    public Chronology getChronology() {
	if(academicChronology == null) {
	    AcademicCalendarRootEntry rootEntry = (AcademicCalendarRootEntry) RootDomainObject.getInstance().readAcademicCalendarEntryByOID(getAcademicCalendarIdInternal());
	    academicChronology = rootEntry.getAcademicChronology();	    
	}
	return academicChronology;
    }

    @Override
    public long getStartMillis() {
	return getAcademicCalendarEntry().getBegin().getMillis();	
    }

    @Override
    public long getEndMillis() {
	return getAcademicCalendarEntry().getEnd().getMillis();
    }
    
    public int getAcademicSemesterOfAcademicYear() {			
	return getAcademicCalendarEntry().getAcademicSemesterOfAcademicYear(getAcademicChronology());
    }                       
    
    public AcademicSemesterCE plusSemester(int amount) {
	int index = getStart().get(AcademicSemesterDateTimeFieldType.academicSemester());
	return getAcademicChronology().getAcademicSemesterIn(index + amount);
    }
    
    public AcademicSemesterCE minusSemester(int amount) {
	int index = getStart().get(AcademicSemesterDateTimeFieldType.academicSemester());
	return getAcademicChronology().getAcademicSemesterIn(index - amount);
    }
    
    public AcademicYearCE plusYear(int amount) {	
	int index = getStart().get(AcademicYearDateTimeFieldType.academicYear());
	return getAcademicChronology().getAcademicYearIn(index + amount);		
    }

    public AcademicYearCE minusYear(int amount) {
	int index = getStart().get(AcademicYearDateTimeFieldType.academicYear());
	return getAcademicChronology().getAcademicYearIn(index - amount);		
    } 
    
    public YearMonthDay getBeginYearMonthDayWithoutChronology() {
	return getAcademicCalendarEntry().getBegin().toYearMonthDay();
    }
    
    public YearMonthDay getEndYearMonthDayWithoutChronology() {
	return getAcademicCalendarEntry().getEnd().toYearMonthDay();
    }
    
    public DateTime getStartDateTimeWithoutChronology() {
	return getAcademicCalendarEntry().getBegin();
    }
    
    public DateTime getEndDateTimeWithoutChronology() {
	return getAcademicCalendarEntry().getEnd();
    }
    
    public boolean isEqual(AcademicInterval academicInterval) {
	return academicInterval != null ? getAcademicCalendarEntry().equals(academicInterval.getAcademicCalendarEntry()) 
		&& getChronology().equals(academicInterval.getChronology()): false;
    }
    
    public AcademicCalendarEntry getAcademicCalendarEntry() {
	if(academicCalendarEntry == null) {
	    academicCalendarEntry = RootDomainObject.getInstance().readAcademicCalendarEntryByOID(getEntryIdInternal());
	}	
	if(!academicCalendarEntry.getClass().getName().equals(getEntryClassName())) {
	    throw new DomainException("error.AcademicInterval.invalid.class.names");
	}	
        return academicCalendarEntry;
    }     
          
    public Integer getEntryIdInternal() {
        return entryIdInternal;
    }
    
    public void setEntryIdInternal(Integer entryIdInternal) {
        if(entryIdInternal == null) {
            throw new DomainException("error.AcademicInterval.empty.entry.idInternal");
        }
	this.entryIdInternal = entryIdInternal;
    }
    
    public String getEntryClassName() {
        return entryClassName;
    }

    public void setEntryClassName(String clazz) {
	if(clazz == null || StringUtils.isEmpty(clazz)) {
	    throw new DomainException("error.AcademicInterval.empty.entry.class");
	}
        this.entryClassName = clazz;
    }

    public Integer getAcademicCalendarIdInternal() {
        return academicCalendarIdInternal;
    }

    public void setAcademicCalendarIdInternal(Integer academicCalendarIdInternal) {
	if(academicCalendarIdInternal == null) {
	    throw new DomainException("error.AcademicInterval.empty.academic.chronology.idInternal");
	}
        this.academicCalendarIdInternal = academicCalendarIdInternal;
    }   
}
