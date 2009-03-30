package net.sourceforge.fenixedu.domain.tests.predicates;

import java.util.HashMap;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.tests.NewAtomicQuestion;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.domain.tests.answers.NullAnswer;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.PredicateBean;

public class GivenUpQuestionPredicate extends AtomicPredicate implements Predicate {
    private final DomainReference<NewAtomicQuestion> atomicQuestion;

    public GivenUpQuestionPredicate(NewAtomicQuestion atomicQuestion) {
	super();
	this.atomicQuestion = new DomainReference<NewAtomicQuestion>(atomicQuestion);
    }

    public GivenUpQuestionPredicate(PredicateBean predicateBean) {
	this(predicateBean.getAtomicQuestion());
    }

    public boolean evaluate(NewQuestion question, Person person) {
	if (this.getAtomicQuestion().getAnswer(person) == null) {
	    return false;
	}

	return this.getAtomicQuestion().getAnswer(person).getConcreteAnswer() instanceof NullAnswer;
    }

    public NewAtomicQuestion getAtomicQuestion() {
	return atomicQuestion.getObject();
    }

    public boolean uses(Object object) {
	NewAtomicQuestion atomicQuestion = (NewAtomicQuestion) object;

	return atomicQuestion.equals(this.getAtomicQuestion());
    }

    public Predicate transform(HashMap<Object, Object> transformMap) {
	NewAtomicQuestion transformation = (NewAtomicQuestion) transformMap.get(getAtomicQuestion());
	return new GivenUpQuestionPredicate(transformation != null ? transformation : getAtomicQuestion());
    }

}
