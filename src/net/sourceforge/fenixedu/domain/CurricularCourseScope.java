package net.sourceforge.fenixedu.domain;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * @author dcs-rjao
 * 
 * 20/Mar/2003
 */

public class CurricularCourseScope extends CurricularCourseScope_Base {

	public CurricularCourseScope() {}
	
	public CurricularCourseScope(IBranch branch, ICurricularCourse curricularCourse, ICurricularSemester curricularSemester,
								 Calendar beginDate, Calendar endDate, String Annotation) {
		
        // check that there isn't another scope active with the same curricular course, branch and semester
		
        if (curricularCourse.hasActiveScopeInGivenSemesterForGivenBranch(curricularSemester.getSemester(), branch)) {
            throw new RuntimeException();
        }
		
		setBranch(branch);
		setCurricularCourse(curricularCourse);
		setCurricularSemester(curricularSemester);
		
		setBeginDate(beginDate);
		setEndDate(endDate);
		setAnotation(Annotation);
	}
	
	
    /**
     * @return Returns the beginDate.
     */
    public Calendar getBeginDate() {
        if (this.getBegin() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getBegin());
            return result;
        }
        return null;
    }

    /**
     * @param beginDate
     *            The beginDate to set.
     */
    public void setBeginDate(Calendar beginDate) {
        if (beginDate != null) {
            this.setBegin(beginDate.getTime());
        } else {
            this.setBegin(null);
        }
    }

    /**
     * @return Returns the endDate.
     */
    public Calendar getEndDate() {
        if (this.getEnd() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnd());
            return result;
        }
        return null;
    }

    /**
     * @param endDate
     *            The endDate to set.
     */
    public void setEndDate(Calendar endDate) {
        if (endDate != null) {
            this.setEnd(endDate.getTime());
        } else {
            this.setEnd(null);
        }
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

    public Boolean isActive() {
        Boolean result = Boolean.FALSE;
        if (this.getEndDate() == null) {
            result = Boolean.TRUE;
        }
        return result;
    }
	
	
	public Boolean canBeDeleted() {
		return !hasAnyAssociatedWrittenEvaluations();
	}
	
	public void delete() throws DomainException {

	    if (canBeDeleted()) {
			removeCurricularSemester();
			removeCurricularCourse();
			removeBranch();
			
			super.deleteDomainObject();
        } else {
            throw new DomainException(this.getClass().getName(), "ola mundo");
        }
	}

}
