package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Context extends Context_Base implements Comparable<Context> {

    protected Context() {
        super();
        this.setOrder(0);
    }

    public Context(CourseGroup courseGroup, DegreeModule degreeModule,
            CurricularPeriod curricularPeriod, ExecutionPeriod beginExecutionPeriod,
            ExecutionPeriod endExecutionPeriod) {
        this();
        if (courseGroup == null || degreeModule == null || beginExecutionPeriod == null) {
            throw new DomainException("error.incorrectContextValues");
        }
        super.setParentCourseGroup(courseGroup);
        super.setDegreeModule(degreeModule);
        super.setCurricularPeriod(curricularPeriod);
        super.setBeginExecutionPeriod(beginExecutionPeriod);
        super.setEndExecutionPeriod(endExecutionPeriod);
    }
    
    public void edit(CourseGroup courseGroup, DegreeModule degreeModule, CurricularPeriod curricularPeriod) {
        setParentCourseGroup(courseGroup);
        setDegreeModule(degreeModule);
        setCurricularPeriod(curricularPeriod);
    }

    public void delete() {
        removeCurricularPeriod();
        removeDegreeModule();
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
            if (this.getDegreeModule() instanceof CurricularCourse) {
                int periodsCompare = this.getCurricularPeriod().compareTo(o.getCurricularPeriod());
                if (periodsCompare != 0) {
                    return periodsCompare;     
                }
                return this.getDegreeModule().getName().compareTo(o.getDegreeModule().getName());
            } else {
                return this.getDegreeModule().getName().compareTo(o.getDegreeModule().getName());
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
    public void setDegreeModule(DegreeModule degreeModule) {
        super.setDegreeModule(degreeModule);
    }

}
