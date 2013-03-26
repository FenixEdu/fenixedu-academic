package net.sourceforge.fenixedu.domain.tests;

public enum NewQuestionType {
    STRING_QUESTION {
        @Override
        public Class getImplementingClass() {
            return NewStringQuestion.class;
        }

        @Override
        public NewQuestion newInstance(NewQuestionGroup parent) {
            return new NewStringQuestion(parent);
        }
    },
    DATE_QUESTION {
        @Override
        public Class getImplementingClass() {
            return NewDateQuestion.class;
        }

        @Override
        public NewQuestion newInstance(NewQuestionGroup parent) {
            return new NewDateQuestion(parent);
        }
    },
    NUMERIC_QUESTION {

        @Override
        public Class getImplementingClass() {
            return NewNumericQuestion.class;
        }

        @Override
        public NewQuestion newInstance(NewQuestionGroup parent) {
            return new NewNumericQuestion(parent);
        }
    },
    MULTIPLE_CHOICE_QUESTION {
        @Override
        public Class getImplementingClass() {
            return NewMultipleChoiceQuestion.class;
        }

        @Override
        public NewQuestion newInstance(NewQuestionGroup parent) {
            return new NewMultipleChoiceQuestion(parent);
        }
    },
    ALL_QUESTION_GROUP {
        @Override
        public Class getImplementingClass() {
            return NewAllGroup.class;
        }

        @Override
        public NewQuestion newInstance(NewQuestionGroup parent) {
            return new NewAllGroup(parent);
        }
    };

    public abstract Class getImplementingClass();

    public abstract NewQuestion newInstance(NewQuestionGroup parent);

    public String getName() {
        return super.name();
    }
}
