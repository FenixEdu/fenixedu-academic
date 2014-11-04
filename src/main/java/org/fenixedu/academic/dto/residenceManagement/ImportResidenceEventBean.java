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

import org.fenixedu.academic.domain.residence.ResidenceMonth;
import org.fenixedu.academic.domain.residence.ResidenceYear;
import org.fenixedu.academic.ui.struts.action.webSiteManager.SimpleFileBean;

public class ImportResidenceEventBean extends SimpleFileBean {

    private ResidenceYear residenceYear;
    private ResidenceMonth residenceMonth;
    private Integer paymentLimitDay;

    private String spreadsheetName;

    public ImportResidenceEventBean(ResidenceMonth month) {
        setResidenceMonth(month);
        setResidenceYear(month.getYear());
    }

    public ImportResidenceEventBean() {
        setResidenceYear(null);
        setResidenceMonth(null);
    }

    public ResidenceYear getResidenceYear() {
        return this.residenceYear;
    }

    public void setResidenceYear(ResidenceYear residenceYear) {
        this.residenceYear = residenceYear;
        if (residenceYear != null) {
            setPaymentLimitDay(residenceYear.getUnit().getCurrentPaymentLimitDay());
        }
    }

    public ResidenceMonth getResidenceMonth() {
        return this.residenceMonth;
    }

    public void setResidenceMonth(ResidenceMonth residenceMonth) {
        this.residenceMonth = residenceMonth;
    }

    public Integer getPaymentLimitDay() {
        return paymentLimitDay;
    }

    public void setPaymentLimitDay(Integer paymentLimitDay) {
        this.paymentLimitDay = paymentLimitDay;
    }

    public String getSpreadsheetName() {
        return spreadsheetName;
    }

    public void setSpreadsheetName(String spreadsheetName) {
        this.spreadsheetName = spreadsheetName;
    }
}
