package net.sourceforge.fenixedu.domain.tests.predicates;

import java.util.HashMap;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;

public class AndPredicate extends CompositePredicate implements Predicate {

	public AndPredicate() {
		super();
	}

	public boolean evaluate(NewQuestion question, Person person) {
		for (Predicate predicate : this.getPredicates()) {
			if (!predicate.evaluate(question, person)) {
				return false;
			}
		}

		return true;
	}

	public Predicate transform(HashMap<Object, Object> transformMap) {
		AndPredicate andPredicate = new AndPredicate();
		
		return initCopy(transformMap, this, andPredicate);
	}

}
