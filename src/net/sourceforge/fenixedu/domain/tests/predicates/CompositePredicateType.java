package net.sourceforge.fenixedu.domain.tests.predicates;

public enum CompositePredicateType {
	AND {
		public Class getImplementingClass() {
			return AndPredicate.class;
		}
		
		public boolean isUnary() {
			return false;
		}
	},
	OR {
		public Class getImplementingClass() {
			return OrPredicate.class;
		}
		
		public boolean isUnary() {
			return false;
		}
	},
	NOT {
		public Class getImplementingClass() {
			return NotPredicate.class;
		}
		
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
