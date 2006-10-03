package net.sourceforge.fenixedu.domain.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import net.sourceforge.fenixedu.commons.Permutations;
import net.sourceforge.fenixedu.domain.tests.predicates.PredicateType;

import org.apache.commons.collections.CollectionUtils;

public class NewQuestionGroup extends NewQuestionGroup_Base {

	public NewQuestionGroup() {
		super();
	}

	public NewQuestionGroup(NewQuestionGroup parentQuestionGroup, String name) {
		this();

		this.init(parentQuestionGroup);

		this.setName(name);
	}

	public List<NewQuestion> getChildQuestions() {
		List<NewQuestion> questions = new ArrayList<NewQuestion>();

		for (NewGroupElement groupElement : getChildElements()) {
			questions.add(groupElement.getChild());
		}

		return questions;
	}

	public Integer getChildQuestionsCount() {
		return this.getChildQuestions().size();
	}

	public List<NewQuestionGroup> getOrderedChildQuestionGroups() {
		List<NewQuestionGroup> questionGroups = this.getChildQuestionGroups();

		Collections.sort(questionGroups, QUESTION_GROUP_COMPARATOR);

		return questionGroups;
	}

	public List<NewQuestion> getOrderedChildAtomicQuestions() {
		List<NewQuestion> questions = this.getChildAtomicQuestions();

		Collections.sort(questions, new AtomicQuestionComparator(this));

		return questions;
	}

	@Override
	public boolean isComposite() {
		return true;
	}

	public int getChildQuestionGroupsCount() {
		return getChildQuestionGroups().size();
	}

	public List<NewQuestionGroup> getChildQuestionGroups() {
		List<NewQuestionGroup> questionGroups = (List<NewQuestionGroup>) CollectionUtils.select(
				getChildQuestions(), COMPOSITE_QUESTION_PREDICATE);

		return questionGroups;
	}

	public int getChildAtomicQuestionsCount() {
		return getChildAtomicQuestions().size();
	}

	public List<NewQuestion> getChildAtomicQuestions() {
		List<NewQuestion> questions = (List<NewQuestion>) CollectionUtils.selectRejected(
				getChildQuestions(), COMPOSITE_QUESTION_PREDICATE);

		return questions;
	}

	/**
	 * Cleans sorting of child questions. Should be called after deleting a
	 * child. Non composite questions are attributed a number. Composite
	 * questions are left with zero.
	 */
	public void resortChildQuestions() {
		int i = 1;
		List<NewGroupElement> groupElements = new ArrayList<NewGroupElement>(this.getChildElements());

		Collections.sort(groupElements, NewGroupElement.POSITION_COMPARATOR);

		for (NewGroupElement groupElement : groupElements) {
			if (COMPOSITE_QUESTION_PREDICATE.evaluate(groupElement.getChild())) {
				groupElement.setPosition(0);
			} else {
				groupElement.setPosition(i++);
			}
		}
	}

	@Override
	public void delete() {
		for (NewGroupElement childElement : this.getChildElements()) {
			NewQuestion child = childElement.getChild();

			this.removeChildElements(childElement);
			childElement.deleteDown();

			if (child.getParentElementsCount() == 0) {
				child.delete();
			}
		}

		super.delete();
	}

	public List<NewQuestion> getAllChildQuestionGroups() {
		Set<NewQuestion> results = new HashSet<NewQuestion>();

		List<NewQuestionGroup> childQuestionGroups = this.getOrderedChildQuestionGroups();
		results.addAll(childQuestionGroups);

		for (NewQuestionGroup childQuestionGroup : childQuestionGroups) {
			results.addAll(childQuestionGroup.getAllChildQuestionGroups());
		}

		return new ArrayList<NewQuestion>(results);
	}

	public List<NewQuestion> getAllChildAtomicQuestions() {
		Stack<NewQuestion> stack = new Stack<NewQuestion>();
		Set<NewQuestion> results = new HashSet<NewQuestion>();

		stack.add(this);

		while (stack.size() > 0) {
			NewQuestion question = stack.pop();

			if (question.isComposite()) {
				stack.addAll(((NewQuestionGroup) question).getChildQuestions());
			} else {
				results.add(question);
			}
		}

		return new ArrayList<NewQuestion>(results);
	}

	public int getAllChildAtomicQuestionsCount() {
		return getAllChildAtomicQuestions().size();
	}

	@Override
	public Integer getNewPosition(NewQuestionGroup parentQuestionGroup) {
		return 0;
	}

	public void disassociate(NewQuestion child) {
		for (NewGroupElement childElement : this.getChildElements()) {
			if (childElement.getChild().equals(child)) {
				childElement.deleteBothWays();
			}
		}
	}

	@Override
	public NewQuestionType getQuestionType() {
		return null;
	}

	@Override
	public List<PredicateType> getPredicates() {
		return null;
	}

	@Override
	public NewTestElement copy(HashMap<Object, Object> transformationMap) {
		throw new IllegalArgumentException("class.not.clonable");
	}

	transient Permutations permutations;

	@Override
	public List<NewQuestion> provide(int count) {
		List<NewQuestion> questions = this.getAllChildAtomicQuestions();

		if (questions.isEmpty()) {
			return null;
		}
		
		if(count > this.getAllChildAtomicQuestionsCount()) {
			count = this.getAllChildAtomicQuestionsCount();
		}

		if (permutations == null) {
			permutations = new Permutations(questions, count);
		}

		List<NewQuestion> permutation = permutations.nextElement();

		if (!permutations.hasMoreElements()) {
			permutations = null;
		}

		for (NewQuestion question : permutation) {
			question.setTimesUsed(question.getTimesUsed() + 1);
		}

		return permutation;
	}
	
	public boolean isGradeComplete() {
		int grades = 0;
		
		for(NewQuestion question : this.getChildAtomicQuestions()) {
			if(question.getGrade() != null) {
				grades++;
			}
		}
		
		return (grades == 0 || grades == this.getChildAtomicQuestionsCount());
	}

}
