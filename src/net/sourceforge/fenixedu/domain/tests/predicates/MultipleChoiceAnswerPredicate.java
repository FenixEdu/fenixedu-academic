package net.sourceforge.fenixedu.domain.tests.predicates;

import java.util.HashMap;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.tests.NewChoice;
import net.sourceforge.fenixedu.domain.tests.NewMultipleChoiceQuestion;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.PredicateBean;

public class MultipleChoiceAnswerPredicate extends AtomicPredicate implements Predicate {
	private DomainReference<NewChoice> choice;

	public MultipleChoiceAnswerPredicate(NewChoice choice) {
		super();

		this.setChoice(choice);
	}

	public MultipleChoiceAnswerPredicate(PredicateBean predicateBean) {
		this(predicateBean.getChoice());
	}

	public boolean evaluate(NewQuestion question, Person person) {
		NewMultipleChoiceQuestion multipleChoiceQuestion = (NewMultipleChoiceQuestion) question;

		if (!multipleChoiceQuestion.isAnswered(person)) {
			return false;
		}

		return multipleChoiceQuestion.getMultipleChoiceAnswer(person).contains(this.getChoice());
	}

	public NewChoice getChoice() {
		return choice.getObject();
	}

	private void setChoice(NewChoice choice) {
		this.choice = new DomainReference<NewChoice>(choice);
	}

	public boolean uses(Object object) {
		NewChoice choice = (NewChoice) object;

		return choice.equals(this.getChoice());
	}

	public Predicate transform(HashMap<Object, Object> transformMap) {
		MultipleChoiceAnswerPredicate predicate = new MultipleChoiceAnswerPredicate(getChoice());

		if (transformMap.get(this.getChoice()) != null) {
			predicate.setChoice((NewChoice) transformMap.get(this.getChoice()));
		}

		return predicate;
	}

}
