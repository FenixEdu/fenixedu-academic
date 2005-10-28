package net.sourceforge.fenixedu.domain;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Season;

public class Exam extends Exam_Base {

    public Exam(Date examDay, Date examStartTime, Date examEndTime, 
            List<IExecutionCourse> executionCoursesToAssociate,
            List<ICurricularCourseScope> curricularCourseScopesToAssociate, 
            List<IRoom> rooms,
            IPeriod period, Season season) {

        checkScopeAndSeasonConstrains(executionCoursesToAssociate, curricularCourseScopesToAssociate, season);
    	
        setAttributesAndAssociateRooms(examDay, examStartTime, examEndTime, executionCoursesToAssociate,
                curricularCourseScopesToAssociate, rooms, period);

        this.setOjbConcreteClass(Exam.class.getName());
        this.setSeason(season);
    }

    private boolean checkScopeAndSeasonConstrains(List<IExecutionCourse> executionCoursesToAssociate,
            List<ICurricularCourseScope> curricularCourseScopesToAssociate, Season season) {

        // for each execution course, there must not exist an exam for the same
        // season and scope
        for (IExecutionCourse executionCourse : executionCoursesToAssociate) {
            for (IEvaluation evaluation : executionCourse.getAssociatedEvaluations()) {
                if (evaluation instanceof Exam) {
                    Exam existingExam = (Exam) evaluation;
                    if (existingExam.getSeason().equals(season)) {
                        // is necessary to confirm if is for the same scope
                        for (ICurricularCourseScope scope : existingExam
                                .getAssociatedCurricularCourseScope()) {
                            if (curricularCourseScopesToAssociate.contains(scope)) {
                                throw new DomainException("error.existingExam");
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public String toString() {
        return "[EXAM:" + " id= '" + this.getIdInternal() + "'\n" + " day= '" + this.getDay() + "'\n"
                + " beginning= '" + this.getBeginning() + "'\n" + " end= '" + this.getEnd() + "'\n"
                + " season= '" + this.getSeason() + "'\n" + "";
    }

    public void edit(Date examDay, Date examStartTime, Date examEndTime, 
            List<IExecutionCourse> executionCoursesToAssociate,
            List<ICurricularCourseScope> curricularCourseScopesToAssociate, 
            List<IRoom> rooms, IPeriod period, Season season) {

        // It's necessary to remove this associations before check some constrains
        this.getAssociatedExecutionCourses().clear();
        this.getAssociatedCurricularCourseScope().clear();

        checkScopeAndSeasonConstrains(executionCoursesToAssociate, curricularCourseScopesToAssociate,
                season);

        super.edit(examDay, examStartTime, examEndTime, executionCoursesToAssociate,
                curricularCourseScopesToAssociate, rooms, period);        
        this.setSeason(season);
    }

    public boolean isExamsMapPublished() {
        for (final IExecutionCourse executionCourse : getAssociatedExecutionCourses()) {
            for (final ICurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
                final IDegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
                for (final IExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
                    if (executionCourse.getExecutionPeriod().getExecutionYear() == executionDegree.getExecutionYear()
                            && !executionDegree.getTemporaryExamMap().booleanValue()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean getIsExamsMapPublished() {
        return isExamsMapPublished();
    }

}
