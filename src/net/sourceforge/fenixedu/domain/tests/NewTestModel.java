package net.sourceforge.fenixedu.domain.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class NewTestModel extends NewTestModel_Base {

	public NewTestModel() {
		super();

		this.setBag(new NewModelGroup());
		this.setCreated(new DateTime());
	}

	public NewTestModel(Teacher creator, String name, Double scale) {
		this();

		this.setCreator(creator);
		this.setName(name);
		this.setScale(scale);
	}

	public int getBagQuestionCount() {
		return this.getBag().getChildRestrictionsCount();
	}

	public void delete() {
		this.getBag().deleteAsBag();

		this.removeCreator();

		super.delete();
	}

	public Grade getGrade() {
		return new Grade(this.getValue(), this.getScale());
	}

	@Override
	public NewTestModel getTestModel() {
		return this;
	}

	public List<NewModelRestriction> getAtomicQuestionRestrictions() {
		List<NewModelRestriction> atomicRestrictions = new ArrayList<NewModelRestriction>();

		for (NewModelRestriction modelRestriction : this.getBag().getChildRestrictions()) {
			NewQuestion question = modelRestriction.getQuestion();
			if (!question.isComposite()) {
				atomicRestrictions.add(modelRestriction);
			}
		}

		return atomicRestrictions;
	}

	public List<NewModelRestriction> getQuestionGroupRestrictions() {
		List<NewModelRestriction> groupRestrictions = new ArrayList<NewModelRestriction>();

		for (NewModelRestriction modelRestriction : this.getBag().getChildRestrictions()) {
			NewQuestion question = modelRestriction.getQuestion();
			if (question.isComposite()) {
				groupRestrictions.add(modelRestriction);
			}
		}

		return groupRestrictions;
	}

	public void selectAtomicQuestionRestrictions(List<NewModelRestriction> modelRestrictions,
			NewModelGroup destinationGroup, Double value) {
		int i = destinationGroup.getChildRestrictionsCount();
		for (NewModelRestriction modelRestriction : modelRestrictions) {
			modelRestriction.setParentGroup(destinationGroup);
			modelRestriction.setPosition(++i);
			modelRestriction.setValue(value);
		}

		this.getBag().resortChildRestrictions();
	}

	public void selectQuestionGroupRestriction(NewModelRestriction modelRestriction,
			NewModelGroup destinationGroup, Integer count, Double value) {
		modelRestriction.setParentGroup(destinationGroup);
		modelRestriction.setPosition(destinationGroup.getChildRestrictionsCount());
		modelRestriction.setCount(count);
		modelRestriction.setValue(value);

		this.getBag().resortChildRestrictions();
	}

	public void unselectRestriction(NewModelRestriction atomicRestriction) {
		NewModelGroup parentGroup = atomicRestriction.getParentGroup();

		if (this.getBag() == null) {
			atomicRestriction.delete();
			return;
		}

		atomicRestriction.setParentGroup(this.getBag());
		atomicRestriction.setPosition(this.getBag().getChildRestrictionsCount() + 1);

		parentGroup.resortChildRestrictions();
	}

	public boolean isOversized() {
		return this.getValue() > this.getScale();
	}

	public NewTestGroup generateTests(String name, ExecutionCourse executionCourse, Integer variations,
			DateTime finalDate) {
		if (!isCanGenerateTests()) {
			throw new DomainException("cannot.generate.tests");
		}

		NewTestGroup testGroup = new NewTestGroup(name, this.getCreator(), executionCourse, finalDate);

		int maxCombinations = getMaxCombinations();
		int temptativeCombinations = variations == null ? 1 : variations;

		if (temptativeCombinations > maxCombinations) {
			temptativeCombinations = maxCombinations;
		}

		for (int i = 0; i < temptativeCombinations; i++) {
			HashMap<Object, Object> transformationMap = new HashMap<Object, Object>();
			NewTestElement testElement = generateSection(this, testGroup, null, transformationMap);
			testElement.cleanTransformation(transformationMap);
		}

		return testGroup;
	}

	public boolean isCanGenerateTests() {
		return getMaxCombinations() > 0 && isGradeComplete();
	}

	public boolean isGradeComplete() {
		Stack<NewModelGroup> stack = new Stack<NewModelGroup>();
		stack.add(this);

		while (stack.size() > 0) {
			NewModelGroup modelGroup = stack.pop();
			for (NewModelRestriction modelRestriction : modelGroup.getChildRestrictions()) {
				if (modelRestriction.isComposite()) {
					stack.add((NewModelGroup) modelRestriction);
				} else if (modelRestriction.getValue() == null) {
					return false;
				}
			}
		}

		return true;
	}

	private boolean isHasAnyAtomicRestriction() {
		Stack<NewModelGroup> stack = new Stack<NewModelGroup>();
		stack.add(this);

		while (stack.size() > 0) {
			NewModelGroup modelGroup = stack.pop();
			for (NewModelRestriction modelRestriction : modelGroup.getChildRestrictions()) {
				if (modelRestriction.isComposite()) {
					stack.add((NewModelGroup) modelRestriction);
				} else {
					return true;
				}
			}
		}

		return false;
	}

	private NewTestElement generateSection(NewModelGroup modelGroup, NewTestGroup testGroup,
			NewSection parentSection, HashMap<Object, Object> transformationMap) {
		NewSection section = createSection(modelGroup, testGroup, parentSection);

		for (NewModelRestriction modelRestriction : modelGroup.getOrderedChildRestrictions()) {
			if (modelRestriction.isComposite()) {
				generateSection((NewModelGroup) modelRestriction, null, section, transformationMap);
			} else {
				List<NewQuestion> choosenQuestions = modelRestriction.getQuestion().provide(
						modelRestriction.getCount());

				double value = modelRestriction.getValue() / modelRestriction.getCount();

				for (NewQuestion choosenQuestion : choosenQuestions) {
					transform(choosenQuestion, section, transformationMap, value);
				}
			}
		}

		return section;
	}

	private NewSection createSection(NewTestElement copyFrom, NewTestGroup testGroup,
			NewSection parentSection) {
		NewSection section;
		if (testGroup != null) {
			section = new NewTest(testGroup, this.getScale());
		} else {
			section = new NewSection(parentSection);
		}

		for (NewPresentationMaterial material : copyFrom.getOrderedPresentationMaterials()) {
			NewPresentationMaterial presentationMaterialCopy = material.copy();
			presentationMaterialCopy.setPosition(material.getPosition());
			section.addPresentationMaterials(presentationMaterialCopy);
		}

		return section;
	}

	private NewTestElement transform(NewQuestion choosenQuestion, NewSection parentSection,
			HashMap<Object, Object> transformationMap, Double value) {
		Grade grade = null;

		if (choosenQuestion.isBelongsToAllGroup() && choosenQuestion.belongsToGradeCompleteGroup()) {
			double factor = choosenQuestion.getGrade().getValue()
					/ choosenQuestion.getGrade().getScale();
			grade = new Grade(value * factor, this.getScale());
		} else if (choosenQuestion.isBelongsToAllGroup()) {
			grade = new Grade(
					value / choosenQuestion.getParentAllGroup().getChildAtomicQuestionsCount(), this
							.getScale());
		} else {
			grade = new Grade(value, this.getScale());
		}

		if (choosenQuestion instanceof NewAtomicQuestion) {
			NewAtomicQuestion questionCopy = (NewAtomicQuestion) choosenQuestion.copy(transformationMap);
			questionCopy.setSection(parentSection);
			questionCopy.setSectionPosition(parentSection.getTestElementsCount());

			questionCopy.setGrade(grade);

			return questionCopy;
		}

		NewQuestionGroup questionGroup = (NewQuestionGroup) choosenQuestion;

		NewSection section = createSection(choosenQuestion, null, parentSection);

		section.setPreCondition(questionGroup.getPreCondition());

		for (NewQuestion question : questionGroup.getOrderedChildAtomicQuestions()) {
			NewTestElement testElement = transform(question, section, transformationMap, grade
					.getValue());
			testElement.setSectionPosition(question.getPosition(questionGroup));
		}

		return section;
	}

	public int getMaxCombinations() {
		if (!isHasAnyAtomicRestriction()) {
			return 0;
		}

		int maxCombinations = 1;

		Stack<NewModelGroup> stack = new Stack<NewModelGroup>();

		stack.add(this);

		while (stack.size() > 0) {
			NewModelGroup modelGroup = stack.pop();

			for (NewModelRestriction modelRestriction : modelGroup.getChildRestrictions()) {
				if (modelRestriction.isComposite()) {
					stack.add((NewModelGroup) modelRestriction);
				} else if (modelRestriction.getQuestion().isComposite()) {
					NewQuestionGroup questionGroup = (NewQuestionGroup) modelRestriction.getQuestion();

					int questionsCount = questionGroup.getAllChildAtomicQuestionsCount();
					int selectedCount = modelRestriction.getCount() > questionGroup
							.getAllChildAtomicQuestionsCount() ? questionGroup
							.getAllChildAtomicQuestionsCount() : modelRestriction.getCount();
					int arrangements = arrangements(questionsCount, selectedCount);

					if (questionsCount != 0) {
						maxCombinations *= arrangements;
					}
				}
			}
		}

		return maxCombinations;
	}

	private int combinations(int n, int r) {
		return factorial(n) / (factorial(n - r) * factorial(r));
	}

	private int arrangements(int n, int r) {
		return factorial(n) / factorial(n - r);
	}

	private int factorial(int n) {
		return n <= 1 ? 1 : n * factorial(n - 1);
	}

	@Override
	public NewModelRestriction findModelRestriction(NewQuestion question) {
		NewModelRestriction modelRestriction = this.getBag().findModelRestriction(question);
		if (modelRestriction != null) {
			return modelRestriction;
		}

		return super.findModelRestriction(question);
	}

}
