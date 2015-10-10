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

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.GradeScale;
import org.fenixedu.academic.domain.IEnrolment;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.Dismissal;
import org.fenixedu.academic.domain.studentCurriculum.ExternalEnrolment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Curriculum implements Serializable, ICurriculum {

    private static Logger logger = LoggerFactory.getLogger(Curriculum.class);

    static private final long serialVersionUID = -8365985725904139675L;

    public static interface CurricularYearCalculator {
        Integer curricularYear(Curriculum curriculum);

        Integer totalCurricularYears(Curriculum curriculum);

        BigDecimal approvedCredits(Curriculum curriculum);

        BigDecimal remainingCredits(Curriculum curriculum);
    }

    private static Supplier<CurricularYearCalculator> CURRICULAR_YEAR_CALCULATOR = () -> new CurricularYearCalculator() {
        private BigDecimal approvedCredits;

        private BigDecimal remainingCredits;

        private Integer curricularYear;

        private Integer totalCurricularYears;

        @Override
        public Integer curricularYear(Curriculum curriculum) {
            if (curricularYear == null) {
                doCalculus(curriculum);
            }
            return curricularYear;
        }

        @Override
        public Integer totalCurricularYears(Curriculum curriculum) {
            if (totalCurricularYears == null) {
                doCalculus(curriculum);
            }
            return totalCurricularYears;
        }

        @Override
        public BigDecimal approvedCredits(Curriculum curriculum) {
            if (approvedCredits == null) {
                doCalculus(curriculum);
            }
            return approvedCredits;
        }

        @Override
        public BigDecimal remainingCredits(Curriculum curriculum) {
            if (remainingCredits == null) {
                doCalculus(curriculum);
            }
            return remainingCredits;
        }

        private void doCalculus(Curriculum curriculum) {
            StudentCurricularPlan scp = curriculum.getStudentCurricularPlan();
            totalCurricularYears = scp == null ? 0 : scp.getDegreeCurricularPlan().getDurationInYears(getCycleType(curriculum));

            approvedCredits = BigDecimal.ZERO;
            for (final ICurriculumEntry entry : curriculum.getCurricularYearEntries()) {
                approvedCredits = approvedCredits.add(entry.getEctsCreditsForCurriculum());
            }
            accountForDirectIngressions(curriculum);
            if (approvedCredits.compareTo(BigDecimal.ZERO) == 0) {
                curricularYear = Integer.valueOf(1);
            } else {
                final BigDecimal ectsCreditsCurricularYear =
                        curriculum.getSumEctsCredits().add(BigDecimal.valueOf(24))
                                .divide(BigDecimal.valueOf(60), 2 * 2 + 1, RoundingMode.HALF_EVEN).add(BigDecimal.valueOf(1));
                curricularYear = Math.min(ectsCreditsCurricularYear.intValue(), totalCurricularYears.intValue());
            }

            remainingCredits = BigDecimal.ZERO;
            for (final ICurriculumEntry entry : curriculum.getCurricularYearEntries()) {
                if (entry instanceof Dismissal) {
                    final Dismissal dismissal = (Dismissal) entry;
                    if (dismissal.getCredits().isCredits() || dismissal.getCredits().isEquivalence()
                            || (dismissal.isCreditsDismissal() && !dismissal.getCredits().isSubstitution())) {
                        remainingCredits = remainingCredits.add(entry.getEctsCreditsForCurriculum());
                    }
                }
            }
        }

        private void accountForDirectIngressions(Curriculum curriculum) {
            if (getCycleType(curriculum) != null) {
                return;
            }
            if (!curriculum.getStudentCurricularPlan().getDegreeCurricularPlan().isBolonhaDegree()) {
                return;
            }
            //this is to prevent some oddly behavior spotted (e.g. student 57276)
            if (curriculum.getStudentCurricularPlan().getCycleCurriculumGroups().isEmpty()) {
                return;
            }
            CycleCurriculumGroup sgroup =
                    Collections.min(curriculum.getStudentCurricularPlan().getCycleCurriculumGroups(),
                            CycleCurriculumGroup.COMPARATOR_BY_CYCLE_TYPE_AND_ID);
            CycleType cycleIter = sgroup.getCycleType().getPrevious();
            while (cycleIter != null) {
                if (curriculum.getStudentCurricularPlan().getDegreeCurricularPlan().getCycleCourseGroup(cycleIter) != null) {
                    approvedCredits = approvedCredits.add(new BigDecimal(cycleIter.getEctsCredits()));
                }
                cycleIter = cycleIter.getPrevious();
            }
        }

        private CycleType getCycleType(Curriculum curriculum) {
            if (!curriculum.hasCurriculumModule() || !curriculum.isBolonha()) {
                return null;
            }

            final CurriculumModule module = curriculum.getCurriculumModule();
            final CycleType cycleType = module.isCycleCurriculumGroup() ? ((CycleCurriculumGroup) module).getCycleType() : null;
            return cycleType;
        }

    };

    public static void setCurricularYearCalculator(Supplier<CurricularYearCalculator> calculator) {
        if (calculator != null) {
            CURRICULAR_YEAR_CALCULATOR = calculator;
        } else {
            logger.error("Could not set curriculum year calculator strategy to null");
        }
    }

    public static interface CurriculumGradeCalculator {
        Grade rawGrade(Curriculum curriculum);

        Grade finalGrade(Curriculum curriculum);

        @Deprecated
        BigDecimal weigthedGradeSum(Curriculum curriculum);
    }

    private static Supplier<CurriculumGradeCalculator> CURRICULUM_GRADE_CALCULATOR = () -> new CurriculumGradeCalculator() {
        private BigDecimal sumPiCi;

        private BigDecimal sumPi;

        private Grade rawGrade;

        private Grade finalGrade;

        private void doCalculus(Curriculum curriculum) {
            sumPiCi = BigDecimal.ZERO;
            sumPi = BigDecimal.ZERO;
            countAverage(curriculum.averageEnrolmentRelatedEntries, curriculum.getAverageType());
            countAverage(curriculum.averageDismissalRelatedEntries, curriculum.getAverageType());
            BigDecimal avg = calculateAverage();
            rawGrade = Grade.createGrade(avg.setScale(2, RoundingMode.HALF_UP).toString(), GradeScale.TYPE20);
            finalGrade = Grade.createGrade(avg.setScale(0, RoundingMode.HALF_UP).toString(), GradeScale.TYPE20);
        }

        private void countAverage(final Set<ICurriculumEntry> entries, AverageType averageType) {
            for (final ICurriculumEntry entry : entries) {
                if (entry.getGrade().isNumeric()) {
                    final BigDecimal weigth = entry.getWeigthForCurriculum();

                    if (averageType == AverageType.WEIGHTED) {
                        sumPi = sumPi.add(weigth);
                        sumPiCi = sumPiCi.add(entry.getWeigthForCurriculum().multiply(entry.getGrade().getNumericValue()));
                    } else if (averageType == AverageType.SIMPLE) {
                        sumPi = sumPi.add(BigDecimal.ONE);
                        sumPiCi = sumPiCi.add(entry.getGrade().getNumericValue());
                    } else {
                        throw new DomainException("Curriculum.average.type.not.supported");
                    }
                }
            }
        }

        private BigDecimal calculateAverage() {
            return sumPi.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : sumPiCi.divide(sumPi, 2 * 2 + 1,
                    RoundingMode.HALF_UP);
        }

        @Override
        public Grade rawGrade(Curriculum curriculum) {
            if (rawGrade == null) {
                doCalculus(curriculum);
            }
            return rawGrade;
        }

        @Override
        public Grade finalGrade(Curriculum curriculum) {
            if (finalGrade == null) {
                doCalculus(curriculum);
            }
            return finalGrade;
        }

        @Override
        public BigDecimal weigthedGradeSum(Curriculum curriculum) {
            if (sumPiCi == null) {
                doCalculus(curriculum);
            }
            return sumPiCi;
        }
    };

    public static void setCurriculumGradeCalculator(Supplier<CurriculumGradeCalculator> calculator) {
        if (calculator != null) {
            CURRICULUM_GRADE_CALCULATOR = calculator;
        } else {
            logger.error("Could not set curriculum grade calculator strategy to null");
        }
    }

    private CurriculumModule curriculumModule;

    private Boolean bolonhaDegree;

    private final ExecutionYear executionYear;

    private final Set<ICurriculumEntry> averageEnrolmentRelatedEntries = new HashSet<ICurriculumEntry>();

    private final Set<ICurriculumEntry> averageDismissalRelatedEntries = new HashSet<ICurriculumEntry>();

    private final Set<ICurriculumEntry> curricularYearEntries = new HashSet<ICurriculumEntry>();

    private AverageType averageType = AverageType.WEIGHTED;

    private CurricularYearCalculator curricularYearCalculator = CURRICULAR_YEAR_CALCULATOR.get();

    private CurriculumGradeCalculator gradeCalculator = CURRICULUM_GRADE_CALCULATOR.get();

    static public Curriculum createEmpty(final ExecutionYear executionYear) {
        return Curriculum.createEmpty(null, executionYear);
    }

    static public Curriculum createEmpty(final CurriculumModule curriculumModule, final ExecutionYear executionYear) {
        return new Curriculum(curriculumModule, executionYear);
    }

    private Curriculum(final CurriculumModule curriculumModule, final ExecutionYear executionYear) {
        this.curriculumModule = curriculumModule;
        this.bolonhaDegree = curriculumModule == null ? null : curriculumModule.getStudentCurricularPlan().isBolonhaDegree();
        this.executionYear = executionYear;
    }

    public Curriculum(final CurriculumModule curriculumModule, final ExecutionYear executionYear,
            final Collection<ICurriculumEntry> averageEnrolmentRelatedEntries,
            final Collection<ICurriculumEntry> averageDismissalRelatedEntries,
            final Collection<ICurriculumEntry> curricularYearEntries) {
        this(curriculumModule, executionYear);

        addAverageEntries(this.averageEnrolmentRelatedEntries, averageEnrolmentRelatedEntries);
        addAverageEntries(this.averageDismissalRelatedEntries, averageDismissalRelatedEntries);
        addCurricularYearEntries(this.curricularYearEntries, curricularYearEntries);
    }

    public void add(final Curriculum curriculum) {
        if (!hasCurriculumModule()) {
            this.curriculumModule = curriculum.getCurriculumModule();
            this.bolonhaDegree = curriculum.isBolonha();
        }

        addAverageEntries(averageEnrolmentRelatedEntries, curriculum.getEnrolmentRelatedEntries());
        addAverageEntries(averageDismissalRelatedEntries, curriculum.getDismissalRelatedEntries());
        addCurricularYearEntries(curricularYearEntries, curriculum.getCurricularYearEntries());

        curricularYearCalculator = CURRICULAR_YEAR_CALCULATOR.get();
        gradeCalculator = CURRICULUM_GRADE_CALCULATOR.get();
    }

    private void addAverageEntries(final Set<ICurriculumEntry> entries, final Collection<ICurriculumEntry> newEntries) {
        for (final ICurriculumEntry newEntry : newEntries) {
            if (!isAlreadyAverageEntry(newEntry)) {
                add(entries, newEntry);
            }
        }
    }

    private boolean isAlreadyAverageEntry(final ICurriculumEntry newEntry) {
        return averageEnrolmentRelatedEntries.contains(newEntry) || averageDismissalRelatedEntries.contains(newEntry);
    }

    private void addCurricularYearEntries(final Set<ICurriculumEntry> entries, final Collection<ICurriculumEntry> newEntries) {
        for (final ICurriculumEntry newEntry : newEntries) {
            add(entries, newEntry);
        }
    }

    private void add(final Set<ICurriculumEntry> entries, final ICurriculumEntry newEntry) {
        if (isBolonha() || !isAlreadyCurricularYearEntry(newEntry)) {
            entries.add(newEntry);
        }
    }

    private boolean isAlreadyCurricularYearEntry(final ICurriculumEntry newEntry) {
        if (newEntry instanceof IEnrolment) {
            return isCurricularYearEntryAsEnrolmentOrAsSourceEnrolment((IEnrolment) newEntry);
        } else if (newEntry instanceof Dismissal) {
            return isCurricularYearEntryAsSimilarDismissal((Dismissal) newEntry);
        }

        return false;
    }

    private boolean isCurricularYearEntryAsEnrolmentOrAsSourceEnrolment(final IEnrolment newIEnrolment) {
        for (final ICurriculumEntry entry : curricularYearEntries) {
            if (entry instanceof Dismissal && ((Dismissal) entry).hasSourceIEnrolments(newIEnrolment)) {
                return true;
            } else if (entry == newIEnrolment) {
                return true;
            }
        }

        return false;
    }

    private boolean isCurricularYearEntryAsSimilarDismissal(final Dismissal newDismissal) {
        for (final ICurriculumEntry entry : curricularYearEntries) {
            if (entry instanceof Dismissal && !newDismissal.isCreditsDismissal() && newDismissal.isSimilar((Dismissal) entry)) {
                return true;
            }
        }

        return false;
    }

    public CurriculumModule getCurriculumModule() {
        return curriculumModule;
    }

    public boolean hasCurriculumModule() {
        return getCurriculumModule() != null;
    }

    public Boolean isBolonha() {
        return bolonhaDegree;
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    @Override
    public StudentCurricularPlan getStudentCurricularPlan() {
        return hasCurriculumModule() ? getCurriculumModule().getStudentCurricularPlan() : null;
    }

    public boolean hasAverageEntry() {
        return hasCurriculumModule() && !getCurriculumEntries().isEmpty();
    }

    @Override
    public boolean isEmpty() {
        return !hasCurriculumModule() || (getCurriculumEntries().isEmpty() && curricularYearEntries.isEmpty());
    }

    @Override
    public Collection<ICurriculumEntry> getCurriculumEntries() {
        final Collection<ICurriculumEntry> result = new HashSet<ICurriculumEntry>();

        result.addAll(averageEnrolmentRelatedEntries);
        result.addAll(averageDismissalRelatedEntries);

        return result;
    }

    @Override
    public boolean hasAnyExternalApprovedEnrolment() {
        for (final ICurriculumEntry entry : averageDismissalRelatedEntries) {
            if (entry instanceof ExternalEnrolment) {
                return true;
            }
        }

        return false;
    }

    public Set<ICurriculumEntry> getEnrolmentRelatedEntries() {
        return averageEnrolmentRelatedEntries;
    }

    public Set<ICurriculumEntry> getDismissalRelatedEntries() {
        return averageDismissalRelatedEntries;
    }

    @Override
    public Set<ICurriculumEntry> getCurricularYearEntries() {
        return curricularYearEntries;
    }

    @Deprecated
    public BigDecimal getWeigthedGradeSum() {
        return gradeCalculator.weigthedGradeSum(this);
    }

    @Override
    public Grade getRawGrade() {
        return gradeCalculator.rawGrade(this);
    }

    @Override
    public Grade getFinalGrade() {
        return gradeCalculator.finalGrade(this);
    }

    @Override
    public BigDecimal getSumEctsCredits() {
        return curricularYearCalculator.approvedCredits(this);
    }

    @Override
    public Integer getCurricularYear() {
        return curricularYearCalculator.curricularYear(this);
    }

    @Override
    public BigDecimal getRemainingCredits() {
        return curricularYearCalculator.remainingCredits(this);
    }

    @Deprecated
    @Override
    public void setAverageType(AverageType averageType) {
        this.averageType = averageType;
        gradeCalculator = CURRICULUM_GRADE_CALCULATOR.get();
    }

    @Deprecated
    public AverageType getAverageType() {
        return averageType;
    }

    @Override
    public Integer getTotalCurricularYears() {
        return curricularYearCalculator.totalCurricularYears(this);
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();

        result.append("\n[CURRICULUM]");
        if (hasCurriculumModule()) {
            result.append("\n[CURRICULUM_MODULE][ID] " + getCurriculumModule().getExternalId() + "\t[NAME]"
                    + getCurriculumModule().getName().getContent());
            result.append("\n[BOLONHA] " + isBolonha().toString());
        } else {
            result.append("\n[NO CURRICULUM_MODULE]");
        }
        result.append("\n[SUM ENTRIES] " + (averageEnrolmentRelatedEntries.size() + averageDismissalRelatedEntries.size()));
        result.append("\n[RAW GRADE] " + getRawGrade().getValue());
        result.append("\n[FINAL GRADE] " + getFinalGrade().getValue());
        result.append("\n[SUM ECTS CREDITS] " + getSumEctsCredits().toString());
        result.append("\n[CURRICULAR YEAR] " + getCurricularYear());
        result.append("\n[REMAINING CREDITS] " + getRemainingCredits().toString());
        result.append("\n[TOTAL CURRICULAR YEARS] " + getTotalCurricularYears());

        result.append("\n[AVERAGE ENROLMENT ENTRIES]");
        for (final ICurriculumEntry entry : averageEnrolmentRelatedEntries) {
            result.append("\n[ENTRY] [NAME]" + entry.getName().getContent() + "\t[CREATION_DATE]"
                    + entry.getCreationDateDateTime() + "\t[GRADE] " + entry.getGrade().toString() + "\t[WEIGHT] "
                    + entry.getWeigthForCurriculum() + "\t[ECTS] " + entry.getEctsCreditsForCurriculum() + "\t[CLASS_NAME] "
                    + entry.getClass().getSimpleName());
        }

        result.append("\n[AVERAGE DISMISSAL RELATED ENTRIES]");
        for (final ICurriculumEntry entry : averageDismissalRelatedEntries) {
            result.append("\n[ENTRY] [NAME]" + entry.getName().getContent() + "\t[CREATION_DATE]"
                    + entry.getCreationDateDateTime() + "\t[GRADE] " + entry.getGrade().toString() + "\t[WEIGHT] "
                    + entry.getWeigthForCurriculum() + "\t[ECTS] " + entry.getEctsCreditsForCurriculum() + "\t[CLASS_NAME] "
                    + entry.getClass().getSimpleName());
        }

        result.append("\n[CURRICULAR YEAR ENTRIES]");
        for (final ICurriculumEntry entry : curricularYearEntries) {
            result.append("\n[ENTRY] [NAME]" + entry.getName().getContent() + "\t[CREATION_DATE]"
                    + entry.getCreationDateDateTime() + "\t[ECTS] " + entry.getEctsCreditsForCurriculum() + "\t[CLASS_NAME] "
                    + entry.getClass().getSimpleName());
        }

        return result.toString();
    }

    /**
     * This is used to remove an entry from average and curricular year entries
     * in order to do calculus without it
     * 
     * @param entryToRemove
     */
    public void removeFromAllCurriculumEntries(final ICurriculumEntry entryToRemove) {
        averageEnrolmentRelatedEntries.remove(entryToRemove);
        averageDismissalRelatedEntries.remove(entryToRemove);
        curricularYearEntries.remove(entryToRemove);
    }
}
