package net.sourceforge.fenixedu.domain.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class NewSection extends NewSection_Base {

	public NewSection() {
		super();
	}

	public NewSection(NewSection parentSection) {
		this();

		this.setSection(parentSection);
		this.setSectionPosition(parentSection.getTestElementsCount());
	}

	@Override
	public NewTestElement copy(HashMap<Object, Object> transformationMap) {
		throw new IllegalArgumentException("class.not.clonable");
	}

	@Override
	public void cleanTransformation(HashMap<Object, Object> transformationMap) {
		if (this.getPreCondition() != null) {
			this.setPreCondition(this.getPreCondition().transform(transformationMap));
		}

		for (NewTestElement testElement : this.getTestElements()) {
			testElement.cleanTransformation(transformationMap);
		}
	}

	public List<NewTestElement> getOrderedTestElements() {
		List<NewTestElement> testElements = new ArrayList<NewTestElement>(getTestElements());

		Collections.sort(testElements, POSITION_COMPARATOR);

		return testElements;
	}

	public List<NewTestElement> getVisibleOrderedTestElements(Person person) {
		List<NewTestElement> testElements = this.getOrderedTestElements();
		Iterator<NewTestElement> iterator = testElements.iterator();
		while (iterator.hasNext()) {
			NewTestElement testElement = iterator.next();
			if (!testElement.isVisible(person)) {
				iterator.remove();
			}
		}

		return testElements;
	}

	public List<NewTestElement> getVisibleOrderedTestElements() {
		return this.getVisibleOrderedTestElements(this.getPerson());
	}

	public Person getPerson() {
		return AccessControl.getUserView().getPerson();
	}

	public List<NewTestElement> getCorrectableOrderedTestElements(Person person) {
		List<NewTestElement> testElements = this.getOrderedTestElements();
		Iterator<NewTestElement> iterator = testElements.iterator();
		while (iterator.hasNext()) {
			NewTestElement testElement = iterator.next();
			if (!testElement.isVisible(person) || !testElement.isCorrectable(person)
					|| !testElement.isAnswered(person)) {
				iterator.remove();
			}
		}

		return testElements;
	}

	public int getCorrectableOrderedTestElementsCount(Person person) {
		return this.getCorrectableOrderedTestElements(person).size();
	}

	protected static class SectionPositionComparator implements Comparator<NewTestElement> {
		public int compare(NewTestElement o1, NewTestElement o2) {
			return o1.getSectionPosition().compareTo(o2.getSectionPosition());
		}
	}

	public static final SectionPositionComparator POSITION_COMPARATOR = new SectionPositionComparator();

	@Override
	public void delete() {
		for (; this.hasAnyTestElements(); this.getTestElements().get(0).delete())
			;

		super.delete();
	}

	public boolean isShowAllElements() {
		for (NewTestElement testElement : this.getTestElements()) {
			if (testElement instanceof NewAtomicQuestion) {
				NewAtomicQuestion atomicQuestion = (NewAtomicQuestion) testElement;
				if (atomicQuestion.getPreCondition() != null
						&& !atomicQuestion.getPreCondition().evaluate(atomicQuestion, null)) {
					return false;
				}
			} else {
				NewSection section = (NewSection) testElement;
				if (section.getPreCondition() != null && !section.getPreCondition().evaluate(null, null)) {
					return false;
				}
			}
		}

		return true;
	}

	public int getUnansweredQuestionsCount(Person person) {
		int values = 0;
		for (NewTestElement testElement : this.getTestElements()) {
			if (testElement instanceof NewAtomicQuestion) {
				NewAtomicQuestion question = (NewAtomicQuestion) testElement;
				if (question.evaluatePreCondition(person) && !question.isAnswered()
						&& !question.isGivenUp()) {
					values++;
				}
			} else {
				values += ((NewSection) testElement).getUnansweredQuestionsCount();
			}
		}
		return values;
	}

	public int getUnansweredQuestionsCount() {
		return this.getUnansweredQuestionsCount(AccessControl.getUserView().getPerson());
	}

	public int getGivenUpQuestionsCount(Person person) {
		int values = 0;
		for (NewTestElement testElement : this.getTestElements()) {
			if (testElement instanceof NewAtomicQuestion) {
				NewAtomicQuestion question = (NewAtomicQuestion) testElement;
				if (question.evaluatePreCondition(person) && question.isGivenUp()) {
					values++;
				}
			} else {
				values += ((NewSection) testElement).getGivenUpQuestionsCount();
			}
		}
		return values;
	}

	public int getGivenUpQuestionsCount() {
		return this.getGivenUpQuestionsCount(AccessControl.getUserView().getPerson());
	}

	public int getVisibleQuestionsCount(Person person) {
		int values = 0;
		for (NewTestElement testElement : this.getTestElements()) {
			if (testElement instanceof NewAtomicQuestion) {
				NewQuestion question = (NewQuestion) testElement;
				if (question.getPreCondition() == null
						|| question.getPreCondition().evaluate(question, person)) {
					values++;
				}
			} else {
				values += ((NewSection) testElement).getVisibleQuestionsCount();
			}
		}
		return values;
	}

	public int getVisibleQuestionsCount() {
		return this.getVisibleQuestionsCount(AccessControl.getUserView().getPerson());
	}

	public double getVisibleQuestionsValuesSum(Person person) {
		double values = 0;
		for (NewTestElement testElement : this.getTestElements()) {
			if (testElement instanceof NewQuestion) {
				NewQuestion question = (NewQuestion) testElement;
				if (question.getPreCondition() == null
						|| question.getPreCondition().evaluate(question, person)) {
					values += question.getGrade().getValue();
				}
			} else {
				values += ((NewSection) testElement).getVisibleQuestionsValuesSum();
			}
		}
		return values;
	}

	public double getVisibleQuestionsValuesSum() {
		return this.getVisibleQuestionsValuesSum(AccessControl.getUserView().getPerson());
	}

	public int getQuestionCount() {
		int count = 0;
		for (NewTestElement testElement : this.getTestElements()) {
			if (testElement instanceof NewAtomicQuestion) {
				count++;
			} else {
				count += ((NewSection) testElement).getQuestionCount();
			}
		}
		return count;
	}

	public boolean evaluatePreCondition(Person person) {
		if (this.getPreCondition() == null) {
			return true;
		}

		return this.getPreCondition().evaluate(null, person);
	}

	@Override
	public boolean isVisible(Person person) {
		return this.evaluatePreCondition(person);
	}

	@Override
	public boolean isCorrectable(Person person) {
		return this.isVisible() && (this.getCorrectableOrderedTestElementsCount(person) > 0);
	}
	
	@Override
	public int getAllUncorrectedQuestionsCount(Person person) {
		int count = 0;
		for(NewTestElement testElement : this.getTestElements()) {
			if(testElement.isCorrectable(person)) {
				count += testElement.getAllUncorrectedQuestionsCount(person);
			}
		}
		return count;
	}
	
	@Override
	public void publishGrades() {
		for(NewTestElement testElement : this.getTestElements()) {
			testElement.publishGrades();
		}
	}
	
	@Override
	public Grade getFinalGrade() {
		return this.getFinalGrade(this.getPerson());
	}
	
	@Override
	public Grade getFinalGrade(Person person) {
		Grade grade = new Grade(0, this.getTest().getScale());
		
		for(NewTestElement testElement : this.getVisibleOrderedTestElements(person)) {
			grade = grade.add(testElement.getFinalGrade(person));
		}
		
		return grade;
	}

}
