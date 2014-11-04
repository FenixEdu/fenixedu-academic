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
package net.sourceforge.fenixedu.domain.phd;

import java.math.BigDecimal;

import org.joda.time.LocalDate;

public class PhdProgramInformationBean implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private LocalDate beginDate;
    private BigDecimal minThesisEctsCredits;
    private BigDecimal maxThesisEctsCredits;
    private BigDecimal minStudyPlanEctsCredits;
    private BigDecimal maxStudyPlanEctsCredits;
    private Integer numberOfYears;
    private Integer numberOfSemesters;

    private PhdProgram phdProgram;

    public PhdProgramInformationBean(final PhdProgram phdProgram) {
        this.phdProgram = phdProgram;
    }

    public PhdProgramInformationBean(final PhdProgramInformation phdProgramInformation) {
        this.phdProgram = phdProgramInformation.getPhdProgram();
        this.beginDate = phdProgramInformation.getBeginDate();
        this.minThesisEctsCredits = phdProgramInformation.getMinThesisEctsCredits();
        this.maxThesisEctsCredits = phdProgramInformation.getMaxThesisEctsCredits();
        this.minStudyPlanEctsCredits = phdProgramInformation.getMinStudyPlanEctsCredits();
        this.maxStudyPlanEctsCredits = phdProgramInformation.getMaxStudyPlanEctsCredits();
        this.numberOfYears = phdProgramInformation.getNumberOfYears();
        this.numberOfSemesters = phdProgramInformation.getNumberOfSemesters();
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public BigDecimal getMinThesisEctsCredits() {
        return minThesisEctsCredits;
    }

    public void setMinThesisEctsCredits(BigDecimal minThesisEctsCredits) {
        this.minThesisEctsCredits = minThesisEctsCredits;
    }

    public BigDecimal getMaxThesisEctsCredits() {
        return maxThesisEctsCredits;
    }

    public void setMaxThesisEctsCredits(BigDecimal maxThesisEctsCredits) {
        this.maxThesisEctsCredits = maxThesisEctsCredits;
    }

    public BigDecimal getMinStudyPlanEctsCredits() {
        return minStudyPlanEctsCredits;
    }

    public void setMinStudyPlanEctsCredits(BigDecimal minStudyPlanEctsCredits) {
        this.minStudyPlanEctsCredits = minStudyPlanEctsCredits;
    }

    public BigDecimal getMaxStudyPlanEctsCredits() {
        return maxStudyPlanEctsCredits;
    }

    public void setMaxStudyPlanEctsCredits(BigDecimal maxStudyPlanEctsCredits) {
        this.maxStudyPlanEctsCredits = maxStudyPlanEctsCredits;
    }

    public PhdProgram getPhdProgram() {
        return phdProgram;
    }

    public Integer getNumberOfYears() {
        return numberOfYears;
    }

    public void setNumberOfYears(Integer numberOfYears) {
        this.numberOfYears = numberOfYears;
    }

    public Integer getNumberOfSemesters() {
        return numberOfSemesters;
    }

    public void setNumberOfSemesters(Integer numberOfSemesters) {
        this.numberOfSemesters = numberOfSemesters;
    }
}
