package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Calendar;

import net.sourceforge.fenixedu.util.DiaSemana;

public class InfoRoomOccupationEditor extends InfoObject {

    protected Calendar startTime;

    protected Calendar endTime;

    protected DiaSemana dayOfWeek;

    protected InfoRoom infoRoom;

    protected InfoPeriod infoPeriod;

    protected Integer frequency;

    protected Integer weekOfQuinzenalStart;

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = new Integer(frequency);
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Integer getWeekOfQuinzenalStart() {
        return weekOfQuinzenalStart;
    }

    public void setWeekOfQuinzenalStart(Integer weekOfQuinzenalStart) {
        this.weekOfQuinzenalStart = weekOfQuinzenalStart;
    }

    public DiaSemana getDayOfWeek() {
        return dayOfWeek;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setDayOfWeek(DiaSemana semana) {
        dayOfWeek = semana;
    }

    public void setEndTime(Calendar calendar) {
        endTime = calendar;
    }

    public void setStartTime(Calendar calendar) {
        startTime = calendar;
    }

    public InfoPeriod getInfoPeriod() {
        return infoPeriod;
    }

    public void setInfoPeriod(InfoPeriod infoPeriod) {
        this.infoPeriod = infoPeriod;
    }

    public InfoRoom getInfoRoom() {
        return infoRoom;
    }

    public void setInfoRoom(InfoRoom infoRoom) {
        this.infoRoom = infoRoom;
    }

}
