package net.sourceforge.fenixedu.domain.tests.predicates;

public abstract class AtomicPredicate implements Predicate {

	@Override
	public boolean isComposite() {
		return false;
	}

}
