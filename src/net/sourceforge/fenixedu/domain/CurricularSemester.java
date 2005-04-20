package net.sourceforge.fenixedu.domain;

import java.util.List;

/**
 * @author dcs-rjao
 * 
 * 19/Mar/2003
 */

public class CurricularSemester extends CurricularSemester_Base {

    private ICurricularYear curricularYear;

    //	private List associatedCurricularCourses;
    private List scopes;

    public CurricularSemester() {
        setSemester(null);
        setIdInternal(null);
        setCurricularYearKey(null);
        setCurricularYear(null);
        setScopes(null);
    }

    public CurricularSemester(Integer semester, ICurricularYear curricularYear) {
        this();
        setSemester(semester);
        setCurricularYear(curricularYear);
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof ICurricularSemester) {
            ICurricularSemester curricularSemester = (ICurricularSemester) obj;
            resultado = (this.getSemester().equals(curricularSemester.getSemester()) && (this
                    .getCurricularYear().equals(curricularSemester.getCurricularYear())));
        }
        return resultado;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": ";
        result += "idInternal = " + this.getIdInternal() + "; ";
        result += "semester = " + this.getSemester() + "; ";
        result += "curricularYear = " + this.getCurricularYear() + "]\n";
        return result;
    }

    /**
     * @return ICurricularYear
     */
    public ICurricularYear getCurricularYear() {
        return curricularYear;
    }

    /**
     * Sets the curricularYear.
     * 
     * @param curricularYear
     *            The curricularYear to set
     */
    public void setCurricularYear(ICurricularYear curricularYear) {
        this.curricularYear = curricularYear;
    }

    /**
     * @return List
     */
    public List getScopes() {
        return scopes;
    }

    /**
     * Sets the scopes.
     * 
     * @param scopes
     *            The scopes to set
     */
    public void setScopes(List scopes) {
        this.scopes = scopes;
    }

}