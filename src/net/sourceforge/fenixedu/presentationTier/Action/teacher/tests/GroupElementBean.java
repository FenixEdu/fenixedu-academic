package net.sourceforge.fenixedu.presentationTier.Action.teacher.tests;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.domain.tests.NewQuestionGroup;

public class GroupElementBean implements Serializable {

	DomainReference<NewQuestionGroup> parent;

	DomainReference<NewQuestion> child;

	public GroupElementBean(NewQuestion child) {
		super();

		this.setChild(child);
		this.setParent(null);
	}

	public NewQuestion getChild() {
		return child.getObject();
	}

	public void setChild(NewQuestion child) {
		this.child = new DomainReference<NewQuestion>(child);
	}

	public NewQuestionGroup getParent() {
		return parent.getObject();
	}

	public void setParent(NewQuestionGroup parent) {
		this.parent = new DomainReference<NewQuestionGroup>(parent);
	}

}
