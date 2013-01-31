package net.sourceforge.fenixedu.domain.tests.predicates;

import java.util.HashMap;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.tests.NewChoice;
import net.sourceforge.fenixedu.domain.tests.NewMultipleChoiceQuestion;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.PredicateBean;

public class MultipleChoiceAnswerPredicate extends AtomicPredicate implements Predicate {
	private final NewChoice choice;

	public MultipleChoiceAnswerPredicate(NewChoice choice) {
		super();
		this.choice = choice;
	}

	public MultipleChoiceAnswerPredicate(PredicateBean predicateBean) {
		this(predicateBean.getChoice());
	}

	@Override
	public boolean evaluate(NewQuestion question, Person person) {
		NewMultipleChoiceQuestion multipleChoiceQuestion = (NewMultipleChoiceQuestion) question;

		if (!multipleChoiceQuestion.isAnswered(person)) {
			return false;
		}

		return multipleChoiceQuestion.getMultipleChoiceAnswer(person).contains(this.getChoice());
	}

	public NewChoice getChoice() {
		return choice;
	}

	@Override
	public boolean uses(Object object) {
		NewChoice choice = (NewChoice) object;

		return choice.equals(this.getChoice());
	}

	@Override
	public Predicate transform(HashMap<Object, Object> transformMap) {
		NewChoice transformation = (NewChoice) transformMap.get(getChoice());
		return new MultipleChoiceAnswerPredicate(transformation != null ? transformation : getChoice());
	}

}
