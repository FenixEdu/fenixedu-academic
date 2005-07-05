package net.sourceforge.fenixedu.domain;

/**
 * @author dcs-rjao
 * 
 * 19/Mar/2003
 */

public class CurricularSemester extends CurricularSemester_Base {

    public CurricularSemester() {
    }

    public CurricularSemester(Integer semester, ICurricularYear curricularYear) {
        this();
        this.setSemester(semester);
        this.setCurricularYear(curricularYear);
    }

    public boolean equals(Object obj) {
        if (obj instanceof ICurricularSemester) {
            final ICurricularSemester curricularSemester = (ICurricularSemester) obj;
            return getIdInternal().equals(curricularSemester.getIdInternal());
        }
        return false;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": ";
        result += "idInternal = " + this.getIdInternal() + "; ";
        result += "semester = " + this.getSemester() + "; ";
        result += "curricularYear = " + this.getCurricularYear() + "]\n";
        return result;
    }
    
}
