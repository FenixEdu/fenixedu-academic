package net.sourceforge.fenixedu.domain.time.chronologies;

import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarRootEntry;
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
        
    private transient AcademicCalendarRootEntry academicCalendar;
        
    
    // Duration Fields
    private transient DurationField acAcademicYearsField;

    private transient DurationField acAcademicSemestersField;
    
    
    // DateTime Fields
    private transient DateTimeField acAcademicYear;
    
    private transient DateTimeField acAcademicSemester;    
    
    private transient DateTimeField acAcademicSemesterOfAcademicYear;

    private transient DateTimeField acDayOfAcademicSemester;          
    
    
    // Static Variables
    private static final ISOChronology ISO_INSTANCE;    
    static {
	ISO_INSTANCE = ISOChronology.getInstance();
    }
    
    // Override Methods
    
    public AcademicChronology(AcademicCalendarRootEntry academicCalendar) {
	super(ISO_INSTANCE, null);
	this.academicCalendar = academicCalendar;	
    }     
    
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
	return ISO_INSTANCE.getInstanceUTC();
    }

    @Override
    public Chronology withZone(DateTimeZone zone) {
	return ISO_INSTANCE.withZone(zone);
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

    public AcademicSemesterCE getAcademicSemesterIn(int value) {
	return (AcademicSemesterCE) academicCalendar.getEntryByValue(value, AcademicSemesterCE.class, AcademicYearCE.class);
    }
    
    public int getAcademicSemester(long instant) {	
	Integer entryValueByInstant = academicCalendar.getEntryValueByInstant(instant, AcademicSemesterCE.class, AcademicYearCE.class);
	if(entryValueByInstant != null) {
	    return entryValueByInstant;
	}
	return 0;
    }
    
    public int getAcademicYear(long instant) {
	Integer entryValueByInstant = academicCalendar.getEntryValueByInstant(instant, AcademicYearCE.class, AcademicCalendarRootEntry.class);
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
	final AcademicCalendarEntry entry = academicCalendar.getEntryByInstant(instant, AcademicYearCE.class, AcademicCalendarRootEntry.class);
	int counter = 1;
	for (final AcademicCalendarEntry subEntry : entry.getChildEntriesSet()) {
	    if (subEntry.getClass().equals(AcademicSemesterCE.class) && AcademicCalendarEntry.COMPARATOR_BEGIN_DATE.compare(subEntry, entry) < 0) {
		counter++;
	    }
	}
	return counter;
    }
    
    public int getMaximumValueForAcademicSemesterOfAcademicYear(long instant) {	
	int academicSemesterOfAcademicYear = getAcademicSemesterOfAcademicYear(instant);		
	if (academicSemesterOfAcademicYear == 0) {
	    return getMaximumValueForAcademicSemesterOfAcademicYear();
	}	
	return getMaximumValueForAcademicSemesterOfAcademicYear() - academicSemesterOfAcademicYear;   
    }
    
    public int getMaximumValueForAcademicSemesterOfAcademicYear() {
	return 2;   
    }
}
