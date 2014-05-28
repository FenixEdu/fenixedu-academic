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

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class CoordinatorExecutionDegreeCoursesReport extends CoordinatorExecutionDegreeCoursesReport_Base {

    public CoordinatorExecutionDegreeCoursesReport() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    private CoordinatorExecutionDegreeCoursesReport(final ExecutionDegree executionDegree,
            final ExecutionInterval executionInterval) {
        this();

        if (executionDegree.getExecutionDegreeCoursesReports(executionInterval) != null) {
            throw new DomainException(
                    "error.CoordinatorExecutionDegreeCoursesReport.alreadyExistsForDegreeAndIntervaAndCoordinator");
        }

        final Coordinator coordinator = executionDegree.getCoordinatorByTeacher(AccessControl.getPerson());
        if (coordinator == null || !coordinator.isResponsible()) {
            throw new DomainException("error.CoordinatorExecutionDegreeCoursesReport.isNotResponsible");
        }
        setCoordinator(coordinator);
        setExecutionDegree(executionDegree);
        setExecutionInterval(executionInterval);
    }

    @Atomic
    public static CoordinatorExecutionDegreeCoursesReport makeNew(final ExecutionDegree executionDegree,
            final ExecutionInterval executionInterval) {
        return new CoordinatorExecutionDegreeCoursesReport(executionDegree, executionInterval);
    }

    public boolean isEmpty() {
        return StringUtils.isEmpty(getStepsToImproveResults())
                && StringUtils.isEmpty(getBolonhaProcessImplementationReflection()) && StringUtils.isEmpty(getGlobalComment())
                && StringUtils.isEmpty(getTeachingBestPracticesDevelopedByTeachers());
    }

    @Override
    public void setBolonhaProcessImplementationReflection(String bolonhaProcessImplementationReflection) {
        setLastEditionDate(new DateTime());
        super.setBolonhaProcessImplementationReflection(bolonhaProcessImplementationReflection);
    }

    @Override
    public void setGlobalComment(String globalComment) {
        setLastEditionDate(new DateTime());
        super.setGlobalComment(globalComment);
    }

    @Override
    public void setStepsToImproveResults(String stepsToImproveResults) {
        setLastEditionDate(new DateTime());
        super.setStepsToImproveResults(stepsToImproveResults);
    }

    @Override
    public void setTeachingBestPracticesDevelopedByTeachers(String teachingBestPracticesDevelopedByTeachers) {
        setLastEditionDate(new DateTime());
        super.setTeachingBestPracticesDevelopedByTeachers(teachingBestPracticesDevelopedByTeachers);
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasBolonhaProcessImplementationReflection() {
        return getBolonhaProcessImplementationReflection() != null;
    }

    @Deprecated
    public boolean hasExecutionInterval() {
        return getExecutionInterval() != null;
    }

    @Deprecated
    public boolean hasLastEditionDate() {
        return getLastEditionDate() != null;
    }

    @Deprecated
    public boolean hasStepsToImproveResults() {
        return getStepsToImproveResults() != null;
    }

    @Deprecated
    public boolean hasExecutionDegree() {
        return getExecutionDegree() != null;
    }

    @Deprecated
    public boolean hasGlobalComment() {
        return getGlobalComment() != null;
    }

    @Deprecated
    public boolean hasTeachingBestPracticesDevelopedByTeachers() {
        return getTeachingBestPracticesDevelopedByTeachers() != null;
    }

    @Deprecated
    public boolean hasCoordinator() {
        return getCoordinator() != null;
    }

}
