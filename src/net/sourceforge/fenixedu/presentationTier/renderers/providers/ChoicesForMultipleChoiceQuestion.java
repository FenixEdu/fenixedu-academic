package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.domain.tests.NewMultipleChoiceQuestion;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ChoicesForMultipleChoiceQuestion implements DataProvider {

	public Object provide(Object source, Object currentValue) {
		NewMultipleChoiceQuestion multipleChoiceQuestion = (NewMultipleChoiceQuestion) source;
		return multipleChoiceQuestion.getOrderedChoices(true);
	}

	public Converter getConverter() {
		return new DomainObjectKeyArrayConverter();
	}

}
