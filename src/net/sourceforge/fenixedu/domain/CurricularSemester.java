package net.sourceforge.fenixedu.domain;

/**
 * @author dcs-rjao
 * 
 * 19/Mar/2003
 */

public class CurricularSemester extends CurricularSemester_Base {

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
}