package net.sourceforge.fenixedu.presentationTier.Action.teacher.tests;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.tests.NewQuestionGroup;
import net.sourceforge.fenixedu.domain.tests.NewQuestionType;

public class AtomicQuestionBean implements Serializable {
	private NewQuestionType questionType;

	private DomainReference<NewQuestionGroup> parentQuestionGroup;

	public AtomicQuestionBean(NewQuestionGroup parentQuestionGroup) {
		super();

		setParentQuestionGroup(parentQuestionGroup);
	}

	public NewQuestionType getQuestionType() {
		return questionType;
	}

	public void setQuestionType(NewQuestionType questionType) {
		this.questionType = questionType;
	}

	public NewQuestionGroup getParentQuestionGroup() {
		return parentQuestionGroup.getObject();
	}

	public void setParentQuestionGroup(NewQuestionGroup parentQuestionGroup) {
		this.parentQuestionGroup = new DomainReference<NewQuestionGroup>(parentQuestionGroup);
	}
}
