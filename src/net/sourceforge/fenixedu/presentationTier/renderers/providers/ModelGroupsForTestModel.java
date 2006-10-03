package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.TestModelBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class ModelGroupsForTestModel implements DataProvider {

	public Object provide(Object source, Object currentValue) {
		TestModelBean bean = (TestModelBean) source;

		return bean.getTestModel().getAllChildModelGroups();

	}

	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
