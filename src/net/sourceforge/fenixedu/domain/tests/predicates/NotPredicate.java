package net.sourceforge.fenixedu.domain.tests.predicates;

import java.util.HashMap;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;

public class NotPredicate extends CompositePredicate implements Predicate {

	public NotPredicate() {
		super();
	}

	public boolean evaluate(NewQuestion question, Person person) {
		return !getPredicates().get(0).evaluate(question, person);
	}

	@Override
	public boolean isComposite() {
		return false;
	}

	public Predicate transform(HashMap<Object, Object> transformMap) {
		NotPredicate notPredicate = new NotPredicate();
		
		return initCopy(transformMap, this, notPredicate);
	}

}
