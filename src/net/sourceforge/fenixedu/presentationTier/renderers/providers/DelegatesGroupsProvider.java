package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.presentationTier.Action.delegate.GroupsBean;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DelegatesGroupsProvider implements DataProvider {

	@Override
	public Converter getConverter() {
		return null;
	}

	@Override
	public Object provide(Object source, Object currentValue) {
		final GroupsBean bean = (GroupsBean) source;
		return bean.getGroups();
	}

}
