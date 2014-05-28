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
 * Created on Nov 17, 2004
 *
 */
package net.sourceforge.fenixedu.domain.oldInquiries;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author Jo�o Fialho & Rita Ferreira
 * 
 */
public class OldInquiriesCoursesRes extends OldInquiriesCoursesRes_Base {

    public OldInquiriesCoursesRes() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        setExecutionPeriod(null);
        setDegree(null);
        deleteDomainObject();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasGepCourseName() {
        return getGepCourseName() != null;
    }

    @Deprecated
    public boolean hasExecutionPeriod() {
        return getExecutionPeriod() != null;
    }

    @Deprecated
    public boolean hasNumberEvaluated() {
        return getNumberEvaluated() != null;
    }

    @Deprecated
    public boolean hasNumberEnrollments() {
        return getNumberEnrollments() != null;
    }

    @Deprecated
    public boolean hasNumAnswers2_5_text() {
        return getNumAnswers2_5_text() != null;
    }

    @Deprecated
    public boolean hasNumberAnswers() {
        return getNumberAnswers() != null;
    }

    @Deprecated
    public boolean hasFirstEnrollment() {
        return getFirstEnrollment() != null;
    }

    @Deprecated
    public boolean hasDeviation2_8() {
        return getDeviation2_8() != null;
    }

    @Deprecated
    public boolean hasGepCourseId() {
        return getGepCourseId() != null;
    }

    @Deprecated
    public boolean hasDeviation2_6() {
        return getDeviation2_6() != null;
    }

    @Deprecated
    public boolean hasSemester() {
        return getSemester() != null;
    }

    @Deprecated
    public boolean hasCurricularYear() {
        return getCurricularYear() != null;
    }

    @Deprecated
    public boolean hasNumAnswers2_3() {
        return getNumAnswers2_3() != null;
    }

    @Deprecated
    public boolean hasNumAnswers2_2() {
        return getNumAnswers2_2() != null;
    }

    @Deprecated
    public boolean hasNumberApproved() {
        return getNumberApproved() != null;
    }

    @Deprecated
    public boolean hasNumAnswers2_7() {
        return getNumAnswers2_7() != null;
    }

    @Deprecated
    public boolean hasAverage2_5() {
        return getAverage2_5() != null;
    }

    @Deprecated
    public boolean hasNumAnswers2_6() {
        return getNumAnswers2_6() != null;
    }

    @Deprecated
    public boolean hasAverage2_4() {
        return getAverage2_4() != null;
    }

    @Deprecated
    public boolean hasAverage2_3() {
        return getAverage2_3() != null;
    }

    @Deprecated
    public boolean hasAverage2_2() {
        return getAverage2_2() != null;
    }

    @Deprecated
    public boolean hasNumAnswers2_4() {
        return getNumAnswers2_4() != null;
    }

    @Deprecated
    public boolean hasDeviation2_4() {
        return getDeviation2_4() != null;
    }

    @Deprecated
    public boolean hasInquiryId() {
        return getInquiryId() != null;
    }

    @Deprecated
    public boolean hasAverage2_8() {
        return getAverage2_8() != null;
    }

    @Deprecated
    public boolean hasDeviation2_5() {
        return getDeviation2_5() != null;
    }

    @Deprecated
    public boolean hasDeviation2_2() {
        return getDeviation2_2() != null;
    }

    @Deprecated
    public boolean hasAverage2_7() {
        return getAverage2_7() != null;
    }

    @Deprecated
    public boolean hasNumAnswers2_8() {
        return getNumAnswers2_8() != null;
    }

    @Deprecated
    public boolean hasDeviation2_3() {
        return getDeviation2_3() != null;
    }

    @Deprecated
    public boolean hasAverage2_6() {
        return getAverage2_6() != null;
    }

    @Deprecated
    public boolean hasGepExecutionYear() {
        return getGepExecutionYear() != null;
    }

    @Deprecated
    public boolean hasNumAnswers2_5_number() {
        return getNumAnswers2_5_number() != null;
    }

    @Deprecated
    public boolean hasCourseCode() {
        return getCourseCode() != null;
    }

    @Deprecated
    public boolean hasDegree() {
        return getDegree() != null;
    }

    @Deprecated
    public boolean hasTolerance2_6() {
        return getTolerance2_6() != null;
    }

    @Deprecated
    public boolean hasTolerance2_8() {
        return getTolerance2_8() != null;
    }

    @Deprecated
    public boolean hasTolerance2_3() {
        return getTolerance2_3() != null;
    }

    @Deprecated
    public boolean hasRepresentationQuota() {
        return getRepresentationQuota() != null;
    }

    @Deprecated
    public boolean hasTolerance2_2() {
        return getTolerance2_2() != null;
    }

    @Deprecated
    public boolean hasTolerance2_5() {
        return getTolerance2_5() != null;
    }

    @Deprecated
    public boolean hasTolerance2_4() {
        return getTolerance2_4() != null;
    }

}
