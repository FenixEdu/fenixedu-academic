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
 * Created on Nov 3, 2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.OccupationPeriod;

/**
 * @author Ana e Ricardo
 * 
 */
public class InfoPeriod extends InfoObject {

    protected Calendar startDate;

    protected Calendar endDate;

    protected InfoPeriod nextPeriod;

    public InfoPeriod() {
    }

    public InfoPeriod(Calendar startSeason1, Calendar endSeason2) {
        this.setStartDate(startSeason1);
        this.setEndDate(endSeason2);
    }

    public InfoPeriod getNextPeriod() {
        return nextPeriod;
    }

    public void setNextPeriod(InfoPeriod nextPeriod) {
        this.nextPeriod = nextPeriod;
    }

    /**
     * @return Returns the endDate.
     */
    public Calendar getEndDate() {
        return endDate;
    }

    /**
     * @param endDate
     *            The endDate to set.
     */
    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    /**
     * @return Returns the startDate.
     */
    public Calendar getStartDate() {
        return startDate;
    }

    /**
     * @param startDate
     *            The startDate to set.
     */
    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public void copyFromDomain(OccupationPeriod period) {
        if (period != null) {
            setStartDate(period.getStartDate());
            setEndDate(period.getEndDate());
            setExternalId(period.getExternalId());
            if (period.getNextPeriod() != null) {
                InfoPeriod infoPeriod = new InfoPeriod();
                infoPeriod.copyFromDomain(period.getNextPeriod());
                setNextPeriod(infoPeriod);
            }
        }
    }

    public static InfoPeriod newInfoFromDomain(OccupationPeriod period) {
        InfoPeriod infoPeriod = null;
        if (period != null) {
            infoPeriod = new InfoPeriod();
            infoPeriod.copyFromDomain(period);
        }
        return infoPeriod;
    }

    public Calendar endDateOfComposite() {
        Calendar end = this.endDate;
        InfoPeriod period = this.nextPeriod;
        while (period != null) {
            end = period.getEndDate();
            period = period.getNextPeriod();
        }
        return end;
    }

}