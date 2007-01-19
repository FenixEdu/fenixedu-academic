package net.sourceforge.fenixedu.domain.time.chronologies;

import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendar;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicSemesterCE;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYearCE;
import net.sourceforge.fenixedu.domain.time.chronologies.dateTimeFields.AcademicSemesterDateTimeField;
import net.sourceforge.fenixedu.domain.time.chronologies.dateTimeFields.AcademicSemesterOfAcademicYearDateTimeField;
import net.sourceforge.fenixedu.domain.time.chronologies.dateTimeFields.AcademicYearDateTimeField;
import net.sourceforge.fenixedu.domain.time.chronologies.dateTimeFields.DayOfAcademicSemesterDateTimeField;
import net.sourceforge.fenixedu.domain.time.chronologies.durationFields.AcademicSemestersDurationField;
import net.sourceforge.fenixedu.domain.time.chronologies.durationFields.AcademicYearsDurationField;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.Interval;
import org.joda.time.chrono.AssembledChronology;
import org.joda.time.chrono.ISOChronology;

public class AcademicChronology extends AssembledChronology {
      
    private transient AcademicCalendar academicCalendar;
    
    
    private transient DurationField acAcademicYearsField;

    private transient DurationField acAcademicSemestersField;

    
    private transient DateTimeField acAcademicYear;
    
    private transient DateTimeField acAcademicSemester;    
    
    private transient DateTimeField acAcademicSemesterOfAcademicYear;

    private transient DateTimeField acDayOfAcademicSemester;

    
    public AcademicChronology(AcademicCalendar academicCalendar_) {
	super(ISOChronology.getInstance(), null);
	academicCalendar = academicCalendar_;	
    }     
    
    // Overrided Methods
    
    @Override
    protected void assemble(Fields fields) {	
	acAcademicYearsField = new AcademicYearsDurationField(this);
	acAcademicYear = new AcademicYearDateTimeField(this);		
        
	acAcademicSemester = new AcademicSemesterDateTimeField(this);
	acAcademicSemestersField = new AcademicSemestersDurationField(this);
        acAcademicSemesterOfAcademicYear = new AcademicSemesterOfAcademicYearDateTimeField(this);	
        acDayOfAcademicSemester = new DayOfAcademicSemesterDateTimeField(this); 	
    }

    @Override
    public Chronology withUTC() {
	return null;
    }

    @Override
    public Chronology withZone(DateTimeZone zone) {
	return null;
    }

    @Override
    public String toString() {
	String str = "AcademicChronology";
	DateTimeZone zone = getZone();
	if (zone != null) {
	    str = str + '[' + zone.getID() + ']';
	}
	return str;
    }       
    
    // DateTime Fields
   
    public DateTimeField academicYear() {
	return acAcademicYear;
    }
    
    public DateTimeField academicSemester() {	
	return acAcademicSemester;
    }    
    
    public DateTimeField academicSemesterOfAcademicYear() {	
	return acAcademicSemesterOfAcademicYear;
    }
    
    public DateTimeField dayOfAcademicSemester() {
	return acDayOfAcademicSemester;
    }

    // Duration Fields

    public DurationField academicYears() {
	return acAcademicYearsField;
    }

    public DurationField academicSemesters() {
	return acAcademicSemestersField;
    }
    
    // Auxiliar Methods

    public int getAcademicYear(long instant) {
	Integer entryValueByInstant = academicCalendar.getEntryValueByInstant(instant, AcademicYearCE.class, null);
	if(entryValueByInstant != null) {
	    return entryValueByInstant;
	}
	return 0;
    }
    
    public int getDayOfAcademicSemester(long instant) {
	AcademicCalendarEntry entryByInstant = academicCalendar.getEntryByInstant(instant, AcademicSemesterCE.class, AcademicYearCE.class);	
	if (entryByInstant != null) {
	    DateTime instantDateTime = new DateTime(instant);
	    Interval interval = new Interval(entryByInstant.getBegin(), instantDateTime);
	    int days = interval.toPeriod().getDays();
	    if (days > 0) {
		return days;
	    }
	}	
	return 0;
    }
    
    public int getAcademicSemesterOfAcademicYear(long instant) {
	Integer entryValueByInstant = academicCalendar.getEntryValueByInstant(instant, AcademicSemesterCE.class, AcademicYearCE.class);	
	if (entryValueByInstant != null) {
	    return entryValueByInstant;
	}
	return 0;
    }
    
    public int getMaximumValueForAcademicSemesterOfAcademicYear(long instant) {
	Integer entryValueByInstant = academicCalendar.getEntryValueByInstant(instant, AcademicSemesterCE.class, AcademicYearCE.class);	
	if (entryValueByInstant == null) {
	    return getMaximumValueForAcademicSemesterOfAcademicYear();
	}	
	return getMaximumValueForAcademicSemesterOfAcademicYear() - entryValueByInstant.intValue();   
    }
    
    public int getMaximumValueForAcademicSemesterOfAcademicYear() {
	return 2;   
    }
}
