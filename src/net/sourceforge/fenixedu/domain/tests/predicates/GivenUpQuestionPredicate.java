package net.sourceforge.fenixedu.domain.tests.predicates;

import java.util.HashMap;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.tests.NewAtomicQuestion;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.domain.tests.answers.NullAnswer;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.PredicateBean;

public class GivenUpQuestionPredicate extends AtomicPredicate implements Predicate {
	private DomainReference<NewAtomicQuestion> atomicQuestion;

	public GivenUpQuestionPredicate(NewAtomicQuestion atomicQuestion) {
		super();

		this.setAtomicQuestion(atomicQuestion);
	}

	public GivenUpQuestionPredicate(PredicateBean predicateBean) {
		this(predicateBean.getAtomicQuestion());
	}

	public boolean evaluate(NewQuestion question, Person person) {
		if (this.getAtomicQuestion().getAnswer(person) == null) {
			return false;
		}

		return this.getAtomicQuestion().getAnswer(person).getConcreteAnswer() instanceof NullAnswer;
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
		GivenUpQuestionPredicate predicate = new GivenUpQuestionPredicate(getAtomicQuestion());

		if (transformMap.get(this.getAtomicQuestion()) != null) {
			predicate.setAtomicQuestion((NewAtomicQuestion) transformMap.get(this.getAtomicQuestion()));
		}

		return predicate;
	}

}
