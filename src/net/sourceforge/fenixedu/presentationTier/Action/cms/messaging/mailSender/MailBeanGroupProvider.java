package net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender;

import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class MailBeanGroupProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        MailBean bean = (MailBean) source;
        return bean.getReceiversOptions();
    }

    public Converter getConverter() {
        return null;
    }

}
