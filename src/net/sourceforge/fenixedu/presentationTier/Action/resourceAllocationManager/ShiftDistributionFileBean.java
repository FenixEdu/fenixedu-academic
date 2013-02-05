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
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.StringNormalizer;

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

    @Service
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
