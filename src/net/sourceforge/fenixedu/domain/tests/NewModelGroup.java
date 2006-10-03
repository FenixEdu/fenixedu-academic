package net.sourceforge.fenixedu.domain.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class NewModelGroup extends NewModelGroup_Base {

	public NewModelGroup() {
		super();
	}

	public NewModelGroup(NewModelGroup parentGroup, String name) {
		this();

		this.setName(name);

		init(parentGroup);
	}

	public NewModelRestriction getModelRestriction(NewQuestion question) {
		for (NewModelRestriction modelRestriction : this.getChildRestrictions()) {
			if (modelRestriction.getQuestion().equals(question)) {
				return modelRestriction;
			}
		}

		return null;
	}

	@Override
	public NewModelRestriction findModelRestriction(NewQuestion question) {
		for (NewModelRestriction eachRestriction : this.getChildRestrictions()) {
			NewModelRestriction modelRestriction = eachRestriction.findModelRestriction(question);
			if(modelRestriction != null) {
				return modelRestriction;
			}
		}

		return null;
	}

	@Override
	public void delete() {
		final NewTestModel testModel = this.getTestModel();
		
		for(NewModelRestriction atomicRestriction : this.getChildAtomicRestrictions()) {
			testModel.unselectRestriction(atomicRestriction);
		}
		
		while (this.getChildRestrictionsCount() > 0) {
			this.getChildRestrictions().get(0).delete();
		}

		super.delete();
	}
	
	public void deleteAsBag() {
		this.getTestModel().removeBag();
		
		while (this.getChildRestrictionsCount() > 0) {
			this.getChildRestrictions().get(0).delete();
		}

		super.delete();
	}

	@Override
	public Double getValue() {
		double sum = 0;

		for (NewModelRestriction modelRestriction : this.getChildRestrictions()) {
			Double value = modelRestriction.getValue();

			if (value != null) {
				sum += value;
			}
		}

		return sum;
	}

	@Override
	public int getQuestionCount() {
		int sum = 0;
		for (NewModelRestriction modelRestriction : getChildRestrictions()) {
			sum += modelRestriction.getQuestionCount();
		}

		return sum;
	}

	@Override
	public NewTestModel getTestModel() {
		if (this.getBagModel() != null) {
			return this.getBagModel();
		}

		return this.getParentGroup().getTestModel();
	}

	public void resortChildRestrictions() {
		int i = 1;
		for (NewModelRestriction modelRestriction : this.getOrderedChildRestrictions()) {
			modelRestriction.setPosition(i++);
		}
	}

	public List<NewModelRestriction> getOrderedChildRestrictions() {
		List<NewModelRestriction> orderedChildRestrictions = new ArrayList<NewModelRestriction>(this
				.getChildRestrictions());

		Collections.sort(orderedChildRestrictions, Positionable.POSITION_COMPARATOR);

		return orderedChildRestrictions;
	}
	
	public List<NewModelGroup> getChildModelGroups() {
		List<NewModelGroup> orderedChildModelGroups = new ArrayList<NewModelGroup>();

		for (NewModelRestriction modelRestriction : this.getChildRestrictions()) {
			if (modelRestriction.isComposite()) {
				orderedChildModelGroups.add((NewModelGroup) modelRestriction);
			}
		}

		return orderedChildModelGroups;
	}

	public List<NewModelGroup> getOrderedChildModelGroups() {
		List<NewModelGroup> orderedChildModelGroups = getChildModelGroups();

		Collections.sort(orderedChildModelGroups, Positionable.POSITION_COMPARATOR);

		return orderedChildModelGroups;
	}

	public List<NewModelRestriction> getChildAtomicRestrictions() {
		List<NewModelRestriction> atomicRestrictions= new ArrayList<NewModelRestriction>();

		for (NewModelRestriction modelRestriction : this.getChildRestrictions()) {
			if (!modelRestriction.isComposite()) {
				atomicRestrictions.add(modelRestriction);
			}
		}

		return atomicRestrictions;
	}
	
	public List<NewQuestionGroup> getChildQuestionGroups() {
		List<NewQuestionGroup> childQuestionGroups = new ArrayList<NewQuestionGroup>();
		
		for(NewModelRestriction childAtomicRestriction : this.getChildAtomicRestrictions()) {
			if(childAtomicRestriction.getQuestion().isComposite()) {
				childQuestionGroups.add((NewQuestionGroup) childAtomicRestriction.getQuestion());
			}
		}
		
		return childQuestionGroups;
	}
	
	public int getChildQuestionGroupsCount() {
		return this.getChildQuestionGroups().size();
	}
	
	public List<NewQuestion> getChildAtomicQuestions() {
		List<NewQuestion> childAtomicQuestions = new ArrayList<NewQuestion>();
		
		for(NewModelRestriction childAtomicRestriction : this.getChildAtomicRestrictions()) {
			if(!childAtomicRestriction.getQuestion().isComposite()) {
				childAtomicQuestions.add(childAtomicRestriction.getQuestion());
			}
		}
		
		return childAtomicQuestions;
	}
	
	public int getChildAtomicQuestionsCount() {
		return this.getChildAtomicQuestions().size();
	}

	public List<NewModelGroup> getAllChildModelGroups() {
		Stack<NewModelGroup> stack = new Stack<NewModelGroup>();
		List<NewModelGroup> childModelGroups = new ArrayList<NewModelGroup>();

		stack.add(this);

		while (stack.size() > 0) {
			NewModelGroup modelGroup = stack.pop();
			childModelGroups.add(modelGroup);
			stack.addAll(modelGroup.getOrderedChildModelGroups());
		}

		return childModelGroups;
	}

	@Override
	public boolean isComposite() {
		return true;
	}

}
