/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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