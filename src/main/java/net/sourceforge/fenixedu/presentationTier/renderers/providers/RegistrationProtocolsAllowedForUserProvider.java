package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.RegistrationProtocol;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.bennu.core.security.Authenticate;

public class RegistrationProtocolsAllowedForUserProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
        final User userView = Authenticate.getUser();
        final Person supervisor = userView.getPerson();

        List<RegistrationProtocol> registrationProtocolsSet = new ArrayList<RegistrationProtocol>();
        registrationProtocolsSet.addAll(supervisor.getRegistrationProtocols());
        Collections.sort(registrationProtocolsSet, RegistrationProtocol.AGREEMENT_COMPARATOR);
        return registrationProtocolsSet;
    }

}
