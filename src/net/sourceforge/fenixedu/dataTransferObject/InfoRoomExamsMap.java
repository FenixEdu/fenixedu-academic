/*
 * Created on May 25, 2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Calendar;
import java.util.List;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
public class InfoRoomExamsMap extends InfoObject {

    List exams;

    InfoRoom infoRoom;

    Calendar startSeason1;

    Calendar endSeason1;

    Calendar startSeason2;

    Calendar endSeason2;

    public InfoRoomExamsMap() {
        super();
    }

    /**
     * @return
     */
    public Calendar getEndSeason1() {
        return endSeason1;
    }

    /**
     * @return
     */
    public Calendar getEndSeason2() {
        return endSeason2;
    }

    /**
     * @return
     */
    public Calendar getStartSeason1() {
        return startSeason1;
    }

    /**
     * @return
     */
    public Calendar getStartSeason2() {
        return startSeason2;
    }

    /**
     * @param calendar
     */
    public void setEndSeason1(Calendar calendar) {
        endSeason1 = calendar;
    }

    /**
     * @param calendar
     */
    public void setEndSeason2(Calendar calendar) {
        endSeason2 = calendar;
    }

    /**
     * @param calendar
     */
    public void setStartSeason1(Calendar calendar) {
        startSeason1 = calendar;
    }

    /**
     * @param calendar
     */
    public void setStartSeason2(Calendar calendar) {
        startSeason2 = calendar;
    }

    /**
     * @return
     */
    public List getExams() {
        return exams;
    }

    /**
     * @param exams
     */
    public void setExams(List exams) {
        this.exams = exams;
    }

    /**
     * @return
     */
    public InfoRoom getInfoRoom() {
        return infoRoom;
    }

    /**
     * @param room
     */
    public void setInfoRoom(InfoRoom room) {
        infoRoom = room;
    }

}