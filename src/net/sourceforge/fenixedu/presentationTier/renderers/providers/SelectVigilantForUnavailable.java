package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.UnavailablePeriodBean;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class SelectVigilantForUnavailable implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {

		UnavailablePeriodBean bean = (UnavailablePeriodBean) source;
		VigilantGroup vigilantGroup = bean.getSelectedVigilantGroup();

		List<VigilantWrapper> vigilantWrappers = new ArrayList<VigilantWrapper>();
		if (vigilantGroup != null) {
			vigilantWrappers.addAll(vigilantGroup.getVigilantWrappers());
			Collections.sort(vigilantWrappers, new BeanComparator("person.name"));
		}
		return vigilantWrappers;

	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}