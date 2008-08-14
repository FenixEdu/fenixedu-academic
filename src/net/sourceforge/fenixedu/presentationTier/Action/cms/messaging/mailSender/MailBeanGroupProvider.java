package net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class MailBeanGroupProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	MailBean bean = (MailBean) source;
	return bean.getReceiversOptions();
    }

    public Converter getConverter() {
	return null;
    }

}
