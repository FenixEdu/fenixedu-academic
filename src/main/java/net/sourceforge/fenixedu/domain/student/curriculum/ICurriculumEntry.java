package net.sourceforge.fenixedu.domain.student.curriculum;

import java.math.BigDecimal;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Grade;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

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
            final ExecutionSemester e1 = o1.getExecutionPeriod();
            final ExecutionSemester e2 = o2.getExecutionPeriod();

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

    MultiLanguageString getName();

    MultiLanguageString getPresentationName();

    Grade getGrade();

    String getGradeValue();

    BigDecimal getWeigthForCurriculum();

    BigDecimal getWeigthTimesGrade();

    BigDecimal getEctsCreditsForCurriculum();

    ExecutionSemester getExecutionPeriod();

    boolean hasExecutionPeriod();

    ExecutionYear getExecutionYear();

    DateTime getCreationDateDateTime();

    YearMonthDay getApprovementDate();

}
