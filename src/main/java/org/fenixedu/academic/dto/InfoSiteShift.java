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
 * InfoShift.java
 *
 * Created on 31 de Outubro de 2002, 12:35
 */

package net.sourceforge.fenixedu.dataTransferObject;

/**
 * 
 * @author tfc130
 */

public class InfoSiteShift extends InfoObject {

    protected Object nrOfGroups;

    protected InfoShift infoShift;

    protected String orderByWeekDay;

    protected String orderByBeginHour;

    protected String orderByEndHour;

    protected String orderByRoom;

    public Object getNrOfGroups() {
        return nrOfGroups;
    }

    public InfoShift getInfoShift() {
        return infoShift;
    }

    public void setNrOfGroups(Object nrOfGroups) {
        this.nrOfGroups = nrOfGroups;
    }

    public void setInfoShift(InfoShift infoShift) {
        this.infoShift = infoShift;
    }

    public String getOrderByWeekDay() {
        return orderByWeekDay;
    }

    public void setOrderByWeekDay(String orderByWeekDay) {
        this.orderByWeekDay = orderByWeekDay;
    }

    public String getOrderByBeginHour() {
        return orderByBeginHour;
    }

    public void setOrderByBeginHour(String orderByBeginHour) {
        this.orderByBeginHour = orderByBeginHour;
    }

    public String getOrderByEndHour() {
        return orderByEndHour;
    }

    public void setOrderByEndHour(String orderByEndHour) {
        this.orderByEndHour = orderByEndHour;
    }

    public String getOrderByRoom() {
        return orderByRoom;
    }

    public void setOrderByRoom(String orderByRoom) {
        this.orderByRoom = orderByRoom;
    }

    @Override
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoSiteShift) {
            InfoSiteShift infoSiteShift = (InfoSiteShift) obj;
            resultado =
                    (this.getNrOfGroups().equals(infoSiteShift.getNrOfGroups()))
                            && (this.getInfoShift().equals(infoSiteShift.getInfoShift()));
        }
        return resultado;
    }

}