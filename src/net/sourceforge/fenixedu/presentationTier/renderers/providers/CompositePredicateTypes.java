package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.tests.predicates.CompositePredicate;
import net.sourceforge.fenixedu.domain.tests.predicates.CompositePredicateType;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.PredicateBean;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.converters.EnumConverter;

public class CompositePredicateTypes implements DataProvider {

	public Object provide(Object source, Object currentValue) {
		PredicateBean predicateBean = (PredicateBean) source;

		List<CompositePredicateType> compositePredicateTypes;
		List<CompositePredicateType> suggestedTypes = CompositePredicate.getCompositePredicateTypes();

		compositePredicateTypes = new ArrayList<CompositePredicateType>();

		switch (predicateBean.getPredicates().size()) {
		case 0:
			break;
		case 1:
			for (CompositePredicateType predicateType : suggestedTypes) {
				if (predicateType.isUnary()) {
					compositePredicateTypes.add(predicateType);
				}
			}
			break;
		default:
			compositePredicateTypes.addAll(suggestedTypes);
		}

		return compositePredicateTypes;
	}

	public Converter getConverter() {
		return new EnumConverter();
	}

}
