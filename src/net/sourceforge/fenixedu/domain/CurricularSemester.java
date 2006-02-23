package net.sourceforge.fenixedu.domain;

/**
 * @author dcs-rjao
 * 
 * 19/Mar/2003
 */

public class CurricularSemester extends CurricularSemester_Base implements Comparable<CurricularSemester> {

    public CurricularSemester() {
        super();
    }

    public CurricularSemester(final CurricularYear curricularYear, final Integer semester) {
        super();
        setCurricularYear(curricularYear);
        setSemester(semester);
    }    

    public String toString() {
        String result = "[" + this.getClass().getName() + ": ";
        result += "idInternal = " + this.getIdInternal() + "; ";
        result += "semester = " + this.getSemester() + "; ";
        result += "curricularYear = " + this.getCurricularYear() + "]\n";
        return result;
    }

	public int compareTo(final CurricularSemester curricularSemester) {
		return getCurricularYear() == curricularSemester.getCurricularYear() ?
				getSemester().compareTo(curricularSemester.getSemester()) :
				getCurricularYear().compareTo(curricularSemester.getCurricularYear());
	}
    
}
