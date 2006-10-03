package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.PredicateBean;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.converters.EnumConverter;

public class PredicateTypesForQuestion implements DataProvider {

	public Object provide(Object source, Object currentValue) {
		PredicateBean predicateBean = (PredicateBean) source;
		NewQuestion question = (NewQuestion) predicateBean.getQuestion();
		return question.getPredicates();
	}

	public Converter getConverter() {
		return new EnumConverter();
	}

}
