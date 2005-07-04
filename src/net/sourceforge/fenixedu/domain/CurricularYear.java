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
        boolean resultado = false;
        if (obj instanceof ICurricularYear) {
            ICurricularYear curricularYear = (ICurricularYear) obj;
            resultado = (this.getYear().equals(curricularYear.getYear()));
        }
        return resultado;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": ";
        result += "year = " + this.getYear() + "]\n";
        return result;
    }

}