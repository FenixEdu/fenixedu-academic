package net.sourceforge.fenixedu.domain.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class NewModelRestriction extends NewModelRestriction_Base implements Positionable {

	public NewModelRestriction() {
		super();
		
		this.setCount(1);
	}

	public NewModelRestriction(NewQuestion question, NewModelGroup modelGroup) {
		this();
		
		NewModelRestriction modelRestriction = modelGroup.getTestModel().findModelRestriction(question);

		if (modelRestriction != null) {
			modelRestriction.delete();
		}

		init(modelGroup);
		this.setQuestion(question);
	}

	protected void init(NewModelGroup parentGroup) {
		this.setParentGroup(parentGroup);
		this.setPosition(parentGroup.getChildRestrictionsCount());
	}

	public void delete() {
		NewModelGroup parentGroup = this.getParentGroup();
		if (parentGroup != null) {
			this.removeParentGroup();
			parentGroup.resortChildRestrictions();
		}

		this.removeQuestion();

		super.delete();
	}

	public NewTestModel getTestModel() {
		return this.getParentGroup().getTestModel();
	}

	public int getQuestionCount() {
		return 1;
	}

	public List<Integer> getPositionPath() {
		List<Integer> path = new ArrayList<Integer>();

		NewTestModel testModel = this.getTestModel();

		NewModelRestriction modelRestriction = this;

		while (!modelRestriction.equals(testModel)) {
			path.add(modelRestriction.getPosition());

			modelRestriction = modelRestriction.getParentGroup();
		}

		Collections.reverse(path);

		return path;
	}

	public boolean isComposite() {
		return false;
	}

	public boolean isFirst() {
		return this.getPosition() == 1;
	}

	public boolean isLast() {
		return this.getPosition() == this.getParentGroup().getChildRestrictionsCount();
	}

	public void switchPosition(Integer relativePosition) {
		int currentPosition = this.getPosition();
		int newPosition = currentPosition + relativePosition;
		NewModelGroup modelGroup = this.getParentGroup();

		if (relativePosition < 0 && this.isFirst()) {
			throw new DomainException("could.not.sort.up");
		}

		if (relativePosition > 0 && this.isLast()) {
			throw new DomainException("could.not.sort.down");
		}

		for (NewModelRestriction modelRestriction : modelGroup.getChildRestrictions()) {
			if (modelRestriction.getPosition() == newPosition) {
				modelRestriction.setPosition(currentPosition);
				break;
			}
		}

		this.setPosition(newPosition);
	}

	@Override
	public NewTestElement copy(HashMap<Object, Object> trasformationMap) {
		throw new IllegalArgumentException("class.not.clonable");
	}

	@Override
	public void cleanTransformation(HashMap<Object, Object> trasformationMap) {
		//Left blank on purpose
	}
	
	public NewModelRestriction findModelRestriction(NewQuestion question) {
		if(this.getQuestion().equals(question)) {
			return this;
		}
		
		return null;
	}

}
