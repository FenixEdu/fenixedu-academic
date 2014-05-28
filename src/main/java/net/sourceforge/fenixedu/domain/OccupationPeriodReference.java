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
package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;

import com.google.common.base.Function;

public class OccupationPeriodReference extends OccupationPeriodReference_Base {

    public static Function<OccupationPeriodReference, OccupationPeriod> FUNCTION_TO_PERIOD =
            new Function<OccupationPeriodReference, OccupationPeriod>() {

                @Override
                public OccupationPeriod apply(OccupationPeriodReference reference) {
                    return reference.getOccupationPeriod();
                }

            };

    public static Function<OccupationPeriodReference, ExecutionDegree> FUNCTION_TO_DEGREE =
            new Function<OccupationPeriodReference, ExecutionDegree>() {

                @Override
                public ExecutionDegree apply(OccupationPeriodReference reference) {
                    return reference.getExecutionDegree();
                }

            };

    private OccupationPeriodReference() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public OccupationPeriodReference(OccupationPeriod period, ExecutionDegree degree, OccupationPeriodType type,
            Integer semester, CurricularYearList curricularYears) {
        this();
        if (period == null || degree == null) {
            throw new DomainException("exception.null.arguments");
        }
        setOccupationPeriod(period);
        setExecutionDegree(degree);
        setPeriodType(type);
        setSemester(semester);
        setCurricularYears(curricularYears);
    }

    public void delete() {
        setOccupationPeriod(null);
        setExecutionDegree(null);
        setRootDomainObject(null);

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
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasSemester() {
        return getSemester() != null;
    }

    @Deprecated
    public boolean hasExecutionDegree() {
        return getExecutionDegree() != null;
    }

    @Deprecated
    public boolean hasOccupationPeriod() {
        return getOccupationPeriod() != null;
    }

    @Deprecated
    public boolean hasPeriodType() {
        return getPeriodType() != null;
    }

    @Deprecated
    public boolean hasCurricularYears() {
        return getCurricularYears() != null;
    }

}
