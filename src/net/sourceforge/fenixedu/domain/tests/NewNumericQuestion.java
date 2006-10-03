package net.sourceforge.fenixedu.domain.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.tests.answers.ConcreteAnswer;
import net.sourceforge.fenixedu.domain.tests.predicates.PredicateType;

public class NewNumericQuestion extends NewNumericQuestion_Base {

	public NewNumericQuestion() {
		super();
	}

	public NewNumericQuestion(NewQuestionGroup parentQuestionGroup) {
		super();

		init(parentQuestionGroup);
	}

	protected static final List<PredicateType> predicates = new ArrayList<PredicateType>();

	static {
		predicates.add(PredicateType.NUMERIC_EQUALS);
		predicates.add(PredicateType.NUMERIC_GREATER_THAN);
		predicates.add(PredicateType.NUMERIC_LESS_THAN);
		predicates.add(PredicateType.NUMERIC_GREATER_THAN_OR_EQUAL);
		predicates.add(PredicateType.NUMERIC_LESS_THAN_OR_EQUAL);
	}

	@Override
	public List<PredicateType> getPredicates() {
		return predicates;
	}

	@Override
	public NewQuestionType getQuestionType() {
		return NewQuestionType.NUMERIC_QUESTION;
	}

	@Override
	public NewTestElement copy(HashMap<Object, Object> transformationMap) {
		NewNumericQuestion question = new NewNumericQuestion();

		transformationMap.put(this, question);

		this.initCopy(question, transformationMap);

		return question;
	}
	
	public Double getNumericAnswer(Person person) {
		NewAnswer answer = this.getAnswer(person);
		if(answer == null) {
			return null;
		}
		return (Double) answer.getConcreteAnswer().getAnswer();
	}

	public Double getNumericAnswer() {
		return this.getNumericAnswer(this.getPerson());
	}
	
	public void setAnswer(Double answerValue) {
		NewAnswer answer = this.getAnswer();
		
		if(answer == null) {
			answer = createAnswer();
		}
		
		ConcreteAnswer concreteAnswer = new ConcreteAnswer(answerValue);
		this.getAnswer().setConcreteAnswer(concreteAnswer);
	}

}
