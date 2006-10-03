package net.sourceforge.fenixedu.domain.tests.predicates;

import java.util.HashMap;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.tests.NewCorrector;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.PredicateBean;

public class OutcomeByCorrectorPredicate extends AtomicPredicate implements Predicate {
	private DomainReference<NewCorrector> corrector;

	public OutcomeByCorrectorPredicate(NewCorrector corrector) {
		super();

		this.setCorrector(corrector);
	}

	public OutcomeByCorrectorPredicate(PredicateBean predicateBean) {
		this(predicateBean.getCorrector());
	}

	public boolean evaluate(NewQuestion question, Person person) {
		NewCorrector corrector = this.getCorrector();
		
		if(!corrector.getAtomicQuestion().isAnswered(person)) {
			return false;
		}
		
		return corrector.getPredicate().evaluate(corrector.getAtomicQuestion(), person);
	}

	public NewCorrector getCorrector() {
		return corrector.getObject();
	}

	private void setCorrector(NewCorrector corrector) {
		this.corrector = new DomainReference<NewCorrector>(corrector);
	}

	public boolean uses(Object object) {
		NewCorrector corrector = (NewCorrector) object;

		return corrector.equals(this.getCorrector());
	}

	public Predicate transform(HashMap<Object, Object> transformMap) {
		OutcomeByCorrectorPredicate predicate = new OutcomeByCorrectorPredicate(getCorrector());

		if (transformMap.get(this.getCorrector()) != null) {
			predicate.setCorrector((NewCorrector) transformMap.get(this.getCorrector()));
		}

		return predicate;
	}

}
