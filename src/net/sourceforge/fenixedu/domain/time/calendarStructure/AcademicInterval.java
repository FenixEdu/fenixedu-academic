package net.sourceforge.fenixedu.domain.time.calendarStructure;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.chronologies.AcademicChronology;
import net.sourceforge.fenixedu.domain.time.chronologies.dateTimeFields.AcademicSemesterDateTimeFieldType;

import org.apache.commons.lang.StringUtils;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.joda.time.base.AbstractInterval;

public class AcademicInterval extends AbstractInterval {
    
    private transient AcademicCalendarEntry academicCalendarEntry;  
    private Integer entryIdInternal;       
    private String entryClassName;
    
    
    public AcademicInterval(Integer entryIdInternal, String entryClassName) {
	setEntryIdInternal(entryIdInternal);	
	setEntryClassName(entryClassName);	
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
	return getAcademicCalendarEntry().getAcademicCalendar().getAcademicChronology();
    }

    @Override
    public long getStartMillis() {
	return getStartDateTime().getMillis();	
    }

    @Override
    public long getEndMillis() {
	return getEndDateTime().getMillis();
    }
    
    public YearMonthDay getBeginYearMonthDay() {
	return getStartDateTime().toYearMonthDay();
    }
    
    public YearMonthDay getEndYearMonthDay() {
	return getEndDateTime().toYearMonthDay();
    }

    public DateTime getStartDateTime() {
	return getAcademicCalendarEntry().getBegin();
    }

    public DateTime getEndDateTime() {
	return getAcademicCalendarEntry().getEnd();
    }

    public int getAcademicSemesterOfAcademicYear() {
	final AcademicCalendarEntry academicCalendarEntry = getAcademicCalendarEntry();
	final AcademicChronology academicChronology = getAcademicChronology();
	return academicCalendarEntry.getAcademicSemesterOfAcademicYear(academicChronology);
    }
    
    public AcademicInterval minusSemester(int amount) {	
	int index = getStart().get(AcademicSemesterDateTimeFieldType.academicSemester());
	AcademicSemesterCE academicSemester = getAcademicChronology().getAcademicSemesterIn(index - amount);
	return academicSemester != null ? new AcademicInterval(academicSemester) : null;
    }
    
    public AcademicInterval plusSemester(int amount) {
	int index = getStart().get(AcademicSemesterDateTimeFieldType.academicSemester());
	AcademicSemesterCE academicSemester = getAcademicChronology().getAcademicSemesterIn(index + amount);
	return academicSemester != null ? new AcademicInterval(academicSemester) : null;
    }
        
    public boolean isEqual(AcademicInterval academicInterval) {
	return academicInterval != null ? getAcademicCalendarEntry().equals(academicInterval) : false;
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
            throw new DomainException("error.AcademicInterval.empty.idInternal");
        }
	this.entryIdInternal = entryIdInternal;
    }
    
    public String getEntryClassName() {
        return entryClassName;
    }

    public void setEntryClassName(String clazz) {
	if(clazz == null || StringUtils.isEmpty(clazz)) {
	    throw new DomainException("error.AcademicInterval.empty.idInternal");
	}
        this.entryClassName = clazz;
    }   
}
