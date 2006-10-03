package net.sourceforge.fenixedu.domain.tests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.tests.predicates.PredicateType;

import org.apache.commons.collections.Predicate;

public class NewAllGroup extends NewAllGroup_Base {

	public NewAllGroup() {
		super();
	}

	public NewAllGroup(NewQuestionGroup parentQuestionGroup) {
		this();

		this.init(parentQuestionGroup);
	}

	protected static class AllGroupPredicate implements Predicate {
		public boolean evaluate(Object arg0) {
			return arg0 instanceof NewAllGroup;
		}
	}

	public static final Predicate ALL_GROUP_PREDICATE = new AllGroupPredicate();

	@Override
	public boolean isComposite() {
		return false;
	}

	@Override
	public Integer getNewPosition(NewQuestionGroup parentQuestionGroup) {
		return parentQuestionGroup.getChildAtomicQuestionsCount();
	}

	@Override
	public void setParentQuestionGroup(NewQuestionGroup parentQuestionGroup) {
		if (!isTopAllGroup()) {
			throw new DomainException("could.not.associate");
		}

		super.setParentQuestionGroup(parentQuestionGroup);
	}

	public boolean isTopAllGroup() {
		if (this.getParentQuestionGroupsCount() == 1
				&& this.getParentQuestionGroups().get(0) instanceof NewAllGroup) {
			return false;
		}

		return true;
	}

	@Override
	public NewQuestionType getQuestionType() {
		return NewQuestionType.ALL_QUESTION_GROUP;
	}

	@Override
	public List<NewQuestionGroup> getAssociableParents() {
		if (!this.getParentQuestionGroups().get(0).isComposite()) {
			return new ArrayList<NewQuestionGroup>();
		}

		return super.getAssociableParents();
	}

	protected static final List<PredicateType> predicates = new ArrayList<PredicateType>();

	@Override
	public List<PredicateType> getPredicates() {
		return predicates;
	}

	@Override
	public List<NewQuestion> provide(int count) {
		this.setTimesUsed(this.getTimesUsed() + 1);

		ArrayList<NewQuestion> choosenQuestions = new ArrayList<NewQuestion>();
		choosenQuestions.add(this);

		return choosenQuestions;
	}

}
