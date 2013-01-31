package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.RegistrationProtocol;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class RegistrationProtocolsProvider implements DataProvider {

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

	@Override
	public Object provide(Object source, Object currentValue) {
		List<RegistrationProtocol> registrationProtocolsSet = new ArrayList<RegistrationProtocol>();
		registrationProtocolsSet.addAll(RootDomainObject.getInstance().getRegistrationProtocols());
		Collections.sort(registrationProtocolsSet, RegistrationProtocol.AGREEMENT_COMPARATOR);
		return registrationProtocolsSet;
	}

}
