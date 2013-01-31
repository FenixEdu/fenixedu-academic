package net.sourceforge.fenixedu.domain.tests.predicates;

public enum CompositePredicateType {
	AND {
		@Override
		public Class getImplementingClass() {
			return AndPredicate.class;
		}

		@Override
		public boolean isUnary() {
			return false;
		}
	},
	OR {
		@Override
		public Class getImplementingClass() {
			return OrPredicate.class;
		}

		@Override
		public boolean isUnary() {
			return false;
		}
	},
	NOT {
		@Override
		public Class getImplementingClass() {
			return NotPredicate.class;
		}

		@Override
		public boolean isUnary() {
			return true;
		}
	};

	public abstract Class getImplementingClass();

	public abstract boolean isUnary();

	public String getName() {
		return super.name();
	}
}
