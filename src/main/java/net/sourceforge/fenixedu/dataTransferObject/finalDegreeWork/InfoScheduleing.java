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
 * Created on 2004/03/10
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;

/**
 * @author Luis Cruz
 * 
 */
public class InfoScheduleing extends InfoObject {

    private InfoExecutionDegree executionDegree;

    private Date startOfProposalPeriod;

    private Date endOfProposalPeriod;

    private Date startOfCandidacyPeriod;

    private Date endOfCandidacyPeriod;

    private Integer minimumNumberOfCompletedCourses;

    private Integer maximumCurricularYearToCountCompletedCourses;

    private Integer minimumCompletedCurricularYear;

    private Integer minimumNumberOfStudents;

    private Integer maximumNumberOfStudents;

    private Integer maximumNumberOfProposalCandidaciesPerGroup;

    private Boolean attributionByTeachers;

    private Boolean allowSimultaneousCoorientationAndCompanion;

    private Integer minimumCompletedCreditsFirstCycle;

    private Integer minimumCompletedCreditsSecondCycle;

    /* Construtores */
    public InfoScheduleing() {
        super();
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoScheduleing) {
            InfoScheduleing proposal = (InfoScheduleing) obj;

            result = getExecutionDegree().equals(proposal.getExecutionDegree());
        }
        return result;
    }

    @Override
    public String toString() {
        String result = "[InfoScheduleing";
        result += ", externalId=" + getExternalId();
        result += ", InfoDegreeCurricularPlan=" + getExecutionDegree();
        result += "]";
        return result;
    }

    /**
     * @return Returns the endOfProposalPeriod.
     */
    public Date getEndOfProposalPeriod() {
        return endOfProposalPeriod;
    }

    /**
     * @param endOfProposalPeriod
     *            The endOfProposalPeriod to set.
     */
    public void setEndOfProposalPeriod(Date endOfProposalPeriod) {
        this.endOfProposalPeriod = endOfProposalPeriod;
    }

    /**
     * @return Returns the executionDegree.
     */
    public InfoExecutionDegree getExecutionDegree() {
        return executionDegree;
    }

    /**
     * @param executionDegree
     *            The executionDegree to set.
     */
    public void setExecutionDegree(InfoExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
    }

    /**
     * @return Returns the startOfProposalPeriod.
     */
    public Date getStartOfProposalPeriod() {
        return startOfProposalPeriod;
    }

    /**
     * @param startOfProposalPeriod
     *            The startOfProposalPeriod to set.
     */
    public void setStartOfProposalPeriod(Date startOfProposalPeriod) {
        this.startOfProposalPeriod = startOfProposalPeriod;
    }

    /**
     * @return Returns the startOfCandidacyPeriod.
     */
    public Date getStartOfCandidacyPeriod() {
        return startOfCandidacyPeriod;
    }

    /**
     * @param startOfCandidacyPeriod
     *            The startOfCandidacyPeriod to set.
     */
    public void setStartOfCandidacyPeriod(Date startOfCandidacyPeriod) {
        this.startOfCandidacyPeriod = startOfCandidacyPeriod;
    }

    /**
     * @return Returns the endOfCandidacyPeriod.
     */
    public Date getEndOfCandidacyPeriod() {
        return endOfCandidacyPeriod;
    }

    /**
     * @param endOfCandidacyPeriod
     *            The endOfCandidacyPeriod to set.
     */
    public void setEndOfCandidacyPeriod(Date endOfCandidacyPeriod) {
        this.endOfCandidacyPeriod = endOfCandidacyPeriod;
    }

    /**
     * @return Returns the minimumNumberOfCompletedCourses.
     */
    public Integer getMinimumNumberOfCompletedCourses() {
        return minimumNumberOfCompletedCourses;
    }

    /**
     * @param minimumNumberOfCompletedCourses
     *            The minimumNumberOfCompletedCourses to set.
     */
    public void setMinimumNumberOfCompletedCourses(Integer minimumNumberOfCompletedCourses) {
        this.minimumNumberOfCompletedCourses = minimumNumberOfCompletedCourses;
    }

    /**
     * @return Returns the maximumNumberOfStudents.
     */
    public Integer getMaximumNumberOfStudents() {
        return maximumNumberOfStudents;
    }

    /**
     * @param maximumNumberOfStudents
     *            The maximumNumberOfStudents to set.
     */
    public void setMaximumNumberOfStudents(Integer maximumNumberOfStudents) {
        this.maximumNumberOfStudents = maximumNumberOfStudents;
    }

    /**
     * @return Returns the minimumNumberOfStudents.
     */
    public Integer getMinimumNumberOfStudents() {
        return minimumNumberOfStudents;
    }

    /**
     * @param minimumNumberOfStudents
     *            The minimumNumberOfStudents to set.
     */
    public void setMinimumNumberOfStudents(Integer minimumNumberOfStudents) {
        this.minimumNumberOfStudents = minimumNumberOfStudents;
    }

    /**
     * @return Returns the maximumNumberOfProposalCandidaciesPerGroup.
     */
    public Integer getMaximumNumberOfProposalCandidaciesPerGroup() {
        return maximumNumberOfProposalCandidaciesPerGroup;
    }

    /**
     * @param maximumNumberOfProposalCandidaciesPerGroup
     *            The maximumNumberOfProposalCandidaciesPerGroup to set.
     */
    public void setMaximumNumberOfProposalCandidaciesPerGroup(Integer maximumNumberOfProposalCandidaciesPerGroup) {
        this.maximumNumberOfProposalCandidaciesPerGroup = maximumNumberOfProposalCandidaciesPerGroup;
    }

    public Boolean getAttributionByTeachers() {
        return attributionByTeachers;
    }

    public void setAttributionByTeachers(Boolean attributionByTeachers) {
        this.attributionByTeachers = attributionByTeachers;
    }

    public Integer getMaximumCurricularYearToCountCompletedCourses() {
        return maximumCurricularYearToCountCompletedCourses;
    }

    public void setMaximumCurricularYearToCountCompletedCourses(Integer maximumCurricularYearToCountCompletedCourses) {
        this.maximumCurricularYearToCountCompletedCourses = maximumCurricularYearToCountCompletedCourses;
    }

    public Integer getMinimumCompletedCurricularYear() {
        return minimumCompletedCurricularYear;
    }

    public void setMinimumCompletedCurricularYear(Integer minimumCompletedCurricularYear) {
        this.minimumCompletedCurricularYear = minimumCompletedCurricularYear;
    }

    public Boolean getAllowSimultaneousCoorientationAndCompanion() {
        return allowSimultaneousCoorientationAndCompanion;
    }

    public void setAllowSimultaneousCoorientationAndCompanion(Boolean allowSimultaneousCoorientationAndCompanion) {
        this.allowSimultaneousCoorientationAndCompanion = allowSimultaneousCoorientationAndCompanion;
    }

    public Integer getMinimumCompletedCreditsFirstCycle() {
        return minimumCompletedCreditsFirstCycle;
    }

    public void setMinimumCompletedCreditsFirstCycle(Integer minimumCompletedCreditsFirstCycle) {
        this.minimumCompletedCreditsFirstCycle = minimumCompletedCreditsFirstCycle;
    }

    public Integer getMinimumCompletedCreditsSecondCycle() {
        return minimumCompletedCreditsSecondCycle;
    }

    public void setMinimumCompletedCreditsSecondCycle(Integer minimumCompletedCreditsSecondCycle) {
        this.minimumCompletedCreditsSecondCycle = minimumCompletedCreditsSecondCycle;
    }

    public boolean isProposalPeriodOpen() {
        if (getStartOfCandidacyPeriod() == null || getEndOfCandidacyPeriod() == null) {
            return false;
        }
        final long currentTime = Calendar.getInstance().getTimeInMillis();
        return currentTime >= getStartOfCandidacyPeriod().getTime() && currentTime <= getEndOfCandidacyPeriod().getTime();
    }
}