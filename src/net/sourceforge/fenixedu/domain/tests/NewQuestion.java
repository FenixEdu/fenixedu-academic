package net.sourceforge.fenixedu.domain.tests;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.tests.predicates.PredicateType;

import org.apache.commons.collections.Predicate;

public abstract class NewQuestion extends NewQuestion_Base {

	public NewQuestion() {
		super();

		this.setTimesUsed(0);
	}

	public NewQuestion(NewQuestionGroup parentQuestionGroup) {
		this();

		init(parentQuestionGroup);
	}

	protected void init(NewQuestionGroup parentQuestionGroup) {
		this.setParentQuestionGroup(parentQuestionGroup);
	}

	public static Predicate COMPOSITE_QUESTION_PREDICATE = new CompositeQuestionPredicate();

	public static Comparator<NewQuestionGroup> QUESTION_GROUP_COMPARATOR = new QuestionGroupComparator();

	protected static class CompositeQuestionPredicate implements Predicate {
		public boolean evaluate(Object arg0) {
			return ((NewQuestion) arg0).isComposite();
		}
	}

	protected static class QuestionGroupComparator implements Comparator<NewQuestionGroup> {
		public int compare(NewQuestionGroup o1, NewQuestionGroup o2) {
			return o1.getName().compareTo(o2.getName());
		}
	}

	public static class AtomicQuestionComparator implements Comparator<NewQuestion> {
		private NewQuestionGroup questionGroup;

		public AtomicQuestionComparator(NewQuestionGroup questionGroup) {
			super();
			this.questionGroup = questionGroup;
		}

		public int compare(NewQuestion o1, NewQuestion o2) {
			return o1.getPosition(questionGroup).compareTo(o2.getPosition(questionGroup));
		}
	}

	public List<NewQuestionGroup> getParentQuestionGroups() {
		List<NewQuestionGroup> questions = new ArrayList<NewQuestionGroup>();

		for (NewGroupElement groupElement : getParentElements()) {
			questions.add(groupElement.getParent());
		}

		return questions;
	}

	public int getParentQuestionGroupsCount() {
		return getParentQuestionGroups().size();
	}

	public Integer getPosition(NewQuestionGroup questionGroup) {
		return this.getParentElement(questionGroup).getPosition();
	}

	public NewGroupElement getParentElement(NewQuestionGroup questionGroup) {
		for (NewGroupElement groupElement : getParentElements()) {
			if (groupElement.getParent().equals(questionGroup)) {
				return groupElement;
			}
		}

		return null;
	}

	public Party getOwner() {
		if (this.getParentElementsCount() == 0) {
			return null;
		}

		return this.getParentElements().get(0).getParent().getOwner();
	}

	public List<NewPermissionUnit> getPermissionUnit(Party party) {
		List<NewPermissionUnit> permissionUnits = new ArrayList<NewPermissionUnit>();

		for (NewPermissionUnit permissionUnit : getPermissionUnits()) {
			if (permissionUnit.getParty().equals(party)) {
				permissionUnits.add(permissionUnit);
			}
		}

		return permissionUnits;
	}

	public void setParentQuestionGroup(NewQuestionGroup parentQuestionGroup) {
		if (this.checkHierarchy(parentQuestionGroup, this)) {
			throw new DomainException("could.not.associate");
		}

		NewGroupElement parentGroupElement = new NewGroupElement();

		parentGroupElement.setChild(this);
		parentGroupElement.setParent(parentQuestionGroup);
		parentGroupElement.setPosition(this.getNewPosition(parentQuestionGroup));
	}

	/**
	 * Checks if the question is a sub question of an all group
	 * 
	 * @return true if the question belongs of non composite question
	 */
	public boolean isBelongsToAllGroup() {
		// Filter out the no parents case
		if (this.getParentQuestionGroupsCount() == 0) {
			return false;
		}

		return !this.getParentQuestionGroups().get(0).isComposite();
	}

	/**
	 * Runs a check agains the hierarchy of parent to determine if child can be
	 * added to parent
	 * 
	 * @param parent
	 *            the parent to be checked
	 * @param child
	 *            the child to be added
	 * @return false if and only if the child can be added
	 */
	private boolean checkHierarchy(NewQuestionGroup parent, NewQuestion child) {
		if (child.isBelongsToAllGroup()) {
			return true;
		}

		if (parent.getChildQuestions().contains(child)) {
			return true;
		}

		Stack<NewQuestionGroup> stack = new Stack<NewQuestionGroup>();

		stack.add(parent);

		while (stack.size() > 0) {
			NewQuestionGroup top = stack.pop();

			if (top.equals(child)) {
				return true;
			}

			stack.addAll(top.getParentQuestionGroups());
		}

		return false;
	}

	@Override
	public void delete() {
		for (NewGroupElement parentElement : this.getParentElements()) {
			NewQuestionGroup questionGroup = parentElement.getParent();

			this.removeParentElements(parentElement);
			parentElement.deleteUp();

			questionGroup.resortChildQuestions();
		}

		for (NewPermissionUnit permissionUnit : this.getPermissionUnits()) {
			permissionUnit.delete();
			this.removePermissionUnits(permissionUnit);
		}

		for (NewModelRestriction modelRestriction : this.getModelRestrictions()) {
			modelRestriction.delete();
		}

		super.delete();
	}

	public abstract Integer getNewPosition(NewQuestionGroup parentQuestionGroup);

	public NewQuestionBank getQuestionBank() {
		return this.getParentQuestionGroups().get(0).getQuestionBank();
	}

	public List<NewQuestionGroup> getAssociableParents() {
		NewQuestionBank questionBank = this.getQuestionBank();

		Set<NewQuestionGroup> possibleParents = new HashSet<NewQuestionGroup>();

		Stack<NewQuestionGroup> stack = new Stack<NewQuestionGroup>();

		stack.add(questionBank);

		while (stack.size() > 0) {
			NewQuestionGroup parent = stack.pop();

			if (!parent.equals(this)) {
				stack.addAll(parent.getChildQuestionGroups());

				if (!parent.getChildQuestions().contains(this)) {
					possibleParents.add(parent);
				}
			}
		}

		return new ArrayList<NewQuestionGroup>(possibleParents);
	}

	public abstract NewQuestionType getQuestionType();

	public List<NewTestModel> getTestModels() {
		Set<NewTestModel> testModels = new HashSet<NewTestModel>();

		for (NewModelRestriction modelRestriction : this.getModelRestrictions()) {
			testModels.add(modelRestriction.getTestModel());
		}

		return new ArrayList<NewTestModel>(testModels);
	}

	public abstract boolean isComposite();

	public abstract List<PredicateType> getPredicates();

	protected static final List<PredicateType> preCondictionPredicates = new ArrayList<PredicateType>();

	static {
		preCondictionPredicates.add(PredicateType.OUTCOME_BY_CORRECTOR);
		preCondictionPredicates.add(PredicateType.OUTCOME_BY_NO_CORRECTOR);
		preCondictionPredicates.add(PredicateType.GIVEN_UP_QUESTION);
		preCondictionPredicates.add(PredicateType.ANSWERED_QUESTION);
	}

	public List<PredicateType> getPreConditionPredicates() {
		return preCondictionPredicates;
	}

	public NewAllGroup getTopAllGroup() {
		NewAllGroup allGroup = getParentAllGroup();
		while (!allGroup.isTopAllGroup()) {
			allGroup = allGroup.getParentAllGroup();
		}

		return allGroup;
	}

	protected NewAllGroup getParentAllGroup() {
		return (NewAllGroup) this.getParentQuestionGroups().get(0);
	}

	@Override
	public List<Integer> getPath() {
		if (this.getSection() != null) {
			return super.getPath();
		}

		return getAllGroupPath();
	}

	private List<Integer> getAllGroupPath() {
		List<Integer> path = new ArrayList<Integer>();

		NewAllGroup parent = this.getParentAllGroup();
		path.add(this.getPosition(parent));

		while (!parent.isTopAllGroup()) {
			path.add(0, parent.getPosition(parent.getParentAllGroup()));
			parent = parent.getParentAllGroup();
		}

		return path;
	}

	@Override
	protected void initCopy(NewTestElement testElement, HashMap<Object, Object> transformationMap) {
		super.initCopy(testElement, transformationMap);

		NewQuestion question = (NewQuestion) testElement;

		question.setPreCondition(this.getPreCondition());
	}

	public abstract List<NewQuestion> provide(int count);

	@Override
	public void cleanTransformation(HashMap<Object, Object> transformationMap) {
		if (this.getPreCondition() != null) {
			this.setPreCondition(this.getPreCondition().transform(transformationMap));
		}
	}

	public boolean belongsToGradeCompleteGroup() {
		NewAllGroup allGroup = this.getParentAllGroup();

		for (NewQuestion question : allGroup.getChildAtomicQuestions()) {

			if (question.getGrade() == null) {
				return false;
			}
		}

		return true;
	}
	
	public boolean evaluatePreCondition(Person person) {
		if(this.getPreCondition() == null) {
			return true;
		}
		
		return this.getPreCondition().evaluate(this, person);
	}
}
