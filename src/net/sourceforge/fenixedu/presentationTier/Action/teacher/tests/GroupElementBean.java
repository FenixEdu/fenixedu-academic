package net.sourceforge.fenixedu.presentationTier.Action.teacher.tests;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.domain.tests.NewQuestionGroup;

public class GroupElementBean implements Serializable {

	NewQuestionGroup parent;

	NewQuestion child;

	public GroupElementBean(NewQuestion child) {
		super();

		this.setChild(child);
		this.setParent(null);
	}

	public NewQuestion getChild() {
		return child;
	}

	public void setChild(NewQuestion child) {
		this.child = child;
	}

	public NewQuestionGroup getParent() {
		return parent;
	}

	public void setParent(NewQuestionGroup parent) {
		this.parent = parent;
	}

}
