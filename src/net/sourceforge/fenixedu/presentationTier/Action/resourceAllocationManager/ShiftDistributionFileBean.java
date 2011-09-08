package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Shift;
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
