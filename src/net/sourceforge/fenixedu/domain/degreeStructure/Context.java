package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.Checked;

public class Context extends Context_Base implements Comparable<Context> {

    protected Context() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        this.setChildOrder(0);
    }

    public Context(CourseGroup courseGroup, DegreeModule degreeModule,
            CurricularPeriod curricularPeriod, ExecutionPeriod beginExecutionPeriod,
            ExecutionPeriod endExecutionPeriod) {
        
        this();
        if (courseGroup == null || degreeModule == null || beginExecutionPeriod == null) {
            throw new DomainException("error.incorrectContextValues");
        }
        
        checkExecutionPeriods(beginExecutionPeriod, endExecutionPeriod);

        super.setParentCourseGroup(courseGroup);
        super.setChildDegreeModule(degreeModule);
        super.setCurricularPeriod(curricularPeriod);
        super.setBeginExecutionPeriod(beginExecutionPeriod);
        super.setEndExecutionPeriod(endExecutionPeriod);
    }
    
    public void edit(CourseGroup parentCourseGroup, DegreeModule degreeModule,
            CurricularPeriod curricularPeriod, ExecutionPeriod beginExecutionPeriod,
            ExecutionPeriod endExecutionPeriod) {
        
        edit(beginExecutionPeriod, endExecutionPeriod);
        setParentCourseGroup(parentCourseGroup);
        setChildDegreeModule(degreeModule);
        setCurricularPeriod(curricularPeriod);
    }
    
    protected void edit(ExecutionPeriod beginExecutionPeriod, ExecutionPeriod endExecutionPeriod) {
        checkExecutionPeriods(beginExecutionPeriod, endExecutionPeriod);
        setBeginExecutionPeriod(beginExecutionPeriod);
        setEndExecutionPeriod(endExecutionPeriod);
    }

    public void delete() {
        removeCurricularPeriod();
        removeChildDegreeModule();
        removeParentCourseGroup();

        // Cannot remove becuase setBeginExecutionPeriod(null) is not allowed.
        // however, since deleteDomainObject will be called, all will be ok, it will just disapear
        //removeBeginExecutionPeriod();

        removeEndExecutionPeriod();
        removeRootDomainObject();
        super.deleteDomainObject();
    }
    
    public int compareTo(Context o) {
        int orderCompare = this.getChildOrder().compareTo(o.getChildOrder());
        if (this.getParentCourseGroup().equals(o.getParentCourseGroup()) && orderCompare != 0) {
            return orderCompare;
        } else {
            if (this.getChildDegreeModule() instanceof CurricularCourse) {
                int periodsCompare = this.getCurricularPeriod().compareTo(o.getCurricularPeriod());
                if (periodsCompare != 0) {
                    return periodsCompare;     
                }
                return this.getChildDegreeModule().getName().compareTo(o.getChildDegreeModule().getName());
            } else {
                return this.getChildDegreeModule().getName().compareTo(o.getChildDegreeModule().getName());
            }
        }
    }

    @Override
    @Checked("ContextPredicates.curricularPlanMemberWritePredicate")
    public void setParentCourseGroup(CourseGroup courseGroup) {
        super.setParentCourseGroup(courseGroup);
    }

    @Override
    @Checked("ContextPredicates.curricularPlanMemberWritePredicate")
    public void setCurricularPeriod(CurricularPeriod curricularPeriod) {
        super.setCurricularPeriod(curricularPeriod);
    }

    @Override
    @Checked("ContextPredicates.curricularPlanMemberWritePredicate")
    public void setChildDegreeModule(DegreeModule degreeModule) {
        super.setChildDegreeModule(degreeModule);
    }
    
    public boolean isValid(ExecutionPeriod executionPeriod) {
    	return (getBeginExecutionPeriod().isBeforeOrEquals(executionPeriod) && (getEndExecutionPeriod() == null || getEndExecutionPeriod().isAfterOrEquals(executionPeriod)))
            && ((getChildDegreeModule() instanceof CurricularCourse && containsSemester(executionPeriod.getSemester()))
                    || !(getChildDegreeModule() instanceof CurricularCourse));
    }
    
    public boolean isValid(ExecutionYear executionYear) {
    	for (ExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
			if(isValid(executionPeriod)) {
				return true;
			}
		}
    	return false;
    }
    
    protected void checkExecutionPeriods(ExecutionPeriod beginExecutionPeriod, ExecutionPeriod endExecutionPeriod) {
	if (beginExecutionPeriod == null) {
	    throw new DomainException("curricular.rule.begin.execution.period.cannot.be.null");
	}
        if (endExecutionPeriod != null && beginExecutionPeriod.isAfter(endExecutionPeriod)) {
            throw new DomainException("curricular.rule.begin.is.after.end.execution.period");
        }
    }

    @Deprecated
    public Integer getOrder() {
        return super.getChildOrder();
    }

    @Deprecated
    public void setOrder(Integer order) {
        super.setChildOrder(order);
    }

    public boolean containsCurricularYear(final Integer curricularYear) {        
        final CurricularPeriod firstCurricularPeriod = getCurricularPeriod().getParent();
        final int firstCurricularPeriodOrder = firstCurricularPeriod.getAbsoluteOrderOfChild();                
        return curricularYear.intValue() == firstCurricularPeriodOrder;
    } 
    
    public boolean containsSemester(final Integer semester) {        
        final CurricularPeriod firstCurricularPeriod = getCurricularPeriod();
        final int firstCurricularPeriodOrder = firstCurricularPeriod.getChildOrder();                
        return semester.intValue() == firstCurricularPeriodOrder;
    } 
    
    public boolean containsSemesterAndCurricularYear(final Integer semester, final Integer curricularYear, 
            final RegimeType regimeType) {
        
        final int argumentOrder = (curricularYear - 1) * 2 + semester.intValue();        
        final CurricularPeriod firstCurricularPeriod = getCurricularPeriod();
        final int firstCurricularPeriodOrder = firstCurricularPeriod.getAbsoluteOrderOfChild();
        final int duration;
        if (regimeType == RegimeType.ANUAL) {
            duration = 2;
        } else if (regimeType == RegimeType.SEMESTRIAL) {
            duration = 1;
        } else {
            throw new IllegalArgumentException("Unknown regimeType: " + regimeType);
        }
        final int lastCurricularPeriodOrder = firstCurricularPeriodOrder + duration - 1;
        return firstCurricularPeriodOrder <= argumentOrder && argumentOrder <= lastCurricularPeriodOrder;            
    }
    
    private DegreeModuleScopeContext degreeModuleScopeContext = null;
    
    private synchronized void initDegreeModuleScopeContext() {
        if(degreeModuleScopeContext == null) {
            degreeModuleScopeContext = new DegreeModuleScopeContext(this);
        }
    }
    
    public DegreeModuleScopeContext getDegreeModuleScopeContext() {
        if(degreeModuleScopeContext == null) {
            initDegreeModuleScopeContext();
        }
        return degreeModuleScopeContext;
    }
    
    @Override
    public void setBeginExecutionPeriod(ExecutionPeriod beginExecutionPeriod) {
	if (beginExecutionPeriod == null) {
	    throw new DomainException("curricular.rule.begin.execution.period.cannot.be.null");
	}
        super.setBeginExecutionPeriod(beginExecutionPeriod);
    }
    
    public class DegreeModuleScopeContext extends DegreeModuleScope {

        private final Context context;
        
        private DegreeModuleScopeContext(Context context) {
            this.context = context;
        }
               
        @Override
        public Integer getIdInternal() {       
            return context.getIdInternal();
        }

        @Override
        public Integer getCurricularSemester() {        
            return context.getCurricularPeriod().getChildOrder();                        
        }

        @Override
        public Integer getCurricularYear() {
            return context.getCurricularYear();
            //return context.getCurricularPeriod().getParent().getAbsoluteOrderOfChild(); 
        }

        @Override
        public String getBranch() {            
            return "";
        }           
        
        public Context getContext() {
            return context;
        }

        @Override
        public boolean isActiveForExecutionPeriod(final ExecutionPeriod executionPeriod) {
            return getContext().isValid(executionPeriod);
        }

        @Override
        public CurricularCourse getCurricularCourse() {            
            return (CurricularCourse) context.getChildDegreeModule();
        }

	@Override
	public String getAnotation() {
	    return null;
	}
	
    }

    public Integer getCurricularYear() {
	return getCurricularPeriod().getParent().getAbsoluteOrderOfChild();
    }

}