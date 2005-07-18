package net.sourceforge.fenixedu.domain;

/**
 * @author dcs-rjao
 * 
 * 21/Mar/2003
 */

public class CurricularYear extends CurricularYear_Base {

    public CurricularYear() {
        super();
    }

    public CurricularYear(final Integer year, final int numberOfSemesters) {
        super();
        setYear(year);

        for (int i = 1; i <= numberOfSemesters; i++) {
            new CurricularSemester(this, Integer.valueOf(i));
        }
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": ";
        result += "year = " + this.getYear() + "]\n";
        return result;
    }

}
