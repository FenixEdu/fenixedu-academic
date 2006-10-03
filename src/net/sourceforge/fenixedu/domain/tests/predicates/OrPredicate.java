package net.sourceforge.fenixedu.domain.tests.predicates;

import java.util.HashMap;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;

public class OrPredicate extends CompositePredicate implements Predicate {

	public OrPredicate() {
		super();
	}

	public boolean evaluate(NewQuestion question, Person person) {
		for (Predicate predicate : this.getPredicates()) {
			if (predicate.evaluate(question, person)) {
				return true;
			}
		}

		return false;
	}

	public Predicate transform(HashMap<Object, Object> transformMap) {
		OrPredicate orPredicate = new OrPredicate();

		return initCopy(transformMap, this, orPredicate);
	}

}
