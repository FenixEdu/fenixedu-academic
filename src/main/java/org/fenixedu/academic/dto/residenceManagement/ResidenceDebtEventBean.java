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
package org.fenixedu.academic.dto.residenceManagement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fenixedu.academic.domain.residence.ResidenceMonth;
import org.fenixedu.academic.util.Money;
import org.joda.time.YearMonthDay;

public class ResidenceDebtEventBean extends ResidenceEventBean {

    private Money roomValuePaid;
    private String paidDate;

    private YearMonthDay paidDateObject;
    private ResidenceMonth month;

    public ResidenceDebtEventBean(String userName, String fiscalNumber, String name, Double roomValue, String room,
            String paidDate, Double roomValuePaid) {
        super(userName, fiscalNumber, name, roomValue, room);
        this.setPaidDate(paidDate);
        this.setRoomValuePaid(new Money(roomValuePaid));
    }

    public void setRoomValuePaid(Money roomValuePaid) {
        this.roomValuePaid = roomValuePaid;
    }

    public Money getRoomValuePaid() {
        return roomValuePaid;
    }

    public void setPaidDate(String paidDate) {
        this.paidDate = paidDate;
    }

    public String getPaidDate() {
        return paidDate;
    }

    @Override
    public boolean getStatus() {
        if (!super.getStatus()) {
            return false;
        }

        String date = getPaidDate();
        Pattern p = Pattern.compile("^(\\d\\d).(\\d\\d).((\\d\\d){1,2})$");
        Matcher m = p.matcher(date);

        if (!m.matches()) {
            setStatusMessage("label.error.invalid.date");
            return false;
        }

        int day = Integer.valueOf(m.group(1));
        int month = Integer.valueOf(m.group(2));
        int year = m.group(3).length() == 2 ? 2000 + Integer.valueOf(m.group(3)) : Integer.valueOf(m.group(3));
        this.setPaidDateObject(new YearMonthDay(year, month, day));

        if (getPaidDate() == null) {
            setStatusMessage("label.error.invalid.date");
            return false;
        }

        if (getRoomValuePaid() == null) {
            setStatusMessage("label.error.invalid.payment.amount");
            return false;
        }

        return true;
    }

    public YearMonthDay getPaidDateObject() {
        return paidDateObject;
    }

    public void setPaidDateObject(YearMonthDay paidDateObject) {
        this.paidDateObject = paidDateObject;
    }

    public ResidenceMonth getMonth() {
        return month;
    }

    public void setMonth(ResidenceMonth month) {
        this.month = month;
    }

}
