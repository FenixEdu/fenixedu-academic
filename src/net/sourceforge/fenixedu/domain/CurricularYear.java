package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author dcs-rjao
 * 
 * 21/Mar/2003
 */

public class CurricularYear extends CurricularYear_Base implements Comparable<CurricularYear> {

    public static Comparator<CurricularYear> CURRICULAR_YEAR_COMPARATORY_BY_YEAR = new BeanComparator("year");
    
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

    public CurricularSemester getCurricularSemester(final Integer semester) {
        for (final CurricularSemester curricularSemester : getCurricularSemestersSet()) {
            if (curricularSemester.getSemester().equals(semester)) {
                return curricularSemester;
            }
        }

        return null;
    }

    public static CurricularYear readByYear(Integer year) {
        List<CurricularYear> curricularYears = RootDomainObject.getInstance().getCurricularYears();
        for (CurricularYear curricularYear : curricularYears) {
            if (curricularYear.getYear().equals(year)) {
                return curricularYear;
            }
        }
        return null;
    }

}