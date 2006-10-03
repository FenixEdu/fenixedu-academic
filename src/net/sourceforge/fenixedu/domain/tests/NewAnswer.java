package net.sourceforge.fenixedu.domain.tests;

import java.util.List;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.tests.answers.ConcreteAnswer;
import net.sourceforge.fenixedu.domain.tests.answers.NullAnswer;
import net.sourceforge.fenixedu.domain.tests.predicates.Predicate;

public class NewAnswer extends NewAnswer_Base {

	public NewAnswer() {
		super();

		this.setRootDomainObject(RootDomainObject.getInstance());
		this.setConcreteAnswer(new NullAnswer());
	}

	public NewAnswer(NewAtomicQuestion atomicQuestion, Person person) {
		this();

		this.setAtomicQuestion(atomicQuestion);
		this.setPerson(person);
	}

	public void delete() {
		this.removeAtomicQuestion();
		this.removePerson();
		this.removeRootDomainObject();

		super.deleteDomainObject();
	}

	@Override
	public void setConcreteAnswer(ConcreteAnswer concreteAnswer) {
		super.setConcreteAnswer(concreteAnswer);

		if (concreteAnswer instanceof NullAnswer) {
			return;
		}

		Predicate validator = this.getAtomicQuestion().getValidator();

		if (validator != null && !validator.evaluate(this.getAtomicQuestion(), this.getPerson())) {
			throw new DomainException("validator.failed");
		}
	}

	public Grade getCalculatedGrade() {
		if (this.getPercentage() == null) {
			return new Grade(0, this.getAtomicQuestion().getGrade().getScale());
		}

		return new Grade((this.getPercentage().doubleValue() / ((double) 100)) * this.getAtomicQuestion().getGrade().getValue(),
				this.getAtomicQuestion().getGrade().getScale());
	}
	
	public String getStringAnswer() {
		return (String) this.getConcreteAnswer().getAnswer();
	}
	
	public Double getNumericAnswer() {
		return (Double) this.getConcreteAnswer().getAnswer();
	}
	
	public DateTime getDateAnswer() {
		return (DateTime) this.getConcreteAnswer().getAnswer();
	}
	
	public List<NewChoice> getMultipleChoiceAnswer() {
		return (List<NewChoice>) this.getConcreteAnswer().getAnswer();
	}

}
