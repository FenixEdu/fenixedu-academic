package net.sourceforge.fenixedu.domain.time.calendarStructure;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.joda.time.base.BaseSingleFieldPeriod;

public abstract class AcademicPeriod extends BaseSingleFieldPeriod {

    public static AcademicPeriod SEMESTER = new AcademicSemesters(1, "SEMESTER");
    public static AcademicPeriod YEAR = new AcademicYears(1, "YEAR");
    public static AcademicPeriod TWO_YEAR = new AcademicYears(2, "TWO_YEAR");
    public static AcademicPeriod THREE_YEAR = new AcademicYears(3, "THREE_YEAR");
    public static AcademicPeriod FIVE_YEAR = new AcademicYears(5, "FIVE_YEAR");
    public static AcademicPeriod TRIMESTER = new AcademicTrimesters(1, "TRIMESTER");

    private static Map<String, AcademicPeriod> academicPeriods = new HashMap<String, AcademicPeriod>();

    static {
	academicPeriods.put(SEMESTER.getRepresentationInStringFormat(), SEMESTER);
	academicPeriods.put(YEAR.getRepresentationInStringFormat(), YEAR);
	academicPeriods.put(TWO_YEAR.getRepresentationInStringFormat(), TWO_YEAR);
	academicPeriods.put(THREE_YEAR.getRepresentationInStringFormat(), THREE_YEAR);
	academicPeriods.put(FIVE_YEAR.getRepresentationInStringFormat(), FIVE_YEAR);
	academicPeriods.put(TRIMESTER.getRepresentationInStringFormat(), TRIMESTER);
    }

    private String name;

    protected AcademicPeriod(final int period, final String name) {
	super(period);
	setName(name);
    }

    private void setName(final String name) {
	this.name = name;
    }

    public String getName() {
	return name;
    }

    public String getAbbreviatedName() {
	return getName() + ".ABBREVIATION";
    }

    public String getRepresentationInStringFormat() {
	return getClass().getName() + ":" + getValue();
    }

    public static AcademicPeriod getAcademicPeriodFromString(String representationInStringFormat) {
	return academicPeriods.get(representationInStringFormat);
    }

    @Override
    final protected void setValue(int value) {

    }

    public abstract float getWeight();

    @Override
    public int compareTo(Object other) {
	if (!(other instanceof AcademicPeriod)) {
	    throw new ClassCastException(getClass() + " cannot be compared to " + other.getClass());
	}

	AcademicPeriod otherAcademicPeriod = (AcademicPeriod) other;
	if (getWeight() > otherAcademicPeriod.getWeight()) {
	    return -1;
	} else if (getWeight() < otherAcademicPeriod.getWeight()) {
	    return 1;
	}

	return 0;
    }

    public static Set<AcademicPeriod> values() {
	return Collections.unmodifiableSet(new HashSet<AcademicPeriod>(academicPeriods.values()));
    }
}
