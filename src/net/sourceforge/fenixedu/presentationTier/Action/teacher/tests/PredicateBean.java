package net.sourceforge.fenixedu.presentationTier.Action.teacher.tests;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.tests.NewAtomicQuestion;
import net.sourceforge.fenixedu.domain.tests.NewChoice;
import net.sourceforge.fenixedu.domain.tests.NewCorrector;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.domain.tests.predicates.CompositePredicateType;
import net.sourceforge.fenixedu.domain.tests.predicates.Predicate;
import net.sourceforge.fenixedu.domain.tests.predicates.PredicateType;

public class PredicateBean implements Serializable {

    public enum Action {
        COMPOSE, DELETE;

        public String getName() {
            return name();
        }
    }

    private List<Predicate> predicates;

    private List<Predicate> selectedPredicates;

    private NewQuestion question;

    private PredicateType predicateType;

    private CompositePredicateType compositePredicateType;

    private NewChoice choice;

    private NewCorrector corrector;

    private NewAtomicQuestion atomicQuestion;

    private int count;

    private int size;

    private Action action;

    private Predicate choosenPredicate;

    private Integer percentage;

    private double value;

    public PredicateBean() {
        predicates = new ArrayList<Predicate>();
        selectedPredicates = new ArrayList<Predicate>();
        this.setChoice(null);
        this.setCorrector(null);
        this.setAtomicQuestion(null);
    }

    public PredicateBean(PredicateBean predicateBean) {
        this(predicateBean.getQuestion(), predicateBean.getPredicates());
    }

    public PredicateBean(NewQuestion question) {
        this();

        this.setQuestion(question);
    }

    public PredicateBean(NewQuestion question, List<Predicate> predicates) {
        this(question);

        this.getPredicates().addAll(predicates);
    }

    public List<Predicate> getPredicates() {
        return predicates;
    }

    public List<Predicate> getSelectedPredicates() {
        return selectedPredicates;
    }

    public void setPredicates(List<Predicate> predicates) {
        this.predicates = predicates;
    }

    public void setSelectedPredicates(List<Predicate> selectedPredicates) {
        this.selectedPredicates = selectedPredicates;
    }

    public NewQuestion getQuestion() {
        return question;
    }

    public void setQuestion(NewQuestion question) {
        this.question = question;
    }

    public PredicateType getPredicateType() {
        return predicateType;
    }

    public void setPredicateType(PredicateType predicateType) {
        this.predicateType = predicateType;
    }

    public CompositePredicateType getCompositePredicateType() {
        return compositePredicateType;
    }

    public void setCompositePredicateType(CompositePredicateType compositePredicateType) {
        this.compositePredicateType = compositePredicateType;
    }

    public NewChoice getChoice() {
        return choice;
    }

    public void setChoice(NewChoice choice) {
        this.choice = choice;
    }

    public NewCorrector getCorrector() {
        return corrector;
    }

    public void setCorrector(NewCorrector corrector) {
        this.corrector = corrector;
    }

    public NewAtomicQuestion getAtomicQuestion() {
        return atomicQuestion;
    }

    public void setAtomicQuestion(NewAtomicQuestion atomicQuestion) {
        this.atomicQuestion = atomicQuestion;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Predicate getChoosenPredicate() {
        return choosenPredicate;
    }

    public void setChoosenPredicate(Predicate choosenPredicate) {
        this.choosenPredicate = choosenPredicate;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }

    public void addPredicate() {
        try {
            Constructor constructor = predicateType.getImplementingClass().getConstructor(new Class[] { getClass() });
            Predicate predicate = (Predicate) constructor.newInstance(new Object[] { this });
            predicates.add(predicate);
            this.setPredicateType(null);
        } catch (Exception e) {
            throw new RuntimeException("could.not.init.predicate", e);
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

}
