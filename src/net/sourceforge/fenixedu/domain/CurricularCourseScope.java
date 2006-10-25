package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarDateComparator;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.DateFormatUtil;



public class CurricularCourseScope extends CurricularCourseScope_Base {

    public static Comparator<CurricularCourseScope> CURRICULAR_COURSE_NAME_COMPARATOR = new Comparator<CurricularCourseScope>() {
        public int compare(CurricularCourseScope c1, CurricularCourseScope c2) {
	    int nameComparation = c1.getCurricularCourse().getName().compareTo(
		    c2.getCurricularCourse().getName());
            if(nameComparation != 0){
                return nameComparation;                
            }            
	    return c1.getCurricularCourse().getIdInternal().compareTo(
		    c2.getCurricularCourse().getIdInternal());
        }  
    };
    
	public CurricularCourseScope() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}
	
    public CurricularCourseScope(Branch branch, CurricularCourse curricularCourse,
	    CurricularSemester curricularSemester, Calendar beginDate, Calendar endDate,
	    String Annotation) {
		this();
	// check that there isn't another scope active with the same curricular
	// course, branch and semester
		
        if (curricularCourse.hasActiveScopeInGivenSemesterForGivenBranch(curricularSemester, branch)) {
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

    public Boolean isActive() {
        return isActive(new Date());
    }
    
    public boolean getActive (){
    	return this.isActive();
    }
	
	
	public Boolean canBeDeleted() {
		return !hasAnyAssociatedWrittenEvaluations();
	}
	
    public void edit(Branch branch, CurricularSemester curricularSemester, Calendar beginDate,
	    Calendar endDate, String Annotation) {
	
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
			
			removeRootDomainObject();
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
	if (calendarDateComparator.compare(getBeginDate(), calendar) <= 0) {
	    if (getEndDate() == null || calendarDateComparator.compare(getEndDate(), calendar) >= 0) {
		result = Boolean.TRUE;
	    }
	}
	return result;
    }

    public boolean intersects(final Date begin, final Date end) {
	return DateFormatUtil.compareDates("yyyyMMdd", getBegin(), end) < 0
		&& (getEnd() == null || DateFormatUtil.compareDates("yyyyMMdd", getEnd(), begin) > 0);
    }
    
    public boolean isActiveForExecutionPeriod(final ExecutionPeriod executionPeriod) {
        return intersects(executionPeriod.getBeginDate(), executionPeriod.getEndDate())
                && executionPeriod.getSemester().equals(getCurricularSemester().getSemester());
    }

    private DegreeModuleScopeCurricularCourseScope degreeModuleScopeCurricularCourseScope = null;
    
    private synchronized void initDegreeModuleScopeCurricularCourseScope() {
        if(degreeModuleScopeCurricularCourseScope == null) {
            degreeModuleScopeCurricularCourseScope = new DegreeModuleScopeCurricularCourseScope(this);
        }
    }
    
    public DegreeModuleScopeCurricularCourseScope getDegreeModuleScopeCurricularCourseScope() {
        if(degreeModuleScopeCurricularCourseScope == null) {
            initDegreeModuleScopeCurricularCourseScope();
        }
        return degreeModuleScopeCurricularCourseScope;
    }   
    
    public class DegreeModuleScopeCurricularCourseScope extends DegreeModuleScope {

        private final CurricularCourseScope curricularCourseScope;
        
        private DegreeModuleScopeCurricularCourseScope(CurricularCourseScope curricularCourseScope) {
            this.curricularCourseScope= curricularCourseScope;
        }
        
        @Override
        public Integer getIdInternal() {        
            return curricularCourseScope.getIdInternal();
        }

        @Override
        public Integer getCurricularSemester() {
            return curricularCourseScope.getCurricularSemester().getSemester();
        }

        @Override
        public Integer getCurricularYear() {
            return curricularCourseScope.getCurricularSemester().getCurricularYear().getYear();
        }

        @Override
        public String getBranch() {           
            return curricularCourseScope.getBranch().getName();
        }

        public CurricularCourseScope getCurricularCourseScope() {
            return curricularCourseScope;
        }

        @Override
        public boolean isActiveForExecutionPeriod(final ExecutionPeriod executionPeriod) {
            return curricularCourseScope.isActiveForExecutionPeriod(executionPeriod);
        }

        @Override
        public CurricularCourse getCurricularCourse() {            
            return curricularCourseScope.getCurricularCourse();
        }

	@Override
	public String getAnotation() {
	    return curricularCourseScope.getAnotation();
	}	
    }
}
    
