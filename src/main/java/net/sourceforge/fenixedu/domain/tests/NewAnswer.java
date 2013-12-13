package net.sourceforge.fenixedu.domain.tests;

import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.tests.answers.ConcreteAnswer;
import net.sourceforge.fenixedu.domain.tests.answers.NullAnswer;
import net.sourceforge.fenixedu.domain.tests.predicates.Predicate;

import org.joda.time.DateTime;

public class NewAnswer extends NewAnswer_Base {

    public NewAnswer() {
        super();

        this.setRootDomainObject(Bennu.getInstance());
        this.setConcreteAnswer(new NullAnswer());
    }

    public NewAnswer(NewAtomicQuestion atomicQuestion, Person person) {
        this();

        this.setAtomicQuestion(atomicQuestion);
        this.setPerson(person);
    }

    public void delete() {
        this.setAtomicQuestion(null);
        this.setPerson(null);
        this.setRootDomainObject(null);

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

    public TestsGrade getCalculatedGrade() {
        if (this.getPercentage() == null) {
            return new TestsGrade(0, this.getAtomicQuestion().getGrade().getScale());
        }

        return new TestsGrade((this.getPercentage().doubleValue() / (100)) * this.getAtomicQuestion().getGrade().getValue(), this
                .getAtomicQuestion().getGrade().getScale());
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

    @Deprecated
    public boolean hasPercentage() {
        return getPercentage() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasConcreteAnswer() {
        return getConcreteAnswer() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

    @Deprecated
    public boolean hasAtomicQuestion() {
        return getAtomicQuestion() != null;
    }

}
