package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.TestModelBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ModelGroupsForTestModel implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		TestModelBean bean = (TestModelBean) source;

		return bean.getTestModel().getAllChildModelGroups();

	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
