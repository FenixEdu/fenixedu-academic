package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.Season;

public class Exam extends Exam_Base {

    public static List<Exam> readExams() {
        List<Exam> result = new ArrayList<Exam>();
        
        for (Evaluation evaluation : RootDomainObject.getInstance().getEvaluations()) {
            if (evaluation instanceof Exam) {
                result.add((Exam) evaluation);
            }
        }        
        return result;
    }
    
    public Exam(Date examDay, Date examStartTime, Date examEndTime, 
            List<ExecutionCourse> executionCoursesToAssociate,
            List<DegreeModuleScope> curricularCourseScopesToAssociate, 
            List<OldRoom> rooms,
            OccupationPeriod period, Season season) {
    	super();
        checkScopeAndSeasonConstrains(executionCoursesToAssociate, curricularCourseScopesToAssociate, season);
    	
        setAttributesAndAssociateRooms(examDay, examStartTime, examEndTime, executionCoursesToAssociate,
                curricularCourseScopesToAssociate, rooms, period);

        this.setOjbConcreteClass(Exam.class.getName());
        this.setSeason(season);
        checkIntervalBetweenEvaluations();
    }

    private boolean checkScopeAndSeasonConstrains(List<ExecutionCourse> executionCoursesToAssociate,
            List<DegreeModuleScope> curricularCourseScopesToAssociate, Season season) {

        // for each execution course, there must not exist an exam for the same
        // season and scope
        for (ExecutionCourse executionCourse : executionCoursesToAssociate) {
            for (Evaluation evaluation : executionCourse.getAssociatedEvaluations()) {
                if (evaluation instanceof Exam) {
                    Exam existingExam = (Exam) evaluation;
                    if (existingExam.getSeason().equals(season)) {
                        // is necessary to confirm if is for the same scope
                        for (CurricularCourseScope scope : existingExam
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

    public void edit(Date examDay, Date examStartTime, Date examEndTime, 
            List<ExecutionCourse> executionCoursesToAssociate,
            List<DegreeModuleScope> curricularCourseScopesToAssociate, 
            List<OldRoom> rooms, OccupationPeriod period, Season season) {

        // It's necessary to remove this associations before check some constrains
        this.getAssociatedExecutionCourses().clear();
        this.getAssociatedCurricularCourseScope().clear();

        checkScopeAndSeasonConstrains(executionCoursesToAssociate, curricularCourseScopesToAssociate,
                season);

        super.edit(examDay, examStartTime, examEndTime, executionCoursesToAssociate,
                curricularCourseScopesToAssociate, rooms, period);        
        this.setSeason(season);
        checkIntervalBetweenEvaluations();
    }

    public boolean isExamsMapPublished() {
        for (final ExecutionCourse executionCourse : getAssociatedExecutionCourses()) {
            for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
                final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
                for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
                    if (executionCourse.getExecutionPeriod().getExecutionYear() == executionDegree.getExecutionYear()
                            && (executionDegree.getTemporaryExamMap() == null || !executionDegree.getTemporaryExamMap().booleanValue())) {
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

    public static List<Exam> getAllByRoomAndExecutionPeriod(String room, String executionPeriod, String year) {
        List<Exam> result = new ArrayList<Exam>();
        
        outter:
        for (Exam exam : Exam.readExams()) {
            for (RoomOccupation occupation : exam.getAssociatedRoomOccupation()) {
                if (! occupation.getRoom().getNome().equals(room)) {
                    continue outter;
                }
            }
            
            for (ExecutionCourse course : exam.getAssociatedExecutionCourses()) {
                if (! course.getExecutionPeriod().getName().equals(executionPeriod)) {
                    continue outter;
                }
                
                if (! course.getExecutionPeriod().getExecutionYear().getYear().equals(year)) {
                    continue outter;
                }
            }
            
            result.add(exam);
        }
        
        return result;
    }

    public static List<Exam> getAllByDate(Calendar examDay, Calendar examStartTime) {
        return getAllByDate(examDay, examStartTime, null);
    }

    public static List<Exam> getAllByDate(Calendar examDay, Calendar examStartTime, Calendar examEndTime) {
        List<Exam> result = new ArrayList<Exam>();
        
        outter:
        for (Exam exam : Exam.readExams()) {
            if (! DateFormatUtil.equalDates("dd/MM/yyyy", examDay.getTime(), exam.getDayDate())) {
                continue;
            }
            
            if (examStartTime != null && !DateFormatUtil.equalDates("HH:mm", examStartTime.getTime(), exam.getBeginningDate())) {
                continue;
            }
            
            if (examEndTime != null && !DateFormatUtil.equalDates("HH:mm", examEndTime.getTime(), exam.getEndDate())) {
                continue;
            }
            
            result.add(exam);
        }

        return result;
    }

}