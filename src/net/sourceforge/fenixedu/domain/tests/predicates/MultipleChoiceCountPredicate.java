package net.sourceforge.fenixedu.domain.tests.predicates;

import java.util.HashMap;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.tests.NewMultipleChoiceQuestion;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.PredicateBean;

public class MultipleChoiceCountPredicate extends AtomicPredicate implements Predicate {
	private int count;

	public MultipleChoiceCountPredicate(int count) {
		super();

		this.count = count;
	}

	public MultipleChoiceCountPredicate(PredicateBean predicateBean) {
		this(predicateBean.getCount());
	}

	public int getCount() {
		return count;
	}

	public boolean evaluate(NewQuestion question, Person person) {
		NewMultipleChoiceQuestion multipleChoiceQuestion = (NewMultipleChoiceQuestion) question;
		
		if(!multipleChoiceQuestion.isAnswered(person)) {
			return false;
		}

		return multipleChoiceQuestion.getMultipleChoiceAnswer(person).size() == count;
	}

	public boolean uses(Object object) {
		return false;
	}

	public Predicate transform(HashMap<Object, Object> transformMap) {
		return new MultipleChoiceCountPredicate(getCount());
	}

}
