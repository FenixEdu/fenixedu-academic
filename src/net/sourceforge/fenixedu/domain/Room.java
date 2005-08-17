/*
 * Room.java
 *
 * Created on 17 de Outubro de 2002, 22:56
 */

package net.sourceforge.fenixedu.domain;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.util.DiaSemana;

/**
 * 
 * @author tfc130
 */

public class Room extends Room_Base {

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

    private boolean isFree(IPeriod period, Calendar startTime, Calendar endTime,
            DiaSemana dayOfWeek, Integer frequency, Integer week) {
        for (final IRoomOccupation roomOccupation : getRoomOccupations()) {
            if (roomOccupation.roomOccupationForDateAndTime(period, startTime, endTime, dayOfWeek, frequency, week, this)) {
                return false;
            }
        }
        return true;
    }

    public void createRoomOccupation(IPeriod period, Calendar startTime, Calendar endTime,
            DiaSemana dayOfWeek, Integer frequency, Integer week, IWrittenEvaluation writtenEvaluation) throws ExistingServiceException {
        boolean isFree = isFree(period, startTime, endTime, dayOfWeek, RoomOccupation.DIARIA, null);
        if (!isFree) {
            throw new ExistingServiceException("A sala está ocupada");
        }

        RoomOccupation roomOccupation = DomainFactory.makeRoomOccupation(this,
                startTime, endTime, dayOfWeek, RoomOccupation.DIARIA);
        roomOccupation.setPeriod(period);
        roomOccupation.setWrittenEvaluation(writtenEvaluation);
    }

}
