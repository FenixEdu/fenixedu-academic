package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.domain.tests.NewMultipleChoiceQuestion;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.PredicateBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class ChoicesForMultipleChoiceQuestionInPredicate implements DataProvider {

	public Object provide(Object source, Object currentValue) {
		PredicateBean predicateBean = (PredicateBean) source;
		NewMultipleChoiceQuestion multipleChoiceQuestion = (NewMultipleChoiceQuestion) predicateBean
				.getQuestion();
		return multipleChoiceQuestion.getOrderedChoices();
	}

	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
