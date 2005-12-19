package net.sourceforge.fenixedu.domain.space;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.domain.IWrittenEvaluation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.DiaSemana;

public class Room extends Room_Base {

	public void createRoomOccupation(IPeriod period, Calendar startTime, Calendar endTime,
			DiaSemana dayOfWeek, Integer frequency, Integer week, IWrittenEvaluation writtenEvaluation) {
		boolean isFree = isFree(period, startTime, endTime, dayOfWeek, RoomOccupation.DIARIA, null);
		if (!isFree) {
			throw new DomainException("error.roomOccupied");
		}

		RoomOccupation roomOccupation = new RoomOccupation(this, startTime, endTime, dayOfWeek,
				RoomOccupation.DIARIA);
		roomOccupation.setPeriod(period);
		roomOccupation.setWrittenEvaluation(writtenEvaluation);
	}

	public void delete() {
        if (canBeDeleted()) {
            setBuilding(null);
            deleteDomainObject();
        } else {
            String[] args = { "a sala", "as aulas" };
            throw new DomainException("errors.invalid.delete.with.objects", args);            
        }
	}

    private boolean isFree(IPeriod period, Calendar startTime, Calendar endTime, DiaSemana dayOfWeek,
            Integer frequency, Integer week) {
        for (final IRoomOccupation roomOccupation : getRoomOccupations()) {
            if (roomOccupation.roomOccupationForDateAndTime(period, startTime, endTime, dayOfWeek,
                    frequency, week, this)) {
                return false;
            }
        }
        return true;
    }

    private boolean canBeDeleted() {
        return getAssociatedLessons().isEmpty()
                && getAssociatedSummaries().isEmpty()
                && getRoomOccupations().isEmpty()
                && getWrittenEvaluationEnrolments().isEmpty();
    }

    public String toString() {
        String result = "[SALA";
        result += ", codInt=" + getIdInternal();
        result += ", nome=" + getNome();
        result += ", piso=" + getPiso();
        result += ", tipo=" + getTipo();
        result += ", capacidadeNormal=" + getCapacidadeNormal();
        result += ", capacidadeExame=" + getCapacidadeExame();
        result += "]";
        return result;
    }

}
