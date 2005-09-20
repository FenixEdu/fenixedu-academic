/*
 * Created on 18/Mar/2003
 *  
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.Season;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class Exam extends Exam_Base {

    public Exam() {
        this.setOjbConcreteClass(Exam.class.getName());
    }

    public Exam(Date day, Date beginning, Date end, Season season,
			List<IExecutionCourse> executionCoursesToAssociate,
			List<ICurricularCourseScope> curricularCourseScopesToAssociate,
			List<IRoom> rooms, IPeriod period) {
		checkScopeAndSeasonConstrains(executionCoursesToAssociate, curricularCourseScopesToAssociate,
				season);
		checkValidHours(beginning, end);

		this.setOjbConcreteClass(Exam.class.getName());
		this.setDayDate(day);
		this.setBeginningDate(beginning);
		this.setEndDate(end);
		this.setSeason(season);
		
		this.getAssociatedExecutionCourses().addAll(executionCoursesToAssociate);
		this.getAssociatedCurricularCourseScope().addAll(curricularCourseScopesToAssociate);
		
        associateRooms(rooms, period);
	}

	private boolean checkValidHours(Date beginning, Date end) {
		if (beginning.after(end)) {
			throw new DomainException("error.data.exame.inválida");
		}

		return true;
	}

	private boolean checkScopeAndSeasonConstrains(List<IExecutionCourse> executionCoursesToAssociate,
			List<ICurricularCourseScope> curricularCourseScopesToAssociate, Season season) {

		// for each execution course, there must not exist an exam for the same season and scope
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

    public List getAssociatedRooms() {
        final List<IRoom> result = new ArrayList<IRoom>();
        for (final IRoomOccupation roomOccupation : this.getAssociatedRoomOccupation()) {
            result.add(roomOccupation.getRoom());
        }
        return result;
    }

    public void edit(Date examDate, Date examStartTime, Date examEndTime, Season season,
            List<IExecutionCourse> executionCoursesToAssociate,
            List<ICurricularCourseScope> curricularCourseScopesToAssociate, List<IRoom> rooms,
            IPeriod period) {

        // It's necessary to remove this associations before check some constrains
        this.getAssociatedExecutionCourses().clear();
        this.getAssociatedCurricularCourseScope().clear();
        
        checkScopeAndSeasonConstrains(executionCoursesToAssociate, curricularCourseScopesToAssociate,
                season);

        this.setDayDate(examDate);
        this.setBeginningDate(examStartTime);
        this.setEndDate(examEndTime);
        this.setSeason(season);
        
        this.getAssociatedExecutionCourses().addAll(executionCoursesToAssociate);        
        this.getAssociatedCurricularCourseScope().addAll(curricularCourseScopesToAssociate);

        associateRooms(rooms, period);
    }

    public void delete() {
        if (hasAnyWrittenEvaluationEnrolments()) {
            throw new DomainException("error.notAuthorizedExamDelete.withStudent");
        }
        getAssociatedExecutionCourses().clear();
        deleteRoomOccupationsNotContainedInList(null);
        getAssociatedCurricularCourseScope().clear();

        super.deleteDomainObject();
    }

    private void associateRooms(final List<IRoom> rooms, final IPeriod period) {
        final DiaSemana dayOfWeek = new DiaSemana(this.getDay().get(Calendar.DAY_OF_WEEK));
        for (final IRoom room : rooms) {
            if (!hasOccupationForRoom(room)) {
                room.createRoomOccupation(period, this.getBeginning(), this.getEnd(), dayOfWeek,
                        RoomOccupation.DIARIA, null, this);
            }
        }

        deleteRoomOccupationsNotContainedInList(rooms);
    }

    private boolean hasOccupationForRoom(IRoom room) {
        for (final IRoomOccupation roomOccupation : this.getAssociatedRoomOccupation()) {
            if (roomOccupation.getRoom() == room) {
                return true;
            }
        }
        return false;
    }

    private void deleteRoomOccupationsNotContainedInList(final List<IRoom> rooms) {
        for (final IRoomOccupation roomOccupation : this.getAssociatedRoomOccupation()) {
            if (rooms == null || !rooms.contains(roomOccupation.getRoom())) {
                roomOccupation.delete();
            }
        }
    }
}
