package net.sourceforge.fenixedu.domain.tests.predicates;

import java.util.HashMap;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.tests.NewAtomicQuestion;
import net.sourceforge.fenixedu.domain.tests.NewCorrector;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.PredicateBean;

public class OutcomeByNoCorrectorPredicate extends AtomicPredicate implements Predicate {
	private DomainReference<NewAtomicQuestion> atomicQuestion;

	public OutcomeByNoCorrectorPredicate(NewAtomicQuestion atomicQuestion) {
		super();

		this.setAtomicQuestion(atomicQuestion);
	}

	public OutcomeByNoCorrectorPredicate(PredicateBean predicateBean) {
		this(predicateBean.getAtomicQuestion());
	}

	public boolean evaluate(NewQuestion question, Person person) {
		if (this.getAtomicQuestion().getAnswer(person) == null) {
			return false;
		}

		for (NewCorrector corrector : this.getAtomicQuestion().getCorrectors()) {
			if (corrector.getPredicate().evaluate(this.getAtomicQuestion(), person)) {
				return false;
			}
		}

		return true;
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
		OutcomeByNoCorrectorPredicate predicate = new OutcomeByNoCorrectorPredicate(getAtomicQuestion());

		if (transformMap.get(this.getAtomicQuestion()) != null) {
			predicate.setAtomicQuestion((NewAtomicQuestion) transformMap.get(this.getAtomicQuestion()));
		}

		return predicate;
	}

}
