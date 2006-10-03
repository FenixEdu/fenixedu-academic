package net.sourceforge.fenixedu.domain.tests.predicates;

import java.util.HashMap;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.domain.tests.NewStringQuestion;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.PredicateBean;

public class StringSizeLessThanPredicate extends AtomicPredicate implements Predicate {
	private int size;

	public StringSizeLessThanPredicate(int size) {
		super();

		this.size = size;
	}

	public StringSizeLessThanPredicate(PredicateBean predicateBean) {
		this(predicateBean.getSize());
	}

	public boolean evaluate(NewQuestion question, Person person) {
		NewStringQuestion stringQuestion = (NewStringQuestion) question;
		
		if(!stringQuestion.isAnswered(person)) {
			return false;
		}

		return stringQuestion.getStringAnswer(person).length() < size;
	}

	public int getSize() {
		return size;
	}

	public boolean uses(Object object) {
		return false;
	}

	public Predicate transform(HashMap<Object, Object> transformMap) {
		return new StringSizeLessThanPredicate(getSize());
	}

}
