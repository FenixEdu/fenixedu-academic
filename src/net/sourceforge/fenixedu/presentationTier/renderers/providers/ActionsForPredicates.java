package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.PredicateBean;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.PredicateBean.Action;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class ActionsForPredicates implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	PredicateBean predicateBean = (PredicateBean) source;
	List<Action> results = new ArrayList<Action>();

	for (Action action : Action.values()) {
	    results.add(action);
	}

	return results;
    }

    public Converter getConverter() {
	return new EnumConverter();
    }

}
