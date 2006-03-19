package net.sourceforge.fenixedu.domain.space;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.DiaSemana;

public class OldRoom extends OldRoom_Base {

    public OldRoom() {
		super();
	}

	/** @deprecated */
    public void createRoomOccupation(OccupationPeriod period, Calendar startTime, Calendar endTime,
			DiaSemana dayOfWeek, Integer frequency, Integer week, WrittenEvaluation writtenEvaluation) {
		boolean isFree = isFree(period, startTime, endTime, dayOfWeek, RoomOccupation.DIARIA, null);
		if (!isFree) {
			throw new DomainException("error.roomOccupied");
		}

		RoomOccupation roomOccupation = new RoomOccupation(this, startTime, endTime, dayOfWeek,
				RoomOccupation.DIARIA);
		roomOccupation.setPeriod(period);
		roomOccupation.setWrittenEvaluation(writtenEvaluation);
	}

    /** @deprecated */
	public void delete() {
        if (canBeDeleted()) {
            setBuilding(null);
            deleteDomainObject();
        } else {
            String[] args = { "a sala", "as aulas" };
            throw new DomainException("errors.invalid.delete.with.objects", args);            
        }
	}

	/** @deprecated */
    public boolean isFree(OccupationPeriod period, Calendar startTime, Calendar endTime, DiaSemana dayOfWeek,
            Integer frequency, Integer week) {
        for (final RoomOccupation roomOccupation : getRoomOccupations()) {
            if (roomOccupation.roomOccupationForDateAndTime(period, startTime, endTime, dayOfWeek,
                    frequency, week, this)) {
                return false;
            }
        }
        return true;
    }

    /** @deprecated */
    private boolean canBeDeleted() {
        return getAssociatedLessons().isEmpty()
                && getAssociatedSummaries().isEmpty()
                && getRoomOccupations().isEmpty()
                && getWrittenEvaluationEnrolments().isEmpty();
    }

    public List<Lesson> findLessonsForExecutionPeriod(final ExecutionPeriod executionPeriod) {
        final List<Lesson> lessons = new ArrayList<Lesson>();
        for (final RoomOccupation roomOccupation : getRoomOccupations()) {
            final Lesson lesson = roomOccupation.getLesson();
            if (lesson != null && lesson.getExecutionPeriod() == executionPeriod) {
                lessons.add(lesson);
            }
        }
        return lessons;
    }
}
