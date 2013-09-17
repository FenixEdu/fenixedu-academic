package net.sourceforge.fenixedu.domain.tests;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.tests.predicates.Predicate;

public class NewCorrector extends NewCorrector_Base implements Positionable {

    public NewCorrector() {
        super();

        this.setRootDomainObject(RootDomainObject.getInstance());
    }

    public NewCorrector(NewAtomicQuestion atomicQuestion, Predicate predicate, Integer percentage) {
        this();

        this.setAtomicQuestion(atomicQuestion);
        this.setPredicate(predicate);
        this.setPercentage(percentage);
        this.setPosition(atomicQuestion.getCorrectorsSet().size());
    }

    public void delete() {
        NewAtomicQuestion atomicQuestion = this.getAtomicQuestion();

        if (atomicQuestion.isBelongsToAllGroup()) {
            for (NewQuestion question : atomicQuestion.getTopAllGroup().getAllChildAtomicQuestions()) {
                if (question.getPreCondition() != null && question.getPreCondition().uses(this)) {
                    question.setPreCondition(null);
                }
            }
        }

        this.setAtomicQuestion(null);
        this.setRootDomainObject(null);

        atomicQuestion.resortCorrectors();

        super.deleteDomainObject();
    }

    @Override
    public boolean isFirst() {
        return this.getPosition() == 1;
    }

    @Override
    public boolean isLast() {
        return this.getPosition() == this.getAtomicQuestion().getCorrectorsSet().size();
    }

    @Override
    public void switchPosition(Integer relativePosition) {
        int currentPosition = this.getPosition();
        int newPosition = currentPosition + relativePosition;
        NewAtomicQuestion atomicQuestion = this.getAtomicQuestion();

        if (relativePosition < 0 && this.isFirst()) {
            throw new DomainException("could.not.sort.up");
        }

        if (relativePosition > 0 && this.isLast()) {
            throw new DomainException("could.not.sort.down");
        }

        for (NewCorrector corrector : atomicQuestion.getCorrectors()) {
            if (corrector.getPosition() == newPosition) {
                corrector.setPosition(currentPosition);
                break;
            }
        }

        this.setPosition(newPosition);
    }

    public NewCorrector copy() {
        return new NewCorrector(this.getAtomicQuestion(), this.getPredicate(), this.getPercentage());
    }

    @Deprecated
    public boolean hasPercentage() {
        return getPercentage() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasPredicate() {
        return getPredicate() != null;
    }

    @Deprecated
    public boolean hasPosition() {
        return getPosition() != null;
    }

    @Deprecated
    public boolean hasAtomicQuestion() {
        return getAtomicQuestion() != null;
    }

}
