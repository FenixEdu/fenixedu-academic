package net.sourceforge.fenixedu.domain.tests.predicates;

import java.util.HashMap;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.tests.NewAtomicQuestion;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.PredicateBean;

public class AnsweredQuestionPredicate extends AtomicPredicate implements Predicate {
	private DomainReference<NewAtomicQuestion> atomicQuestion;

	public AnsweredQuestionPredicate(NewAtomicQuestion atomicQuestion) {
		super();

		this.setAtomicQuestion(atomicQuestion);
	}

	public AnsweredQuestionPredicate(PredicateBean predicateBean) {
		this(predicateBean.getAtomicQuestion());
	}

	public boolean evaluate(NewQuestion question, Person person) {
		return this.getAtomicQuestion().isAnswered(person);
	}

	public NewAtomicQuestion getAtomicQuestion() {
		return atomicQuestion.getObject();
	}

	private void setAtomicQuestion(NewAtomicQuestion atomicQuestion) {
		this.atomicQuestion = new DomainReference<NewAtomicQuestion>(atomicQuestion);
	}

	public boolean uses(Object object) {
		NewAtomicQuestion atomicQuestion = (NewAtomicQuestion) object;

		return atomicQuestion.equals(this.getAtomicQuestion());
	}

	public Predicate transform(HashMap<Object, Object> transformMap) {
		AnsweredQuestionPredicate predicate = new AnsweredQuestionPredicate(getAtomicQuestion());

		if (transformMap.get(this.getAtomicQuestion()) != null) {
			predicate.setAtomicQuestion((NewAtomicQuestion) transformMap.get(this.getAtomicQuestion()));
		}

		return predicate;
	}

}
