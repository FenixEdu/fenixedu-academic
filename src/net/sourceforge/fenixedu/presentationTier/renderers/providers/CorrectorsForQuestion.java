package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import net.sourceforge.fenixedu.domain.tests.NewAllGroup;
import net.sourceforge.fenixedu.domain.tests.NewAtomicQuestion;
import net.sourceforge.fenixedu.domain.tests.NewCorrector;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.PredicateBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class CorrectorsForQuestion implements DataProvider {

	public Object provide(Object source, Object currentValue) {
		List<NewCorrector> correctors = new ArrayList<NewCorrector>();
		Stack<NewAllGroup> allGroups = new Stack<NewAllGroup>();

		PredicateBean predicateBean = (PredicateBean) source;

		allGroups.add(predicateBean.getQuestion().getTopAllGroup());

		while (allGroups.size() > 0) {
			NewAllGroup allGroup = allGroups.pop();

			for (NewQuestion question : allGroup.getChildAtomicQuestions()) {
				if (NewAllGroup.ALL_GROUP_PREDICATE.evaluate(question)) {
					allGroups.add((NewAllGroup) question);
				} else {
					NewAtomicQuestion atomicQuestion = (NewAtomicQuestion) question;
					correctors.addAll(atomicQuestion.getCorrectors());
				}
			}
		}

		return correctors;
	}

	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
