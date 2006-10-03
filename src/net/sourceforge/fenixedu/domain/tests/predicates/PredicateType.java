package net.sourceforge.fenixedu.domain.tests.predicates;

public enum PredicateType {
	GIVEN_UP_QUESTION {
		public Class getImplementingClass() {
			return GivenUpQuestionPredicate.class;
		}
	},
	ANSWERED_QUESTION {
		public Class getImplementingClass() {
			return AnsweredQuestionPredicate.class;
		}
	},
	OUTCOME_BY_CORRECTOR {
		public Class getImplementingClass() {
			return OutcomeByCorrectorPredicate.class;
		}
	},
	OUTCOME_BY_NO_CORRECTOR {
		public Class getImplementingClass() {
			return OutcomeByNoCorrectorPredicate.class;
		}
	},
	MULTIPLE_CHOICE_ANSWER {
		public Class getImplementingClass() {
			return MultipleChoiceAnswerPredicate.class;
		}
	},
	MULTIPLE_CHOICE_COUNT {
		public Class getImplementingClass() {
			return MultipleChoiceCountPredicate.class;
		}
	},
	STRING_SIZE_EQUALS {
		public Class getImplementingClass() {
			return StringSizeEqualsPredicate.class;
		}
	},
	STRING_SIZE_MORE_THAN {
		public Class getImplementingClass() {
			return StringSizeMoreThanPredicate.class;
		}
	},
	STRING_SIZE_LESS_THAN {
		public Class getImplementingClass() {
			return StringSizeLessThanPredicate.class;
		}
	},
	NUMERIC_EQUALS {
		public Class getImplementingClass() {
			return NumericEqualsPredicate.class;
		}
	},
	NUMERIC_GREATER_THAN {
		public Class getImplementingClass() {
			return NumericGreaterThanPredicate.class;
		}
	},
	NUMERIC_GREATER_THAN_OR_EQUAL {
		public Class getImplementingClass() {
			return NumericGreaterThanOrEqualPredicate.class;
		}
	},
	NUMERIC_LESS_THAN_OR_EQUAL {
		public Class getImplementingClass() {
			return NumericLessThanOrEqualPredicate.class;
		}
	},
	NUMERIC_LESS_THAN {
		public Class getImplementingClass() {
			return NumericLessThanPredicate.class;
		}
	};

	public abstract Class getImplementingClass();

	public String getName() {
		return super.name();
	}
}
