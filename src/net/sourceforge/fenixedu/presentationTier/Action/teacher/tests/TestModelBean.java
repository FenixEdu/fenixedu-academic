package net.sourceforge.fenixedu.presentationTier.Action.teacher.tests;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.tests.NewModelGroup;
import net.sourceforge.fenixedu.domain.tests.NewModelRestriction;
import net.sourceforge.fenixedu.domain.tests.NewTestModel;

import org.joda.time.DateTime;

public class TestModelBean implements Serializable {

	private DomainReference<NewModelGroup> modelGroup;

	private DomainReference<NewTestModel> testModel;

	String name;

	Integer count;

	Double value;

	private List<NewModelRestriction> selectedAtomicQuestionRestrictions;

	private DomainReference<ExecutionCourse> executionCourse;

	private boolean useVariations;

	private boolean useFinalDate;

	private Integer variations;

	private DateTime finalDate;

	public TestModelBean() {
		super();

		reset();
	}

	public TestModelBean(NewTestModel testModel) {
		this();

		this.setTestModel(testModel);
	}

	public TestModelBean(NewTestModel testModel, String name) {
		this();

		this.setTestModel(testModel);
		this.setName(name);
	}

	public NewModelGroup getModelGroup() {
		return modelGroup.getObject();
	}

	public void setModelGroup(NewModelGroup modelGroup) {
		this.modelGroup = new DomainReference<NewModelGroup>(modelGroup);
	}

	public NewTestModel getTestModel() {
		return testModel.getObject();
	}

	public void setTestModel(NewTestModel testModel) {
		this.testModel = new DomainReference<NewTestModel>(testModel);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<NewModelRestriction> getSelectedAtomicQuestionRestrictions() {
		return selectedAtomicQuestionRestrictions;
	}

	public void setSelectedAtomicQuestionRestrictions(
			List<NewModelRestriction> selectedAtomicQuestionRestrictions) {
		this.selectedAtomicQuestionRestrictions = selectedAtomicQuestionRestrictions;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public ExecutionCourse getExecutionCourse() {
		return executionCourse.getObject();
	}

	public void setExecutionCourse(ExecutionCourse executionCourse) {
		this.executionCourse = new DomainReference<ExecutionCourse>(executionCourse);
	}

	public DateTime getFinalDate() {
		return finalDate;
	}

	public void setFinalDate(DateTime finalDate) {
		this.finalDate = finalDate;
	}

	public boolean isUseFinalDate() {
		return useFinalDate;
	}

	public void setUseFinalDate(boolean useFinalDate) {
		this.useFinalDate = useFinalDate;
	}

	public boolean isUseVariations() {
		return useVariations;
	}

	public void setUseVariations(boolean useVariations) {
		this.useVariations = useVariations;
	}

	public Integer getVariations() {
		return variations;
	}

	public void setVariations(Integer variations) {
		this.variations = variations;
	}

	public void reset() {
		this.setModelGroup(null);
		this.setExecutionCourse(null);
		selectedAtomicQuestionRestrictions = new ArrayList<NewModelRestriction>();
	}

}
