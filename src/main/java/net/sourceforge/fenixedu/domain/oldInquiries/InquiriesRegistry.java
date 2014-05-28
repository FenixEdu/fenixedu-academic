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
 * Created on Mar 18, 2005
 * 
 */
package net.sourceforge.fenixedu.domain.oldInquiries;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistryState;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.lang.NullArgumentException;
import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author Jo�o Fialho & Rita Ferreira
 * 
 */
public class InquiriesRegistry extends InquiriesRegistry_Base {

    public InquiriesRegistry() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public InquiriesRegistry(ExecutionCourse executionCourse, ExecutionSemester executionSemester, Registration registration) {
        this();
        checkParameters(executionCourse, executionSemester, registration);
        this.setExecutionCourse(executionCourse);
        this.setExecutionPeriod(executionSemester);
        this.setStudent(registration);
        this.setState(executionCourse.getAvailableForInquiries() ? InquiriesRegistryState.ANSWER_LATER : InquiriesRegistryState.UNAVAILABLE);
    }

    public InquiriesRegistry(ExecutionCourse executionCourse, CurricularCourse curricularCourse,
            ExecutionSemester executionSemester, Registration registration) {
        this(executionCourse, executionSemester, registration);
        if (curricularCourse == null) {
            throw new NullArgumentException("CurricularCourse should not be null!");
        }
        this.setCurricularCourse(curricularCourse);
    }

    private void checkParameters(ExecutionCourse executionCourse, ExecutionSemester executionSemester, Registration registration) {
        if ((executionCourse == null) || (executionSemester == null) || (registration == null)) {
            throw new NullArgumentException("The executionCourse, executionPeriod and student should not be null!");
        }

        for (final InquiriesRegistry inquiriesRegistry : registration.getAssociatedInquiriesRegistries()) {
            if (inquiriesRegistry.getExecutionCourse() == executionCourse
                    && inquiriesRegistry.getExecutionPeriod() == executionSemester) {
                throw new DomainException(
                        "Already exists an Inquiries Registry in this Registration for the given period and course!");
            }
        }
    }

    public boolean isAnswered() {
        return getState() == InquiriesRegistryState.ANSWERED;
    }

    public boolean isNotAnswered() {
        return getState() == InquiriesRegistryState.NOT_ANSWERED;
    }

    public boolean isToAnswerLater() {
        return !isNotAvailableToInquiries() && (getState() == null || getState() == InquiriesRegistryState.ANSWER_LATER);
    }

    public boolean isNotAvailableToInquiries() {
        return !getExecutionCourse().getAvailableForInquiries();
    }

    public boolean isOpenToAnswer() {

        if (isAnswered() || isNotAnswered()) {
            return false;
        }

        if (isCreatedAfterWeeklySpentHoursSubmission()) {
            return false;
        }

        //TODO remove this classes at the end of the new QUC model implementation
        //	if (getStudent().getStudent().isWeeklySpentHoursSubmittedForOpenInquiriesResponsePeriod() && isNotAvailableToInquiries()) {
        //	    return false;
        //	}

        return true;
    }

    public boolean isCreatedAfterWeeklySpentHoursSubmission() {
        //TODO remove this classes at the end of the new QUC model implementation
        //	return getStudent().getStudent().isWeeklySpentHoursSubmittedForOpenInquiriesResponsePeriod()
        //		&& (getWeeklyHoursSpentPercentage() == null || getStudyDaysSpentInExamsSeason() == null);
        return false;
    }

    public ExecutionDegree getExecutionDegree() {

        final StudentCurricularPlan studentCurricularPlan = getStudent().getActiveStudentCurricularPlan();
        if (studentCurricularPlan != null) {
            final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
            return degreeCurricularPlan.getExecutionDegreeByYear(getExecutionPeriod().getExecutionYear());
        }

        DegreeCurricularPlan lastDegreeCurricularPlan = getStudent().getLastDegreeCurricularPlan();
        if (lastDegreeCurricularPlan != null) {
            return lastDegreeCurricularPlan.getExecutionDegreeByYear(getExecutionPeriod().getExecutionYear());
        }

        return null;
    }

    public InquiriesStudentExecutionPeriod getInquiriesStudentExecutionPeriod() {
        return null;
        //TODO remove this classes at the end of the new QUC model implementation
        //	return getStudent().getStudent().getInquiriesStudentExecutionPeriod(getExecutionPeriod());
    }

    @Deprecated
    public boolean hasStudent() {
        return getStudent() != null;
    }

    @Deprecated
    public boolean hasExecutionCourse() {
        return getExecutionCourse() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasStudyDaysSpentInExamsSeason() {
        return getStudyDaysSpentInExamsSeason() != null;
    }

    @Deprecated
    public boolean hasState() {
        return getState() != null;
    }

    @Deprecated
    public boolean hasExecutionPeriod() {
        return getExecutionPeriod() != null;
    }

    @Deprecated
    public boolean hasWeeklyHoursSpentPercentage() {
        return getWeeklyHoursSpentPercentage() != null;
    }

    @Deprecated
    public boolean hasCurricularCourse() {
        return getCurricularCourse() != null;
    }

}
