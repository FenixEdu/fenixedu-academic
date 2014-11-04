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
package net.sourceforge.fenixedu.domain.student.curriculum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;

public class Curriculum implements Serializable, ICurriculum {

    static private final long serialVersionUID = -8365985725904139675L;

    private CurriculumModule curriculumModule;

    private Boolean bolonhaDegree;

    private final ExecutionYear executionYear;

    private final Set<ICurriculumEntry> averageEnrolmentRelatedEntries = new HashSet<ICurriculumEntry>();

    private final Set<ICurriculumEntry> averageDismissalRelatedEntries = new HashSet<ICurriculumEntry>();

    private final Set<ICurriculumEntry> curricularYearEntries = new HashSet<ICurriculumEntry>();

    private BigDecimal sumPiCi;

    private BigDecimal sumPi;

    static final protected int SCALE = 2;

    static final protected RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    private AverageType averageType = AverageType.WEIGHTED;

    private BigDecimal average;

    private BigDecimal sumEctsCredits;

    private Integer curricularYear;

    private boolean forceCalculus;

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

        forceCalculus = true;
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

    @Override
    public BigDecimal getSumPiCi() {
        if (sumPiCi == null || forceCalculus) {
            doCalculus();
            forceCalculus = false;
        }

        return sumPiCi;
    }

    @Override
    public BigDecimal getSumPi() {
        if (sumPi == null || forceCalculus) {
            doCalculus();
            forceCalculus = false;
        }

        return sumPi;
    }

    @Override
    public BigDecimal getAverage() {
        if (average == null || forceCalculus) {
            doCalculus();
            forceCalculus = false;
        }

        return average.setScale(SCALE, ROUNDING_MODE);
    }

    @Override
    public Integer getRoundedAverage() {
        return getRoundedAverage(getAverage());
    }

    static public Integer getRoundedAverage(final BigDecimal average) {
        return average.setScale(0, RoundingMode.HALF_UP).intValue();
    }

    @Override
    public BigDecimal getSumEctsCredits() {
        if (sumEctsCredits == null || forceCalculus) {
            doCalculus();
            forceCalculus = false;
        }

        return sumEctsCredits;
    }

    @Override
    public Integer getCurricularYear() {
        if (curricularYear == null || forceCalculus) {
            doCalculus();
            forceCalculus = false;
        }

        return curricularYear;
    }

    @Override
    public BigDecimal getRemainingCredits() {
        BigDecimal result = BigDecimal.ZERO;

        for (final ICurriculumEntry entry : curricularYearEntries) {
            if (entry instanceof Dismissal) {
                final Dismissal dismissal = (Dismissal) entry;
                if (dismissal.getCredits().isCredits() || dismissal.getCredits().isEquivalence()
                        || (dismissal.isCreditsDismissal() && !dismissal.getCredits().isSubstitution())) {
                    result = result.add(entry.getEctsCreditsForCurriculum());
                }
            }
        }

        return result;
    }

    private void doCalculus() {
        sumPiCi = BigDecimal.ZERO;
        sumPi = BigDecimal.ZERO;
        countAverage(averageEnrolmentRelatedEntries);
        countAverage(averageDismissalRelatedEntries);
        average = calculateAverage();

        sumEctsCredits = BigDecimal.ZERO;
        countCurricularYear(curricularYearEntries);
        accountForDirectIngressions();
        curricularYear = calculateCurricularYear();
    }

    @Override
    public void setAverageType(AverageType averageType) {
        this.averageType = averageType;
        forceCalculus = true;
    }

    private void countAverage(final Set<ICurriculumEntry> entries) {
        for (final ICurriculumEntry entry : entries) {
            if (entry.getGrade().isNumeric()) {
                final BigDecimal weigth = entry.getWeigthForCurriculum();

                if (averageType == AverageType.WEIGHTED) {
                    sumPi = sumPi.add(weigth);
                    sumPiCi = sumPiCi.add(entry.getWeigthTimesGrade());
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
        return sumPi.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : sumPiCi.divide(sumPi, SCALE * SCALE + 1, ROUNDING_MODE);
    }

    private void countCurricularYear(final Set<ICurriculumEntry> entries) {
        for (final ICurriculumEntry entry : entries) {
            sumEctsCredits = sumEctsCredits.add(entry.getEctsCreditsForCurriculum());
        }
    }

    private void accountForDirectIngressions() {
        if (getCycleType() != null) {
            return;
        }
        if (!getStudentCurricularPlan().getDegreeCurricularPlan().isBolonhaDegree()) {
            return;
        }
        //this is to prevent some oddly behavior spotted (e.g. student 57276)
        if (getStudentCurricularPlan().getCycleCurriculumGroups().isEmpty()) {
            return;
        }
        CycleCurriculumGroup sgroup =
                Collections.min(getStudentCurricularPlan().getCycleCurriculumGroups(),
                        CycleCurriculumGroup.COMPARATOR_BY_CYCLE_TYPE_AND_ID);
        CycleType cycleIter = sgroup.getCycleType().getPrevious();
        while (cycleIter != null) {
            if (getStudentCurricularPlan().getDegreeCurricularPlan().getCycleCourseGroup(cycleIter) != null) {
                sumEctsCredits = sumEctsCredits.add(new BigDecimal(cycleIter.getEctsCredits()));
            }
            cycleIter = cycleIter.getPrevious();
        }
    }

    private Integer calculateCurricularYear() {
        if (sumEctsCredits.compareTo(BigDecimal.ZERO) == 0) {
            return Integer.valueOf(1);
        }

        final BigDecimal ectsCreditsCurricularYear =
                sumEctsCredits.add(BigDecimal.valueOf(24))
                        .divide(BigDecimal.valueOf(60), SCALE * SCALE + 1, RoundingMode.HALF_EVEN).add(BigDecimal.valueOf(1));
        return Math.min(ectsCreditsCurricularYear.intValue(), getTotalCurricularYears().intValue());
    }

    @Override
    public Integer getTotalCurricularYears() {
        final StudentCurricularPlan plan = getStudentCurricularPlan();
        if (plan == null) {
            return Integer.valueOf(0);
        }

        return plan.getDegreeType().getYears(getCycleType());
    }

    private CycleType getCycleType() {
        if (!hasCurriculumModule() || !isBolonha()) {
            return null;
        }

        final CurriculumModule module = getCurriculumModule();
        final CycleType cycleType = module.isCycleCurriculumGroup() ? ((CycleCurriculumGroup) module).getCycleType() : null;
        return cycleType;
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
        result.append("\n[SUM PiCi] " + getSumPiCi().toString());
        result.append("\n[SUM Pi] " + getSumPi().toString());
        result.append("\n[AVERAGE] " + getAverage());
        result.append("\n[ROUNDED_AVERAGE] " + getRoundedAverage());
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
