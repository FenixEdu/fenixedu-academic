package net.sourceforge.fenixedu.domain.teacher.evaluation;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class FacultyEvaluationProcessBean implements Serializable {

	static private final long serialVersionUID = 1L;

	private MultiLanguageString title;
	private DateTime autoEvaluationIntervalStart;
	private DateTime autoEvaluationIntervalEnd;
	private DateTime evaluationIntervalStart;
	private DateTime evaluationIntervalEnd;
	private boolean allowNoEval;
	private String suffix;
	private byte[] evaluatorListFileContent;
	private FacultyEvaluationProcess facultyEvaluationProcess;

	public FacultyEvaluationProcessBean() {
	}

	public FacultyEvaluationProcessBean(final FacultyEvaluationProcess facultyEvaluationProcess) {
		this();
		setTitle(facultyEvaluationProcess.getTitle());
		setAutoEvaluationIntervalStart(facultyEvaluationProcess.getAutoEvaluationInterval().getStart());
		setAutoEvaluationIntervalEnd(facultyEvaluationProcess.getAutoEvaluationInterval().getEnd());
		setEvaluationIntervalStart(facultyEvaluationProcess.getEvaluationInterval().getStart());
		setEvaluationIntervalEnd(facultyEvaluationProcess.getEvaluationInterval().getEnd());
		setAllowNoEval(facultyEvaluationProcess.getAllowNoEval());
		setSuffix(facultyEvaluationProcess.getSuffix());
		setFacultyEvaluationProcess(facultyEvaluationProcess);
	}

	public MultiLanguageString getTitle() {
		return title;
	}

	public void setTitle(MultiLanguageString title) {
		this.title = title;
	}

	public DateTime getAutoEvaluationIntervalStart() {
		return autoEvaluationIntervalStart;
	}

	public void setAutoEvaluationIntervalStart(DateTime autoEvaluationIntervalStart) {
		this.autoEvaluationIntervalStart = autoEvaluationIntervalStart;
	}

	public DateTime getAutoEvaluationIntervalEnd() {
		return autoEvaluationIntervalEnd;
	}

	public void setAutoEvaluationIntervalEnd(DateTime autoEvaluationIntervalEnd) {
		this.autoEvaluationIntervalEnd = autoEvaluationIntervalEnd;
	}

	public DateTime getEvaluationIntervalStart() {
		return evaluationIntervalStart;
	}

	public void setEvaluationIntervalStart(DateTime evaluationIntervalStart) {
		this.evaluationIntervalStart = evaluationIntervalStart;
	}

	public DateTime getEvaluationIntervalEnd() {
		return evaluationIntervalEnd;
	}

	public void setEvaluationIntervalEnd(DateTime evaluationIntervalEnd) {
		this.evaluationIntervalEnd = evaluationIntervalEnd;
	}

	public boolean isAllowNoEval() {
		return allowNoEval;
	}

	public void setAllowNoEval(boolean allowNoEval) {
		this.allowNoEval = allowNoEval;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getSuffix() {
		return suffix;
	}

	public byte[] getEvaluatorListFileContent() {
		return evaluatorListFileContent;
	}

	public void setEvaluatorListFileContent(byte[] evaluatorListFileContent) {
		this.evaluatorListFileContent = evaluatorListFileContent;
	}

	public FacultyEvaluationProcess getFacultyEvaluationProcess() {
		return facultyEvaluationProcess;
	}

	public void setFacultyEvaluationProcess(FacultyEvaluationProcess facultyEvaluationProcess) {
		this.facultyEvaluationProcess = facultyEvaluationProcess;
	}

	public Interval getAutoEvaluationInterval() {
		return new Interval(getAutoEvaluationIntervalStart(), getAutoEvaluationIntervalEnd());
	}

	public Interval getEvaluationInterval() {
		return new Interval(getEvaluationIntervalStart(), getEvaluationIntervalEnd());
	}

	@Service
	public FacultyEvaluationProcess create() {
		FacultyEvaluationProcess process =
				new FacultyEvaluationProcess(getTitle(), getAutoEvaluationInterval(), getEvaluationInterval());
		process.setAllowNoEval(isAllowNoEval());
		process.setSuffix(getSuffix());
		return process;
	}

	@Service
	public void edit() {
		facultyEvaluationProcess.setTitle(getTitle());
		facultyEvaluationProcess.setAutoEvaluationInterval(getAutoEvaluationInterval());
		facultyEvaluationProcess.setEvaluationInterval(getEvaluationInterval());
		facultyEvaluationProcess.setAllowNoEval(isAllowNoEval());
		facultyEvaluationProcess.setSuffix(getSuffix());
	}

}
