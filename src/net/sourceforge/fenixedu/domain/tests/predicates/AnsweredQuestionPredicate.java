package net.sourceforge.fenixedu.domain.tests.predicates;

import java.util.HashMap;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.tests.NewAtomicQuestion;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.PredicateBean;

public class AnsweredQuestionPredicate extends AtomicPredicate implements Predicate {
	private final NewAtomicQuestion atomicQuestion;

	public AnsweredQuestionPredicate(NewAtomicQuestion atomicQuestion) {
		super();
		this.atomicQuestion = atomicQuestion;
	}

	public AnsweredQuestionPredicate(PredicateBean predicateBean) {
		this(predicateBean.getAtomicQuestion());
	}

	@Override
	public boolean evaluate(NewQuestion question, Person person) {
		return this.getAtomicQuestion().isAnswered(person);
	}

	public NewAtomicQuestion getAtomicQuestion() {
		return atomicQuestion;
	}

	@Override
	public boolean uses(Object object) {
		NewAtomicQuestion atomicQuestion = (NewAtomicQuestion) object;

		return atomicQuestion.equals(this.getAtomicQuestion());
	}

	@Override
	public Predicate transform(HashMap<Object, Object> transformMap) {
		NewAtomicQuestion transformation = (NewAtomicQuestion) transformMap.get(getAtomicQuestion());
		return new AnsweredQuestionPredicate(transformation != null ? transformation : getAtomicQuestion());
	}
}
