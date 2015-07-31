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

import java.util.Comparator;

import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.joda.time.DateTime;

public interface IEnrolment extends ICurriculumEntry {

    static final public Comparator<IEnrolment> COMPARATOR_BY_APPROVEMENT_DATE = new Comparator<IEnrolment>() {
        @Override
        public int compare(IEnrolment o1, IEnrolment o2) {
            if (o1.getApprovementDate() == null && o2.getApprovementDate() == null) {
                return 0;
            }
            if (o1.getApprovementDate() == null) {
                return -1;
            }
            if (o2.getApprovementDate() == null) {
                return 1;
            }
            return o1.getApprovementDate().compareTo(o2.getApprovementDate());
        }
    };

    @Override
    String getExternalId();

    /**
     * @deprecated
     *             Use {@link ICurriculumEntry#getGrade()} instead.
     *             Code should not assume that final grades are integers.
     *             This method will be removed on next major release
     *
     * 
     */
    @Deprecated
    Integer getFinalGrade();

    String getDescription();

    Double getEctsCredits();

    Grade getEctsGrade(StudentCurricularPlan scp, DateTime processingDate);

    boolean isAnual();

    String getEnrolmentTypeName();

    Double getWeigth();

    Unit getAcademicUnit();

    boolean isApproved();

    boolean isEnroled();

    boolean isExternalEnrolment();

    boolean isEnrolment();

    /**
     * Obtains the last valid thesis for this enrolment. The returned thesis may
     * not be evaluated. You can used {@link Thesis#isFinalThesis()} and {@link Thesis#isFinalAndApprovedThesis()} to distinguish
     * between a thesis
     * currently in evaluation and a final thesis.
     * 
     * @return the last valid thesis for this enrolment
     */
    Thesis getThesis();

    void delete();
}
