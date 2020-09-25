/**
 * Copyright © 2002 Instituto Superior Técnico
 * <p>
 * This file is part of FenixEdu Academic.
 * <p>
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.resourceAllocationManager;

import edu.emory.mathcs.backport.java.util.Collections;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.candidacy.degree.ShiftDistribution;
import org.fenixedu.academic.domain.candidacy.degree.ShiftDistributionEntry;
import org.fenixedu.academic.dto.GenericPair;
import org.fenixedu.commons.StringNormalizer;
import pt.ist.fenixframework.Atomic;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ShiftDistributionFileBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private transient InputStream inputStream;
    private String filename;
    private boolean firstPhase;
    private Map<Shift, List<GenericPair<DegreeCurricularPlan, Integer>>> distribution;
    private Map<DegreeCurricularPlan, List<Integer>> abstractStudentNumbers;

    public ShiftDistributionFileBean() {
        setFirstPhase(true);
    }

    @Atomic
    protected void writeDistribution() {
        final ExecutionYear executionYear = ExecutionSemester.readActualExecutionSemester().getExecutionYear();
        final ShiftDistribution shiftDistribution =
                executionYear.getShiftDistribution() != null ? executionYear.getShiftDistribution() : executionYear
                        .createShiftDistribution();

        for (final Entry<Shift, List<GenericPair<DegreeCurricularPlan, Integer>>> entry : getDistribution().entrySet()) {
            for (final GenericPair<DegreeCurricularPlan, Integer> pair : entry.getValue()) {
                new ShiftDistributionEntry(shiftDistribution, pair.getLeft().getExecutionDegreeByYear(executionYear),
                        entry.getKey(), pair.getRight());
            }
        }

        applyShiftDistributionRestrictions(shiftDistribution);
    }

    private void applyShiftDistributionRestrictions(final ShiftDistribution shiftDistribution) {
        if (!Shift.RESTRICT_STUDENTS_TO_ODD_OR_EVEN_WEEKS) {
            return;
        }

        final SortedMap<Integer, Collection<ShiftDistributionEntry>> odd = new TreeMap<>();
        final SortedMap<Integer, Collection<ShiftDistributionEntry>> even = new TreeMap<>();
        final SortedMap<Integer, Collection<ShiftDistributionEntry>> inconsistent = new TreeMap<>();
        shiftDistribution.getShiftDistributionEntriesSet().stream()
                .map(entry -> entry.getAbstractStudentNumber())
                .distinct()
                .forEach(number -> {
                    final boolean isEven = shiftDistribution.getEntriesByStudentNumber(number)
                            .map(entry -> entry.getShift())
                            .filter(shift -> shift.needToCheckShiftType())
                            .allMatch(shift -> shift.isEven());
                    final boolean isOdd = shiftDistribution.getEntriesByStudentNumber(number)
                            .map(entry -> entry.getShift())
                            .filter(shift -> shift.needToCheckShiftType())
                            .allMatch(shift -> shift.isOdd());
                    final List<ShiftDistributionEntry> entries = shiftDistribution.getEntriesByStudentNumber(number)
                            .collect(Collectors.toList());
                    if (isEven && !isOdd) {
                        even.put(number, entries);
                    } else if (isOdd && !isEven) {
                        odd.put(number, entries);
                    } else {
                        inconsistent.put(number, entries);
                    }
                });
        final int min = shiftDistribution.getShiftDistributionEntriesSet().stream()
                .mapToInt(entry -> entry.getAbstractStudentNumber())
                .min().orElse(1);
        for (int number = min; !odd.isEmpty() || !even.isEmpty() || !inconsistent.isEmpty(); number++) {
            final boolean isOdd = number % 2 != 0;
            final Collection<ShiftDistributionEntry> entries;
            if (isOdd && !odd.isEmpty()) {
                entries = consume(odd);
            } else if (!isOdd && !even.isEmpty()) {
                entries = consume(even);
            } else {
                entries = consume(inconsistent);
            }
            final Integer newNumber = number;
            entries.forEach(entry -> {
                    entry.setAbstractStudentNumber(newNumber);
            });
        }
    }

    private Collection<ShiftDistributionEntry> consume(final SortedMap<Integer, Collection<ShiftDistributionEntry>> map) {
        if (map.isEmpty()) {
            return Collections.emptySet();
        }
        final Integer key = map.firstKey();
        final Collection<ShiftDistributionEntry> entries = map.get(key);
        map.remove(key);
        return entries;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = StringNormalizer.normalize(filename);
    }

    public void setFirstPhase(boolean firstPhase) {
        this.firstPhase = firstPhase;
    }

    public boolean isFirstPhase() {
        return firstPhase;
    }

    public void setDistribution(Map<Shift, List<GenericPair<DegreeCurricularPlan, Integer>>> distribution) {
        this.distribution = distribution;
    }

    public Map<Shift, List<GenericPair<DegreeCurricularPlan, Integer>>> getDistribution() {
        return distribution;
    }

    public void setAbstractStudentNumbers(Map<DegreeCurricularPlan, List<Integer>> abstractStudentNumbers) {
        this.abstractStudentNumbers = abstractStudentNumbers;
    }

    public Map<DegreeCurricularPlan, List<Integer>> getAbstractStudentNumbers() {
        return abstractStudentNumbers;
    }

    public int getPhaseNumber() {
        if (isFirstPhase()) {
            return 1;
        }
        return 2;
    }
}
