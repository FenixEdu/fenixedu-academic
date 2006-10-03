package net.sourceforge.fenixedu.domain.tests;

public enum NewQuestionType {
	STRING_QUESTION {
		public Class getImplementingClass() {
			return NewStringQuestion.class;
		}
		
		public NewQuestion newInstance(NewQuestionGroup parent) {
			return new NewStringQuestion(parent);
		}
	},
	DATE_QUESTION {
		public Class getImplementingClass() {
			return NewDateQuestion.class;
		}
		
		public NewQuestion newInstance(NewQuestionGroup parent) {
			return new NewDateQuestion(parent);
		}
	},
	NUMERIC_QUESTION {

		public Class getImplementingClass() {
			return NewNumericQuestion.class;
		}
		
		public NewQuestion newInstance(NewQuestionGroup parent) {
			return new NewNumericQuestion(parent);
		}
	},
	MULTIPLE_CHOICE_QUESTION {
		public Class getImplementingClass() {
			return NewMultipleChoiceQuestion.class;
		}
		
		public NewQuestion newInstance(NewQuestionGroup parent) {
			return new NewMultipleChoiceQuestion(parent);
		}
	},
	ALL_QUESTION_GROUP {
		public Class getImplementingClass() {
			return NewAllGroup.class;
		}
		
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
