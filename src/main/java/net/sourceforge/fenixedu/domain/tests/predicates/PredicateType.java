package net.sourceforge.fenixedu.domain.tests.predicates;

public enum PredicateType {
    GIVEN_UP_QUESTION {
        @Override
        public Class getImplementingClass() {
            return GivenUpQuestionPredicate.class;
        }
    },
    ANSWERED_QUESTION {
        @Override
        public Class getImplementingClass() {
            return AnsweredQuestionPredicate.class;
        }
    },
    OUTCOME_BY_CORRECTOR {
        @Override
        public Class getImplementingClass() {
            return OutcomeByCorrectorPredicate.class;
        }
    },
    OUTCOME_BY_NO_CORRECTOR {
        @Override
        public Class getImplementingClass() {
            return OutcomeByNoCorrectorPredicate.class;
        }
    },
    MULTIPLE_CHOICE_ANSWER {
        @Override
        public Class getImplementingClass() {
            return MultipleChoiceAnswerPredicate.class;
        }
    },
    MULTIPLE_CHOICE_COUNT {
        @Override
        public Class getImplementingClass() {
            return MultipleChoiceCountPredicate.class;
        }
    },
    STRING_SIZE_EQUALS {
        @Override
        public Class getImplementingClass() {
            return StringSizeEqualsPredicate.class;
        }
    },
    STRING_SIZE_MORE_THAN {
        @Override
        public Class getImplementingClass() {
            return StringSizeMoreThanPredicate.class;
        }
    },
    STRING_SIZE_LESS_THAN {
        @Override
        public Class getImplementingClass() {
            return StringSizeLessThanPredicate.class;
        }
    },
    NUMERIC_EQUALS {
        @Override
        public Class getImplementingClass() {
            return NumericEqualsPredicate.class;
        }
    },
    NUMERIC_GREATER_THAN {
        @Override
        public Class getImplementingClass() {
            return NumericGreaterThanPredicate.class;
        }
    },
    NUMERIC_GREATER_THAN_OR_EQUAL {
        @Override
        public Class getImplementingClass() {
            return NumericGreaterThanOrEqualPredicate.class;
        }
    },
    NUMERIC_LESS_THAN_OR_EQUAL {
        @Override
        public Class getImplementingClass() {
            return NumericLessThanOrEqualPredicate.class;
        }
    },
    NUMERIC_LESS_THAN {
        @Override
        public Class getImplementingClass() {
            return NumericLessThanPredicate.class;
        }
    };

    public abstract Class getImplementingClass();

    public String getName() {
        return super.name();
    }
}
