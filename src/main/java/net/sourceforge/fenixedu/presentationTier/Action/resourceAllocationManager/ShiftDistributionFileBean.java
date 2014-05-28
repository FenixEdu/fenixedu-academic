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
package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.candidacy.degree.ShiftDistribution;
import net.sourceforge.fenixedu.domain.candidacy.degree.ShiftDistributionEntry;

import org.fenixedu.commons.StringNormalizer;

import pt.ist.fenixframework.Atomic;

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
                executionYear.hasShiftDistribution() ? executionYear.getShiftDistribution() : executionYear
                        .createShiftDistribution();

        for (final Entry<Shift, List<GenericPair<DegreeCurricularPlan, Integer>>> entry : getDistribution().entrySet()) {
            for (final GenericPair<DegreeCurricularPlan, Integer> pair : entry.getValue()) {
                new ShiftDistributionEntry(shiftDistribution, pair.getLeft().getExecutionDegreeByYear(executionYear),
                        entry.getKey(), pair.getRight());
            }
        }
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
