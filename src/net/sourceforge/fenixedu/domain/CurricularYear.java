package net.sourceforge.fenixedu.domain;

/**
 * @author dcs-rjao
 * 
 * 21/Mar/2003
 */

public class CurricularYear extends CurricularYear_Base {

    public CurricularYear() {
    }

    public CurricularYear(Integer year) {
        setYear(year);
    }

    public boolean equals(Object obj) {
        if (obj instanceof ICurricularYear) {
            final ICurricularYear curricularYear = (ICurricularYear) obj;
            return this.getIdInternal().equals(curricularYear.getIdInternal());
        }
        return false;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": ";
        result += "year = " + this.getYear() + "]\n";
        return result;
    }

}
