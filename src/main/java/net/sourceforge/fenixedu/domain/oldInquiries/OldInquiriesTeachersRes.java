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
public class OldInquiriesTeachersRes extends OldInquiriesTeachersRes_Base {

    public OldInquiriesTeachersRes() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        setExecutionPeriod(null);
        setDegree(null);
        setTeacher(null);
        deleteDomainObject();
    }

    @Deprecated
    public boolean hasTeacher() {
        return getTeacher() != null;
    }

    @Deprecated
    public boolean hasGepCourseName() {
        return getGepCourseName() != null;
    }

    @Deprecated
    public boolean hasTotalNumberAnswers() {
        return getTotalNumberAnswers() != null;
    }

    @Deprecated
    public boolean hasExecutionPeriod() {
        return getExecutionPeriod() != null;
    }

    @Deprecated
    public boolean hasClassTypeLong() {
        return getClassTypeLong() != null;
    }

    @Deprecated
    public boolean hasTeacherNumber() {
        return getTeacherNumber() != null;
    }

    @Deprecated
    public boolean hasTolerance3_9() {
        return getTolerance3_9() != null;
    }

    @Deprecated
    public boolean hasDepartmentCode() {
        return getDepartmentCode() != null;
    }

    @Deprecated
    public boolean hasDeviation3_3() {
        return getDeviation3_3() != null;
    }

    @Deprecated
    public boolean hasDeviation3_4() {
        return getDeviation3_4() != null;
    }

    @Deprecated
    public boolean hasTeacherName() {
        return getTeacherName() != null;
    }

    @Deprecated
    public boolean hasTolerance3_10() {
        return getTolerance3_10() != null;
    }

    @Deprecated
    public boolean hasTolerance3_4() {
        return getTolerance3_4() != null;
    }

    @Deprecated
    public boolean hasSemester() {
        return getSemester() != null;
    }

    @Deprecated
    public boolean hasTolerance3_11() {
        return getTolerance3_11() != null;
    }

    @Deprecated
    public boolean hasTolerance3_3() {
        return getTolerance3_3() != null;
    }

    @Deprecated
    public boolean hasTolerance3_6() {
        return getTolerance3_6() != null;
    }

    @Deprecated
    public boolean hasTolerance3_5() {
        return getTolerance3_5() != null;
    }

    @Deprecated
    public boolean hasTolerance3_8() {
        return getTolerance3_8() != null;
    }

    @Deprecated
    public boolean hasDeviation3_11() {
        return getDeviation3_11() != null;
    }

    @Deprecated
    public boolean hasTolerance3_7() {
        return getTolerance3_7() != null;
    }

    @Deprecated
    public boolean hasDeviation3_10() {
        return getDeviation3_10() != null;
    }

    @Deprecated
    public boolean hasDeviation3_9() {
        return getDeviation3_9() != null;
    }

    @Deprecated
    public boolean hasDeviation3_7() {
        return getDeviation3_7() != null;
    }

    @Deprecated
    public boolean hasDeviation3_8() {
        return getDeviation3_8() != null;
    }

    @Deprecated
    public boolean hasDeviation3_5() {
        return getDeviation3_5() != null;
    }

    @Deprecated
    public boolean hasAverage3_10() {
        return getAverage3_10() != null;
    }

    @Deprecated
    public boolean hasAverage3_9() {
        return getAverage3_9() != null;
    }

    @Deprecated
    public boolean hasDeviation3_6() {
        return getDeviation3_6() != null;
    }

    @Deprecated
    public boolean hasAverage3_11() {
        return getAverage3_11() != null;
    }

    @Deprecated
    public boolean hasAverage3_8() {
        return getAverage3_8() != null;
    }

    @Deprecated
    public boolean hasActive() {
        return getActive() != null;
    }

    @Deprecated
    public boolean hasAverage3_7() {
        return getAverage3_7() != null;
    }

    @Deprecated
    public boolean hasGepExecutionYear() {
        return getGepExecutionYear() != null;
    }

    @Deprecated
    public boolean hasNumAnswers3_9() {
        return getNumAnswers3_9() != null;
    }

    @Deprecated
    public boolean hasAverage3_6() {
        return getAverage3_6() != null;
    }

    @Deprecated
    public boolean hasNumAnswers3_8() {
        return getNumAnswers3_8() != null;
    }

    @Deprecated
    public boolean hasAverage3_5() {
        return getAverage3_5() != null;
    }

    @Deprecated
    public boolean hasNumAnswers3_7() {
        return getNumAnswers3_7() != null;
    }

    @Deprecated
    public boolean hasAverage3_4() {
        return getAverage3_4() != null;
    }

    @Deprecated
    public boolean hasNumAnswers3_6() {
        return getNumAnswers3_6() != null;
    }

    @Deprecated
    public boolean hasAverage3_3() {
        return getAverage3_3() != null;
    }

    @Deprecated
    public boolean hasNumAnswers3_5() {
        return getNumAnswers3_5() != null;
    }

    @Deprecated
    public boolean hasNumAnswers3_4() {
        return getNumAnswers3_4() != null;
    }

    @Deprecated
    public boolean hasNumAnswers3_3() {
        return getNumAnswers3_3() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasMail() {
        return getMail() != null;
    }

    @Deprecated
    public boolean hasNumAnswers3_11() {
        return getNumAnswers3_11() != null;
    }

    @Deprecated
    public boolean hasNumAnswers3_10() {
        return getNumAnswers3_10() != null;
    }

    @Deprecated
    public boolean hasGepCourseId() {
        return getGepCourseId() != null;
    }

    @Deprecated
    public boolean hasCurricularYear() {
        return getCurricularYear() != null;
    }

    @Deprecated
    public boolean hasInquiryId() {
        return getInquiryId() != null;
    }

    @Deprecated
    public boolean hasClassType() {
        return getClassType() != null;
    }

    @Deprecated
    public boolean hasCategory() {
        return getCategory() != null;
    }

    @Deprecated
    public boolean hasCourseCode() {
        return getCourseCode() != null;
    }

    @Deprecated
    public boolean hasDegree() {
        return getDegree() != null;
    }

}
