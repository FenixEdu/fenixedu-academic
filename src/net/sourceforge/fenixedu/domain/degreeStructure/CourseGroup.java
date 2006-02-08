package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class CourseGroup extends CourseGroup_Base {

    protected CourseGroup() {
        super();
        setOjbConcreteClass(CourseGroup.class.getName());
    }

    public CourseGroup(String name) {
        this();
        super.setName(name);
    }

    public boolean isLeaf() {
        return false;
    }

    public void edit(String name) {
        setName(name);
    }

    public Boolean getCanBeDeleted() {
        return !hasAnyCourseGroupContexts();
    }

    public void delete() {
        if (getCanBeDeleted()) {
            super.delete();
            super.deleteDomainObject();
        } else {
            throw new DomainException("courseGroup.notEmptyCourseGroupContexts");
        }
    }

    public void print(StringBuilder dcp, String tabs, Context previousContext) {
        String tab = tabs + "\t";
        dcp.append(tab);
        dcp.append("[CG ").append(this.getIdInternal()).append("] ").append(this.getName()).append("\n");

        for (Context context : this.getCourseGroupContexts()) {
            context.getDegreeModule().print(dcp, tab, context);
        }
    }

    public List<Context> getContextsWithCurricularCourses() {
        List<Context> result = new ArrayList<Context>();
        for (Context context : this.getCourseGroupContexts()) {
            if (context.getDegreeModule() instanceof CurricularCourse) {
                result.add(context);
            }
        }

        return result;
    }

    public List<Context> getContextsWithCourseGroups() {
        List<Context> result = new ArrayList<Context>();
        for (Context context : this.getCourseGroupContexts()) {
            if (context.getDegreeModule() instanceof CourseGroup) {
                result.add(context);
            }
        }

        return result;
    }

    public Double getEctsCredits() {
        Double result = 0.0;

        for (Context context : this.getCourseGroupContexts()) {
            if (context.getDegreeModule() != null && context.getDegreeModule().getEctsCredits() != null) {
                result += context.getDegreeModule().getEctsCredits();
            }
        }

        return result;
    }

    protected void checkContextsFor(final CourseGroup parentCourseGroup,
            final CurricularPeriod curricularPeriod) {
        for (final Context context : this.getDegreeModuleContexts()) {
            if (context.getCourseGroup() == parentCourseGroup) {
                throw new DomainException("courseGroup.contextAlreadyExistForCourseGroup");
            }
        }
    }

    @Checked("CourseGroupPredicates.curricularPlanMemberWritePredicate")
    public CurricularCourse createCurricularCourse(Double weight, String prerequisites,
            String prerequisitesEn, CurricularStage curricularStage, CompetenceCourse competenceCourse,
            CurricularPeriod curricularPeriod, ExecutionPeriod beginExecutionPeriod) {
        checkIfPresentInDegreeCurricularPlan(this.getParentDegreeCurricularPlan(), competenceCourse);
        checkIfAnualBeginsInFirstPeriod(competenceCourse, curricularPeriod);
        return new CurricularCourse(weight, prerequisites, prerequisitesEn, curricularStage,
                competenceCourse, this, curricularPeriod, beginExecutionPeriod);
    }

    private void checkIfPresentInDegreeCurricularPlan(final DegreeCurricularPlan degreeCurricularPlan,
            final CompetenceCourse competenceCourse) {
        final List<CurricularCourse> curricularCoursesFromDegreeCurricularPlan = degreeCurricularPlan
                .getDcpCurricularCourses();
        for (CurricularCourse curricularCourse : competenceCourse.getAssociatedCurricularCourses()) {
            if (curricularCoursesFromDegreeCurricularPlan.contains(curricularCourse)) {
                throw new DomainException(
                        "competenceCourse.already.has.a.curricular.course.in.degree.curricular.plan");
            }
        }
    }

    private void checkIfAnualBeginsInFirstPeriod(final CompetenceCourse competenceCourse,
            CurricularPeriod curricularPeriod) {
        if (competenceCourse.getRegime().equals(RegimeType.ANUAL)
                && curricularPeriod.getChildByOrder(1) == null) {
            throw new DomainException(
                    "competenceCourse.anual.but.trying.to.associate.curricular.course.not.to.first.period");
        }
    }

    @Override
    @Checked("CourseGroupPredicates.curricularPlanMemberWritePredicate")
    public void setName(String name) {
        super.setName(name);
    }
    
    
}
