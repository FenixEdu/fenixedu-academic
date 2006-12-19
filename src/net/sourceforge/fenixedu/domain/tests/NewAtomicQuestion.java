package net.sourceforge.fenixedu.domain.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.tests.answers.NullAnswer;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public abstract class NewAtomicQuestion extends NewAtomicQuestion_Base {

	public NewAtomicQuestion() {
		super();
	}

	public NewAtomicQuestion(NewQuestionGroup parentQuestionGroup) {
		this();

		init(parentQuestionGroup);
	}

	public Integer getNewPosition(NewQuestionGroup parentQuestionGroup) {
		return parentQuestionGroup.getChildAtomicQuestionsCount();
	}

	@Override
	public List<NewQuestionGroup> getAssociableParents() {
		if (!this.getParentQuestionGroups().get(0).isComposite()) {
			return new ArrayList<NewQuestionGroup>();
		}

		return super.getAssociableParents();
	}

	@Override
	public boolean isComposite() {
		return false;
	}

	public void resortCorrectors() {
		int i = 1;
		for (NewCorrector corrector : this.getOrderedCorrectors()) {
			corrector.setPosition(i++);
		}
	}

	public List<NewCorrector> getOrderedCorrectors() {
		List<NewCorrector> correctors = new ArrayList<NewCorrector>(this.getCorrectors());

		Collections.sort(correctors, Positionable.POSITION_COMPARATOR);

		return correctors;
	}

	@Override
	public void delete() {
		for (; this.hasAnyCorrectors(); this.getCorrectors().get(0).delete())
			;

		for (; this.hasAnyAnswers(); this.getAnswers().get(0).delete())
			;

		super.delete();
	}

	@Override
	protected void initCopy(NewTestElement testElement, HashMap<Object, Object> transformationMap) {
		super.initCopy(testElement, transformationMap);

		NewAtomicQuestion atomicQuestion = (NewAtomicQuestion) testElement;

		atomicQuestion.setValidator(this.getValidator());

		for (NewCorrector corrector : this.getCorrectors()) {
			NewCorrector correctorCopy = corrector.copy();

			transformationMap.put(corrector, correctorCopy);

			correctorCopy.setPosition(corrector.getPosition());
			correctorCopy.setAtomicQuestion(atomicQuestion);
		}
	}

	@Override
	public List<NewQuestion> provide(int count) {
		this.setTimesUsed(this.getTimesUsed() + 1);

		ArrayList<NewQuestion> choosenQuestions = new ArrayList<NewQuestion>();
		choosenQuestions.add(this);

		return choosenQuestions;
	}

	@Override
	public void cleanTransformation(HashMap<Object, Object> transformationMap) {
		super.cleanTransformation(transformationMap);

		for (NewCorrector corrector : this.getCorrectors()) {
			corrector.setPredicate(corrector.getPredicate().transform(transformationMap));
		}

		if (this.getValidator() != null) {
			this.setValidator(this.getValidator().transform(transformationMap));
		}
	}

	public List<Person> getAnswerPersons() {
		List<Person> registrations = new ArrayList<Person>();

		for (NewAnswer answer : this.getAnswers()) {
			registrations.add(answer.getPerson());
		}

		return registrations;
	}

	protected NewAnswer createAnswer() {
		this.deleteAnswer(this.getPerson());

		return new NewAnswer(this, this.getPerson());
	}

	public NewAnswer getAnswer() {
		return this.getAnswer(this.getPerson());
	}

	public NewAnswer getAnswer(Person person) {
		for (NewAnswer answer : this.getAnswers()) {
			if (answer.getPerson().equals(person)) {
				return answer;
			}
		}
		return null;
	}

	public Person getPerson() {
		return AccessControl.getPerson();
	}

	public void deleteAnswer(Person person) {
		NewAnswer answer = this.getAnswer(person);

		if (answer != null) {
			answer.delete();
		}
	}

	public void deleteAnswer() {
		this.deleteAnswer(this.getPerson());
	}

	public void giveUpQuestion() {
		NewAnswer answer = createAnswer();

		answer.setConcreteAnswer(new NullAnswer());
	}

	public boolean isAnswerable(Person person) {
		return this.getAnswer(person) == null
				|| this.getAnswer(person).getConcreteAnswer() instanceof NullAnswer;
	}

	public boolean isAnswerable() {
		return this.isAnswerable(this.getPerson());
	}

	public boolean isCanGiveUp(Person person) {
		return !this.getState(person).equals(AtomicQuestionState.GIVEN_UP);
	}

	public boolean isCanGiveUp() {
		return this.isCanGiveUp(this.getPerson());
	}

	public boolean isAnswerEditable(Person person) {
		return this.isAnswered(person);
	}

	public boolean isAnswerEditable() {
		return this.isAnswerEditable(this.getPerson());
	}

	public boolean isAnswerDeletable(Person person) {
		return this.isAnswered(person);
	}

	public boolean isAnswerDeletable() {
		return this.isAnswerDeletable(this.getPerson());
	}

	@Override
	public boolean isAnswered(Person person) {
		return this.getAnswer(person) != null
				&& !(this.getAnswer(person).getConcreteAnswer() instanceof NullAnswer);
	}

	@Override
	public boolean isAnswered() {
		return this.isAnswered(this.getPerson());
	}

	public boolean isGivenUp(Person person) {
		return this.getAnswer(person) != null
				&& this.getAnswer(person).getConcreteAnswer() instanceof NullAnswer;
	}

	public boolean isGivenUp() {
		return this.isGivenUp(this.getPerson());
	}

	public AtomicQuestionState getState(Person person) {
		if (this.isGivenUp(person)) {
			return AtomicQuestionState.GIVEN_UP;
		} else if (this.isAnswerable(person)) {
			return AtomicQuestionState.ANSWERABLE;
		} else if (this.isAnswered(person)) {
			return AtomicQuestionState.ANSWERED;
		}

		return null;
	}

	public AtomicQuestionState getState() {
		return this.getState(this.getPerson());
	}

	@Override
	public boolean isVisible(Person person) {
		return this.evaluatePreCondition(person);
	}

	public boolean isVisible() {
		return this.isVisible(this.getPerson());
	}

	@Override
	public boolean isCorrectable(Person person) {
		return this.isAnswered(person) && this.isVisible(person) && this.getCorrectorsCount() == 0;
	}

	public boolean isCorrectable() {
		return this.isCorrectable(this.getPerson());
	}

	@Override
	public int getAllUncorrectedQuestionsCount(Person person) {
		if (!this.isCorrectable(person)) {
			return 0;
		}

		return this.getAnswer(person).getPercentage() == null ? 1 : 0;
	}

	@Override
	public void publishGrades() {
		if(this.getCorrectorsCount() == 0) {
			for(NewAnswer answer : this.getAnswers()) {
				if(answer.getPercentage() == null) {
					answer.setPercentage(new Double(0));
				}
			}
		}
	}
	
	@Override
	public Grade getFinalGrade() {
		return this.getFinalGrade(this.getPerson());
	}
	
	@Override
	public Grade getFinalGrade(Person person) {
		if(!this.isAnswered(person)) {
			return new Grade(0, this.getTest().getScale());
		}
		
		if(this.getCorrectorsCount() > 0) {
			return this.getFinalGradeByCorrector(person);
		}
		
		if(this.getAnswer(person).getPercentage() == null) {
			return new Grade(0, this.getTest().getScale());
		}
		
		return this.getGrade().multiply(this.getAnswer(person).getPercentage() / 100);
	}

	private Grade getFinalGradeByCorrector(Person person) {
		NewAnswer answer = this.getAnswer(person);
		
		for(NewCorrector corrector : this.getOrderedCorrectors()) {
			if(corrector.getPredicate().evaluate(this, answer.getPerson())) {
				return this.getGrade().multiply(((double) corrector.getPercentage()) / 100);
			}
		}

		return new Grade(0, this.getTest().getScale());
	}

}
