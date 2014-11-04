/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 14/Mai/2003
 *
 * 
 */
package org.fenixedu.academic.dto;

import java.util.List;

/**
 * @author João Mota
 * 
 */
public class InfoSiteRoomTimeTable extends DataTranferObject {

    // private List infoLessons;
    private List infoShowOccupation;

    private InfoRoom infoRoom;

    /**
     * @return
     */
    public List getInfoShowOccupation() {
        return infoShowOccupation;
    }

    /**
     * @return
     */
    public InfoRoom getInfoRoom() {
        return infoRoom;
    }

    /**
     * @param list
     */
    public void setInfoShowOccupation(List list) {
        infoShowOccupation = list;
    }

    /**
     * @param list
     */
    public void setInfoRoom(InfoRoom list) {
        infoRoom = list;
    }

}