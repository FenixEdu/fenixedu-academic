package net.sourceforge.fenixedu.domain.tests;

import java.util.HashMap;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class NewChoice extends NewChoice_Base implements Positionable {

	public NewChoice() {
		super();
	}

	public NewChoice(NewMultipleChoiceQuestion multipleChoiceQuestion) {
		this();

		this.setMultipleChoiceQuestion(multipleChoiceQuestion);
		this.setPosition(this.getNewPosition());
	}

	protected Integer getNewPosition() {
		return this.getMultipleChoiceQuestion().getChoicesCount();
	}

	@Override
	public void delete() {
		NewMultipleChoiceQuestion multipleChoiceQuestion = this.getMultipleChoiceQuestion();

		for (NewCorrector corrector : multipleChoiceQuestion.getCorrectors()) {
			if (corrector.getPredicate().uses(this)) {
				corrector.delete();
			}
		}

		if (multipleChoiceQuestion.getValidator() != null && multipleChoiceQuestion.getValidator().uses(this)) {
			multipleChoiceQuestion.setValidator(null);
		}

		this.removeMultipleChoiceQuestion();

		multipleChoiceQuestion.resortChoices();

		super.delete();
	}

	public boolean isFirst() {
		return this.getPosition() == 1;
	}

	public boolean isLast() {
		return this.getPosition() == this.getMultipleChoiceQuestion().getChoicesCount();
	}

	public void switchPosition(Integer relativePosition) {
		int currentPosition = this.getPosition();
		int newPosition = currentPosition + relativePosition;
		NewMultipleChoiceQuestion multipleChoiceQuestion = this.getMultipleChoiceQuestion();

		if (relativePosition < 0 && this.isFirst()) {
			throw new DomainException("could.not.sort.up");
		}

		if (relativePosition > 0 && this.isLast()) {
			throw new DomainException("could.not.sort.down");
		}

		for (NewChoice choice : multipleChoiceQuestion.getChoices()) {
			if (choice.getPosition() == newPosition) {
				choice.setPosition(currentPosition);
				break;
			}
		}

		this.setPosition(newPosition);
	}

	@Override
	public NewTestElement copy(HashMap<Object, Object> transformationMap) {
		NewChoice choiceCopy = new NewChoice(this.getMultipleChoiceQuestion());
		
		this.initCopy(choiceCopy, transformationMap);
		
		transformationMap.put(this, choiceCopy);
		
		return choiceCopy;
	}

	@Override
	public void cleanTransformation(HashMap<Object, Object> trasformationMap) {
		//Left blank on purpose
	}

}
