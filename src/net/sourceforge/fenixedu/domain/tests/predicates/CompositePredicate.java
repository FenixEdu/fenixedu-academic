package net.sourceforge.fenixedu.domain.tests.predicates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class CompositePredicate implements Predicate {

	private List<Predicate> predicates;

	public CompositePredicate() {
		predicates = new ArrayList<Predicate>();
	}

	public List<Predicate> getPredicates() {
		return predicates;
	}

	public boolean uses(Object object) {
		for (Predicate predicate : this.getPredicates()) {
			if (predicate.uses(object)) {
				return true;
			}
		}

		return false;
	}

	static protected List<CompositePredicateType> compositePredicateTypes = new ArrayList<CompositePredicateType>();

	static {
		compositePredicateTypes.add(CompositePredicateType.AND);
		compositePredicateTypes.add(CompositePredicateType.OR);
		compositePredicateTypes.add(CompositePredicateType.NOT);
	}

	static public List<CompositePredicateType> getCompositePredicateTypes() {
		return compositePredicateTypes;
	}

	public boolean isComposite() {
		return true;
	}

	public Predicate initCopy(HashMap<Object, Object> transformMap, CompositePredicate original,
			CompositePredicate copy) {
		for (Predicate predicate : original.getPredicates()) {
			copy.getPredicates().add(predicate.transform(transformMap));
		}

		return copy;
	}

}
