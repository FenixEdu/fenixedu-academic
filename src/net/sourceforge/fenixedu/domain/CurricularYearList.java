package net.sourceforge.fenixedu.domain;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;

/**
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class CurricularYearList {

    private final AcademicPeriod academicPeriod;

    private final List<Integer> curricularYears;

    public CurricularYearList(AcademicPeriod academicPeriod, List<Integer> curricularYears) {
	super();
	if (academicPeriod == null || curricularYears == null)
	    throw new IllegalArgumentException("exception.null.values");
	this.academicPeriod = academicPeriod;
	this.curricularYears = curricularYears;
    }

    @Override
    public String toString() {
	StringBuffer buffer = new StringBuffer();
	buffer.append(academicPeriod.getRepresentationInStringFormat() + "|");

	for (Integer year : curricularYears) {
	    buffer.append(year + ",");
	}

	return buffer.toString();
    }

    private void checkValidYear(Integer year) {
	if (year == null || year < 0 || year > (int) academicPeriod.getWeight())
	    throw new IllegalArgumentException("exception.unsupported.year");
    }

    public void addYear(Integer year) {
	checkValidYear(year);

	curricularYears.add(year);
    }

    public static CurricularYearList internalize(String data) {

	String[] fields = data.split("\\|");

	if (fields.length != 2)
	    throw new IllegalArgumentException("exception.malformed.year.list");

	AcademicPeriod period = AcademicPeriod.getAcademicPeriodFromString(fields[0]);

	String[] years = fields[1].split(",");

	List<Integer> yearList = new LinkedList<Integer>();

	for (String year : years) {
	    yearList.add(Integer.parseInt(year));
	}

	return new CurricularYearList(period, yearList);

    }

    public boolean containsYear(Integer year) {
	if (year == null || year < 0 || year > (int) academicPeriod.getWeight())
	    return false;
	return curricularYears.contains(year);
    }

    public boolean containsYears(Collection<Integer> years) {
	return curricularYears.containsAll(curricularYears);
    }

}
