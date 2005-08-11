package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarDateComparator;



public class CurricularCourseScope extends CurricularCourseScope_Base {

	public CurricularCourseScope() {}
	
	public CurricularCourseScope(IBranch branch, ICurricularCourse curricularCourse, ICurricularSemester curricularSemester,
								 Calendar beginDate, Calendar endDate, String Annotation){
		
        // check that there isn't another scope active with the same curricular course, branch and semester
		
        if (curricularCourse.hasActiveScopeInGivenSemesterForGivenBranch(curricularSemester.getSemester(), branch)) {
            throw new DomainException("error.curricular.course.scope.conflict.creation");
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
	
	
	public void edit(IBranch branch, ICurricularSemester curricularSemester,
			 Calendar beginDate, Calendar endDate, String Annotation) {
   	
		setBranch(branch);
		setCurricularSemester(curricularSemester);
		setBeginDate(beginDate);
		setEndDate(endDate);
		setAnotation(Annotation);
	}
	
	public void end(Calendar endDate) {
		setEndDate(endDate);
	}	
	
	public void delete() throws DomainException {

	    if (canBeDeleted()) {
			removeCurricularSemester();
			removeCurricularCourse();
			removeBranch();
			
			super.deleteDomainObject();
        } else {
            throw new DomainException("error.curricular.course.scope.has.written.evaluations");
        }
	}

    public Boolean isActive(Date date) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	CalendarDateComparator calendarDateComparator = new CalendarDateComparator();
    	Boolean result = Boolean.FALSE;
    	if(calendarDateComparator.compare(getBeginDate(), calendar) <= 0) {
    		if(getEnd() == null || calendarDateComparator.compare(getEndDate(), calendar) >= 0) {
    			result = Boolean.TRUE;
			}
    	}
    	return result;
    }
    
}
