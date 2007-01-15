package net.sourceforge.fenixedu.domain.time.chronologies;

import net.sourceforge.fenixedu.domain.time.dateTimeFields.AcademicSemesterOfAcademicYearDateTimeFieldType;
import net.sourceforge.fenixedu.domain.time.dateTimeFields.AcademicYearDateTimeField;
import net.sourceforge.fenixedu.domain.time.dateTimeFields.AcademicYearDateTimeFieldType;
import net.sourceforge.fenixedu.domain.time.dateTimeFields.DayOfAcademicSemesterDateTimeFieldType;
import net.sourceforge.fenixedu.domain.time.durationFields.AcademicSemestersDurationFieldType;
import net.sourceforge.fenixedu.domain.time.durationFields.AcademicYearsDurationFieldType;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.YearMonthDay;
import org.joda.time.chrono.AssembledChronology;
import org.joda.time.field.PreciseDateTimeField;
import org.joda.time.field.PreciseDurationField;

public class AcademicChronology extends AssembledChronology {
    
    private transient long academicYearMillis;

    private transient long academicSemesterMillis;

    
    private transient DurationField acAcademicYearsField;

    private transient DurationField acAcademicSemestersField;

    
    private transient DateTimeField acAcademicYear;
    
    private transient DateTimeField acAcademicSemesterOfAcademicYear;

    private transient DateTimeField acDayOfAcademicSemester;

    
    public AcademicChronology(Chronology base, YearMonthDay academiceYearBeginDate,
	    YearMonthDay academiceYearEndDate, YearMonthDay academicSemesterBeginDate,
	    YearMonthDay academicSemesterEndDate) {
	
	super(base, null);
	this.academicYearMillis = getAcademicYearMillis(academiceYearBeginDate, academiceYearEndDate);
	this.academicSemesterMillis = getAcademicSemesterMillis(academicSemesterBeginDate, academicSemesterEndDate);
    }
  
    @Override
    protected void assemble(Fields fields) {
	if(this.academicYearMillis > 0) {
	    acAcademicYearsField = new PreciseDurationField(AcademicYearsDurationFieldType.ACADEMIC_YEARS_TYPE, academicYearMillis);
	    acAcademicYear = new AcademicYearDateTimeField(AcademicYearDateTimeFieldType.ACADEMIC_YEAR_TYPE, acAcademicYearsField);
	}
	
	if(this.academicSemesterMillis > 0) {
            acAcademicSemestersField = new PreciseDurationField(AcademicSemestersDurationFieldType.ACADEMIC_SEMESTERS_TYPE, academicSemesterMillis);
            acAcademicSemesterOfAcademicYear = new PreciseDateTimeField(AcademicSemesterOfAcademicYearDateTimeFieldType.ACADEMIC_SEMESTER_OF_ACADEMIC_YEAR, acAcademicSemestersField, acAcademicYearsField);	
            acDayOfAcademicSemester = new PreciseDateTimeField(DayOfAcademicSemesterDateTimeFieldType.DAY_OF_ACADEMIC_SEMESTER_TYPE, getBase().days(), acAcademicSemestersField);
	}	
    }

    // DateTime Fields
   
    public DateTimeField academicYear() {
	return acAcademicYear;
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
    
    // -------

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
    
    private long getAcademicYearMillis(YearMonthDay academiceYearsBeginDate,
	    YearMonthDay academiceYearsEndDate) {
	return (academiceYearsBeginDate != null && academiceYearsEndDate != null) ? academiceYearsEndDate
		.toDateTimeAtMidnight().getMillis()
		- academiceYearsBeginDate.toDateTimeAtMidnight().getMillis()
		: 0;
    }

    private long getAcademicSemesterMillis(YearMonthDay academicSemesterBeginDate,
	    YearMonthDay academicSemesterEndDate) {
	return (academicSemesterBeginDate != null && academicSemesterEndDate != null) ? academicSemesterEndDate
		.toDateTimeAtMidnight().getMillis()
		- academicSemesterBeginDate.toDateTimeAtMidnight().getMillis()
		: 0;
    }   
}
