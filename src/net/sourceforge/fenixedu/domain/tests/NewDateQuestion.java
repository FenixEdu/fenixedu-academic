package net.sourceforge.fenixedu.domain.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.tests.answers.ConcreteAnswer;
import net.sourceforge.fenixedu.domain.tests.predicates.PredicateType;

import org.joda.time.DateTime;

public class NewDateQuestion extends NewDateQuestion_Base {

	public NewDateQuestion() {
		super();
	}

	public NewDateQuestion(NewQuestionGroup parentQuestionGroup) {
		super();

		init(parentQuestionGroup);
	}

	protected static final List<PredicateType> predicates = new ArrayList<PredicateType>();

	@Override
	public List<PredicateType> getPredicates() {
		return predicates;
	}

	@Override
	public NewQuestionType getQuestionType() {
		return NewQuestionType.DATE_QUESTION;
	}

	@Override
	public NewTestElement copy(HashMap<Object, Object> transformationMap) {
		NewDateQuestion question = new NewDateQuestion();

		transformationMap.put(this, question);

		this.initCopy(question, transformationMap);

		return question;
	}

	public DateTime getDateAnswer(Person person) {
		NewAnswer answer = this.getAnswer(person);
		if (answer == null) {
			return null;
		}
		return (DateTime) answer.getConcreteAnswer().getAnswer();
	}

	public DateTime getDateAnswer() {
		return this.getDateAnswer(this.getPerson());
	}

	public void setAnswer(DateTime answerValue) {
		NewAnswer answer = this.getAnswer();
		
		if(answer == null) {
			answer = createAnswer();
		}
		
		ConcreteAnswer concreteAnswer = new ConcreteAnswer(answerValue);
		this.getAnswer().setConcreteAnswer(concreteAnswer);
	}

}
