package net.sourceforge.fenixedu.domain;

import java.util.Calendar;

/**
 * @author dcs-rjao
 * 
 * 20/Mar/2003
 */

public class CurricularCourseScope extends CurricularCourseScope_Base {

    /**
     * @return Returns the beginDate.
     */
    public Calendar getBeginDate() {
        Calendar result = Calendar.getInstance();
        result.setTime(this.getBegin());
        return result;
    }

    /**
     * @param beginDate
     *            The beginDate to set.
     */
    public void setBeginDate(Calendar beginDate) {
        this.setBegin(beginDate.getTime());
    }

    /**
     * @return Returns the endDate.
     */
    public Calendar getEndDate() {
        Calendar result = Calendar.getInstance();
        result.setTime(this.getEnd());
        return result;
    }

    /**
     * @param endDate
     *            The endDate to set.
     */
    public void setEndDate(Calendar endDate) {
        this.setEnd(endDate.getTime());
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": ";
        result += "idInternal = " + this.getIdInternal() + "; ";
        result += "curricularCourse = " + this.getCurricularCourse() + "; ";
        result += "curricularSemester = " + this.getCurricularSemester() + "; ";
        result += "branch = " + this.getBranch() + "; ";
        result += "endDate = " + this.getEndDate() + "]\n";

        return result;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;

        if (obj instanceof ICurricularCourseScope) {
            ICurricularCourseScope ccs = (ICurricularCourseScope) obj;

            resultado = (((getBranch() == null && ccs.getBranch() == null) || (getBranch() != null
                    && ccs.getBranch() != null && getBranch().equals(ccs.getBranch())))
                    && ((getCurricularCourse() == null && ccs.getCurricularCourse() == null) || (getCurricularCourse() != null
                            && ccs.getCurricularCourse() != null && getCurricularCourse().equals(
                            ccs.getCurricularCourse())))
                    && ((getCurricularSemester() == null && ccs.getCurricularSemester() == null) || (getCurricularSemester() != null
                            && ccs.getCurricularSemester() != null && getCurricularSemester().equals(
                            ccs.getCurricularSemester()))) && ((getEndDate() == null && ccs.getEndDate() == null) || (getEndDate() != null
                    && ccs.getEndDate() != null && getEndDate().equals(ccs.getEndDate()))));
        }

        return resultado;
    }

    public Boolean isActive() {
        Boolean result = Boolean.FALSE;
        if (this.getEndDate() == null) {
            result = Boolean.TRUE;
        }
        return result;
    }

}
