package DataBeans;

import Dominio.ICurricularYear;

/**
 * @author dcs-rjao
 * 
 * 21/Mar/2003
 */

public class InfoCurricularYear extends InfoObject {

    private Integer year;

    public InfoCurricularYear() {
        setYear(null);
    }

    public InfoCurricularYear(Integer year) {
        setYear(year);
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoCurricularYear) {
            InfoCurricularYear curricularYear = (InfoCurricularYear) obj;
            resultado = (this.getYear().equals(curricularYear.getYear()));
        }
        return resultado;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": ";
        result += "year = " + this.year + "]";
        return result;
    }

    /**
     * @return Integer
     */
    public Integer getYear() {
        return year;
    }

    /**
     * Sets the year.
     * 
     * @param year
     *            The year to set
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    public void copyFromDomain(ICurricularYear curricularYear) {
        super.copyFromDomain(curricularYear);
        if (curricularYear != null) {
            setYear(curricularYear.getYear());
        }
    }

    public static InfoCurricularYear newInfoFromDomain(ICurricularYear curricularYear) {
        InfoCurricularYear infoCurricularYear = null;
        if (curricularYear != null) {
            infoCurricularYear = new InfoCurricularYear();
            infoCurricularYear.copyFromDomain(curricularYear);
        }
        return infoCurricularYear;
    }

}