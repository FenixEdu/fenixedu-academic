package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.dataTransferObject.messaging.RegistrationsBean;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;


public class RegistrationProvider implements DataProvider{

    @Override
    public Converter getConverter() {
	
	return new DomainObjectKeyConverter();
    }

    @Override
    public Object provide(Object arg0, Object arg1) {
	RegistrationsBean bean = (RegistrationsBean) arg0;
	return bean.getRegistrations();
    }

    
}
