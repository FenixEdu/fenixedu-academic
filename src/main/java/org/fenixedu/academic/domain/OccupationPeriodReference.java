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
package org.fenixedu.academic.domain;

import java.util.List;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;

public class OccupationPeriodReference extends OccupationPeriodReference_Base {
    private OccupationPeriodReference() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public OccupationPeriodReference(OccupationPeriod period, ExecutionDegree degree, OccupationPeriodType type,
            ExecutionInterval interval, CurricularYearList curricularYears) {
        this();
        if (period == null || degree == null) {
            throw new DomainException("exception.null.arguments");
        }
        setOccupationPeriod(period);
        setExecutionDegree(degree);
        setPeriodType(type);
        setExecutionInterval(interval);
        setCurricularYears(curricularYears);
    }

    @Deprecated
    public OccupationPeriodReference(OccupationPeriod period, ExecutionDegree degree, OccupationPeriodType type, Integer semester,
            CurricularYearList curricularYears) {
        this(period, degree, type, degree.getExecutionYear().getExecutionSemesterFor(semester), curricularYears);
    }

    public void delete() {
        setOccupationPeriod(null);
        setExecutionDegree(null);
        setRootDomainObject(null);
        setExecutionInterval(null);

        deleteDomainObject();
    }

    public String getCurricularYearsString() {
        CurricularYearList years = getCurricularYears();
        if (years == null) {
            return "-1";
        }

        List<Integer> yearList = years.getYears();

        StringBuilder returnStr = new StringBuilder();

        for (Integer year : yearList) {
            if (returnStr.length() > 0) {
                returnStr.append(",");
            }

            returnStr.append(year);
        }

        return returnStr.toString();
    }

    public String getCurricularYearsPresentationString() {
        CurricularYearList years = getCurricularYears();
        if (years == null || years.hasAll()) {
            return "Todos os anos";
        }

        List<Integer> yearList = years.getYears();

        StringBuilder returnStr = new StringBuilder();

        for (Integer year : yearList) {
            if (returnStr.length() > 0) {
                returnStr.append(", ");
            }

            returnStr.append(year + "º");
        }

        if (yearList.size() > 1) {
            returnStr.append(" Anos");
        } else {
            returnStr.append(" Ano");
        }

        return returnStr.toString();
    }

    @Deprecated
    @Override
    public Integer getSemester() {
        return super.getExecutionInterval() != null ? super.getExecutionInterval().getChildOrder() : super.getSemester();
    }

}
