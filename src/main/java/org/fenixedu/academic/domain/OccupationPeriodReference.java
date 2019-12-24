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

    /**
     * The magic of the following code handles having an interface that assumes specific periods for each season, and a domain
     * that supports custom configuration of all existing seasons. The interface should be redone to allow the user to link the
     * OccupationPeriodReferences with the seasons or lessons, or whatever they are used for.
     */
    @Deprecated
    @Override
    public OccupationPeriodType getPeriodType() {
        switch (super.getPeriodType()) {
        case EXAMS:
            if (getEvaluationSeasonSet().stream().anyMatch(EvaluationSeason::isSpecial)) {
                return OccupationPeriodType.EXAMS_SPECIAL_SEASON;
            }
            break;
        case GRADE_SUBMISSION:
            if (getEvaluationSeasonSet().stream().anyMatch(EvaluationSeason::isSpecial)) {
                return OccupationPeriodType.GRADE_SUBMISSION_SPECIAL_SEASON;
            }
        }
        return super.getPeriodType();
    }

    /**
     * The magic of the following code handles having an interface that assumes specific periods for each season, and a domain
     * that supports custom configuration of all existing seasons. The interface should be redone to allow the user to link the
     * OccupationPeriodReferences with the seasons or lessons, or whatever they are used for.
     */
    @Deprecated
    @Override
    public void setPeriodType(OccupationPeriodType periodType) {
        switch (periodType) {
        case EXAMS:
            addEvaluationSeason(EvaluationSeason.readNormalSeason());
            addEvaluationSeason(EvaluationSeason.readImprovementSeason());
            super.setPeriodType(periodType);
            break;
        case EXAMS_SPECIAL_SEASON:
            addEvaluationSeason(EvaluationSeason.readSpecialSeason());
            super.setPeriodType(OccupationPeriodType.EXAMS);
            break;
        case GRADE_SUBMISSION:
            addEvaluationSeason(EvaluationSeason.readNormalSeason());
            addEvaluationSeason(EvaluationSeason.readImprovementSeason());
            super.setPeriodType(periodType);
            break;
        case GRADE_SUBMISSION_SPECIAL_SEASON:
            addEvaluationSeason(EvaluationSeason.readSpecialSeason());
            super.setPeriodType(OccupationPeriodType.GRADE_SUBMISSION);
            break;
        default:
            super.setPeriodType(periodType);
            break;
        }
    }

    public void delete() {
        setOccupationPeriod(null);
        setExecutionDegree(null);
        setRootDomainObject(null);
        setExecutionInterval(null);
        getEvaluationSeasonSet().forEach(s -> removeEvaluationSeason(s));

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

    @Override
    public ExecutionInterval getExecutionInterval() {
        return super.getExecutionInterval() != null ? super.getExecutionInterval() : getExecutionDegree().getExecutionYear()
                .getExecutionSemesterFor(getSemester());
    }

    @Deprecated
    @Override
    public Integer getSemester() {
        return super.getExecutionInterval() != null ? super.getExecutionInterval().getChildOrder() : super.getSemester();
    }

    public boolean migrateExecutionInterval() {
        if (super.getExecutionInterval() == null) {
            final ExecutionInterval interval = getExecutionDegree().getExecutionYear().getExecutionSemesterFor(getSemester());
            if (interval != null) {
                setExecutionInterval(interval);
                return true;
            }
        }
        return false;
    }

}
