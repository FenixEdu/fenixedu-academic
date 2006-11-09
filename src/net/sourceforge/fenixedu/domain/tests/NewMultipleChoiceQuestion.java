package net.sourceforge.fenixedu.domain.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.tests.answers.ConcreteAnswer;
import net.sourceforge.fenixedu.domain.tests.predicates.PredicateType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class NewMultipleChoiceQuestion extends NewMultipleChoiceQuestion_Base {

	public NewMultipleChoiceQuestion() {
		super();
		
		this.setShuffle(false);
	}

	public NewMultipleChoiceQuestion(NewQuestionGroup parentQuestionGroup) {
		this();

		init(parentQuestionGroup);
	}

	public List<NewChoice> getOrderedChoices() {
		List<NewChoice> choices = new ArrayList<NewChoice>(this.getChoices());

		Collections.sort(choices, Positionable.POSITION_COMPARATOR);

		return choices;
	}

	public List<NewChoice> getOrderedChoices(boolean honorShuffle) {
		if(honorShuffle && this.getShuffle()) {
			return getRandomlyOrderedChoices();
		} else {
			return getOrderedChoices();
		}
	}

	private List<NewChoice> getRandomlyOrderedChoices() {
		Person person = AccessControl.getUserView().getPerson();
		Random random = new Random(person.getIdInternal());
		
		int[] indexes = new int[this.getChoicesCount()];
		int[] shuffledIndexes = new int[this.getChoicesCount()];
		
		for(int i = 0; i < this.getChoicesCount(); i++) {
			indexes[i] = i;
		}

		for(int i = 0, j = this.getChoicesCount(); i < this.getChoicesCount(); i++, j--) {
			int randomPosition = random.nextInt(j);
			shuffledIndexes[i] = indexes[randomPosition];
			indexes[randomPosition] = indexes[indexes[j - 1]];
		}
		
		List<NewChoice> choices = new ArrayList<NewChoice>(this.getChoices());

		Collections.sort(choices, Positionable.POSITION_COMPARATOR);
		
		List<NewChoice> randomlyOrderedChoices = new ArrayList<NewChoice>();

		for(int i = 0; i < this.getChoicesCount(); i++) {
			randomlyOrderedChoices.add(choices.get(shuffledIndexes[i]));
		}

		return randomlyOrderedChoices;
	}

	@Override
	public void delete() {
		for (NewChoice choice : this.getChoices()) {
			choice.delete();
		}

		super.delete();
	}

	/**
	 * Cleans sorting of child choices. Should be called after deleting a
	 * choice.
	 */
	public void resortChoices() {
		int i = 1;
		for (NewChoice choice : this.getOrderedChoices()) {
			choice.setPosition(i++);
		}
	}

	protected static final List<PredicateType> predicates = new ArrayList<PredicateType>();

	static {
		predicates.add(PredicateType.MULTIPLE_CHOICE_ANSWER);
		predicates.add(PredicateType.MULTIPLE_CHOICE_COUNT);
	}

	public List<PredicateType> getPredicates() {
		return predicates;
	}

	@Override
	public NewQuestionType getQuestionType() {
		return NewQuestionType.MULTIPLE_CHOICE_QUESTION;
	}

	@Override
	protected void initCopy(NewTestElement testElement, HashMap<Object, Object> transformationMap) {
		super.initCopy(testElement, transformationMap);

		NewMultipleChoiceQuestion multipleChoiceQuestion = (NewMultipleChoiceQuestion) testElement;

		for (NewChoice choice : this.getChoices()) {
			NewChoice choiceCopy = (NewChoice) choice.copy(transformationMap);

			transformationMap.put(choice, choiceCopy);

			choiceCopy.setPosition(choice.getPosition());
			choiceCopy.setMultipleChoiceQuestion(multipleChoiceQuestion);
		}
		
		multipleChoiceQuestion.setShuffle(this.getShuffle());
	}

	@Override
	public NewTestElement copy(HashMap<Object, Object> transformationMap) {
		NewMultipleChoiceQuestion question = new NewMultipleChoiceQuestion();

		transformationMap.put(this, question);

		this.initCopy(question, transformationMap);

		return question;
	}

	public List<NewChoice> getMultipleChoiceAnswer(Person person) {
		NewAnswer answer = this.getAnswer(person);
		if (answer == null) {
			return null;
		}
		return (List<NewChoice>) answer.getConcreteAnswer().getAnswer();
	}

	public List<NewChoice> getMultipleChoiceAnswer() {
		return this.getMultipleChoiceAnswer(this.getPerson());
	}

	public void setAnswer(List<NewChoice> answerValue) {
		NewAnswer answer = this.getAnswer();

		if (answer == null) {
			answer = createAnswer();
		}

		ConcreteAnswer concreteAnswer = new ConcreteAnswer(answerValue);
		this.getAnswer().setConcreteAnswer(concreteAnswer);
	}

}
