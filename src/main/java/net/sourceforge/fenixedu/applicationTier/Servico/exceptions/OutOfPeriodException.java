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
 * Created on 8/Mai/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.exceptions;

import java.util.Date;

/**
 * @author jpvl
 */
public class OutOfPeriodException extends FenixServiceException {
    private String messageKey;

    private Date startDate;

    private Date endDate;

    public OutOfPeriodException(String messageKey, Date startDate, Date endDate) {
        this.messageKey = messageKey;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * @return
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @return
     */
    public String getMessageKey() {
        return messageKey;
    }

    /**
     * @return
     */
    public Date getStartDate() {
        return startDate;
    }

}