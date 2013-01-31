package net.sourceforge.fenixedu.domain.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.tests.answers.ConcreteAnswer;
import net.sourceforge.fenixedu.domain.tests.predicates.PredicateType;

public class NewStringQuestion extends NewStringQuestion_Base {

	public NewStringQuestion() {
		super();
	}

	public NewStringQuestion(NewQuestionGroup parentQuestionGroup) {
		super();

		init(parentQuestionGroup);
	}

	protected static final List<PredicateType> predicates = new ArrayList<PredicateType>();

	static {
		predicates.add(PredicateType.STRING_SIZE_EQUALS);
		predicates.add(PredicateType.STRING_SIZE_MORE_THAN);
		predicates.add(PredicateType.STRING_SIZE_LESS_THAN);
	}

	@Override
	public List<PredicateType> getPredicates() {
		return predicates;
	}

	@Override
	public NewQuestionType getQuestionType() {
		return NewQuestionType.STRING_QUESTION;
	}

	@Override
	public NewTestElement copy(HashMap<Object, Object> transformationMap) {
		NewStringQuestion question = new NewStringQuestion();

		transformationMap.put(this, question);

		this.initCopy(question, transformationMap);

		return question;
	}

	public String getStringAnswer(Person person) {
		NewAnswer answer = this.getAnswer(person);
		if (answer == null) {
			return null;
		}
		return (String) answer.getConcreteAnswer().getAnswer();
	}

	public String getStringAnswer() {
		return this.getStringAnswer(this.getPerson());
	}

	public void setAnswer(String answerValue) {
		NewAnswer answer = this.getAnswer();

		if (answer == null) {
			answer = createAnswer();
		}

		ConcreteAnswer concreteAnswer = new ConcreteAnswer(answerValue);
		this.getAnswer().setConcreteAnswer(concreteAnswer);
	}

}
