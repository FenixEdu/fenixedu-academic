package net.sourceforge.fenixedu.domain;

/**
 * @author dcs-rjao
 * 
 * 21/Mar/2003
 */

public class CurricularYear extends CurricularYear_Base implements Comparable<CurricularYear> {

    public CurricularYear() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public CurricularYear(final Integer year, final int numberOfSemesters) {
        this();
        setYear(year);

        for (int i = 1; i <= numberOfSemesters; i++) {
            new CurricularSemester(this, Integer.valueOf(i));
        }
    }

	public int compareTo(final CurricularYear curricularYear) {
		return getYear().compareTo(curricularYear.getYear());
	}

}
