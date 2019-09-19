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
package org.fenixedu.academic.domain.student.curriculum;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Set;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public interface ICurriculumEntry {

    static final Comparator<ICurriculumEntry> COMPARATOR_BY_ID = new Comparator<ICurriculumEntry>() {
        @Override
        public int compare(ICurriculumEntry o1, ICurriculumEntry o2) {
            return o1.getExternalId().compareTo(o2.getExternalId());
        }
    };

    static final Comparator<ICurriculumEntry> COMPARATOR_BY_EXECUTION_PERIOD = new Comparator<ICurriculumEntry>() {
        @Override
        public int compare(ICurriculumEntry o1, ICurriculumEntry o2) {
            final ExecutionInterval e1 = o1.getExecutionInterval();
            final ExecutionInterval e2 = o2.getExecutionInterval();

            if (e1 == null && e2 == null) {
                return 0;
            } else if (e1 == null) {
                return -1;
            } else if (e2 == null) {
                return 1;
            }

            return e1.compareTo(e2);
        }
    };

    static final public Comparator<ICurriculumEntry> COMPARATOR_BY_EXECUTION_PERIOD_AND_ID = new Comparator<ICurriculumEntry>() {
        @Override
        final public int compare(ICurriculumEntry o1, ICurriculumEntry o2) {
            final ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(ICurriculumEntry.COMPARATOR_BY_EXECUTION_PERIOD);
            comparatorChain.addComparator(ICurriculumEntry.COMPARATOR_BY_ID);

            return comparatorChain.compare(o1, o2);
        }
    };

    static final Comparator<ICurriculumEntry> COMPARATOR_BY_REVERSE_EXECUTION_PERIOD_AND_NAME =
            new Comparator<ICurriculumEntry>() {
                @Override
                public int compare(ICurriculumEntry o1, ICurriculumEntry o2) {
                    int result = COMPARATOR_BY_EXECUTION_PERIOD.compare(o2, o1);
                    return (result == 0) ? o1.getName().compareTo(o2.getName()) : result;
                }
            };

    static final Comparator<ICurriculumEntry> COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME = new Comparator<ICurriculumEntry>() {
        @Override
        public int compare(ICurriculumEntry o1, ICurriculumEntry o2) {
            int result = COMPARATOR_BY_EXECUTION_PERIOD.compare(o1, o2);
            return (result == 0) ? o1.getName().compareTo(o2.getName()) : result;
        }
    };

    static final Comparator<ICurriculumEntry> COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID =
            new Comparator<ICurriculumEntry>() {
                @Override
                final public int compare(ICurriculumEntry o1, ICurriculumEntry o2) {
                    final ComparatorChain comparatorChain = new ComparatorChain();
                    comparatorChain.addComparator(ICurriculumEntry.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME);
                    comparatorChain.addComparator(ICurriculumEntry.COMPARATOR_BY_ID);

                    return comparatorChain.compare(o1, o2);
                }
            };

    static final Comparator<ICurriculumEntry> COMPARATOR_BY_REVERSE_EXECUTION_PERIOD_AND_NAME_AND_ID =
            new Comparator<ICurriculumEntry>() {
                @Override
                final public int compare(ICurriculumEntry o1, ICurriculumEntry o2) {
                    final ComparatorChain comparatorChain = new ComparatorChain();
                    comparatorChain.addComparator(ICurriculumEntry.COMPARATOR_BY_REVERSE_EXECUTION_PERIOD_AND_NAME);
                    comparatorChain.addComparator(ICurriculumEntry.COMPARATOR_BY_ID);

                    return comparatorChain.compare(o1, o2);
                }
            };

    static final Comparator<ICurriculumEntry> COMPARATOR_BY_EXECUTION_YEAR = new Comparator<ICurriculumEntry>() {
        @Override
        public int compare(ICurriculumEntry o1, ICurriculumEntry o2) {
            final ExecutionYear e1 = o1.getExecutionYear();
            final ExecutionYear e2 = o2.getExecutionYear();

            if (e1 == null && e2 == null) {
                return 0;
            } else if (e1 == null) {
                return -1;
            } else if (e2 == null) {
                return 1;
            }

            return e1.compareTo(e2);
        }
    };

    static final Comparator<ICurriculumEntry> COMPARATOR_BY_EXECUTION_YEAR_AND_NAME = new Comparator<ICurriculumEntry>() {
        @Override
        public int compare(ICurriculumEntry o1, ICurriculumEntry o2) {
            int result = COMPARATOR_BY_EXECUTION_YEAR.compare(o1, o2);
            return (result == 0) ? o1.getName().compareTo(o2.getName()) : result;
        }
    };

    static final Comparator<ICurriculumEntry> COMPARATOR_BY_EXECUTION_YEAR_AND_NAME_AND_ID = new Comparator<ICurriculumEntry>() {
        @Override
        final public int compare(ICurriculumEntry o1, ICurriculumEntry o2) {
            final ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(ICurriculumEntry.COMPARATOR_BY_EXECUTION_YEAR_AND_NAME);
            comparatorChain.addComparator(ICurriculumEntry.COMPARATOR_BY_ID);

            return comparatorChain.compare(o1, o2);
        }
    };

    static final Comparator<ICurriculumEntry> COMPARATOR_BY_APPROVEMENT_DATE = new Comparator<ICurriculumEntry>() {
        @Override
        public int compare(ICurriculumEntry curriculumEntry1, ICurriculumEntry curriculumEntry2) {
            final YearMonthDay localDate1 = curriculumEntry1.getApprovementDate();
            final YearMonthDay localDate2 = curriculumEntry2.getApprovementDate();
            final int c = localDate1.compareTo(localDate2);
            return c == 0 ? COMPARATOR_BY_ID.compare(curriculumEntry1, curriculumEntry2) : c;
        }
    };

    String getExternalId();

    String getCode();

    LocalizedString getName();

    LocalizedString getPresentationName();

    Grade getGrade();

    String getGradeValue();

    Set<CurriculumLine> getCurriculumLinesForCurriculum(StudentCurricularPlan studentCurricularPlan);

    BigDecimal getWeigthForCurriculum();

    BigDecimal getEctsCreditsForCurriculum();

    ExecutionInterval getExecutionInterval();

    /**
     * @deprecated use {@link #getExecutionInterval()}
     */
    @Deprecated
    ExecutionInterval getExecutionPeriod();

    @Deprecated
    boolean hasExecutionPeriod();

    ExecutionYear getExecutionYear();

    DateTime getCreationDateDateTime();

    YearMonthDay getApprovementDate();

}
