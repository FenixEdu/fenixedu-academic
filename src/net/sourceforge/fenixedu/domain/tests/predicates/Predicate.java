package net.sourceforge.fenixedu.domain.tests.predicates;

import java.io.Serializable;
import java.util.HashMap;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;

public interface Predicate extends Serializable {
	public abstract boolean evaluate(NewQuestion question, Person person);

	public abstract boolean uses(Object object);

	public abstract boolean isComposite();

	public abstract Predicate transform(HashMap<Object, Object> transformMap);
}
