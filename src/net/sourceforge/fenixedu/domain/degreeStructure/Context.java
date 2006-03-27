package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Context extends Context_Base implements Comparable<Context> {

    protected Context() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        this.setOrder(0);
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
        removeBeginExecutionPeriod();
        removeEndExecutionPeriod();
        super.deleteDomainObject();
    }
    
    public int compareTo(Context o) {
        int orderCompare = this.getOrder().compareTo(o.getOrder());
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
    	return (getBeginExecutionPeriod().isBeforeOrEquals(executionPeriod) && (getEndExecutionPeriod() == null || getEndExecutionPeriod().isAfterOrEquals(executionPeriod)));
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
        if (endExecutionPeriod != null && beginExecutionPeriod.isAfter(endExecutionPeriod)) {
            throw new DomainException("curricular.rule.begin.is.after.end.execution.period");
        }
    }
}
