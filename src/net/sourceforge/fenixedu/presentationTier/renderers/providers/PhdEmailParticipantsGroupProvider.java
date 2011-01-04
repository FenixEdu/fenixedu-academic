package net.sourceforge.fenixedu.presentationTier.renderers.providers;


import net.sourceforge.fenixedu.domain.phd.PhdEmailBean;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class PhdEmailParticipantsGroupProvider implements DataProvider {

    public Object provide(final Object source, final Object currentValue) {
	final PhdEmailBean emailBean = (PhdEmailBean) source;
	return emailBean.getPossibleParticipantsGroups();
    }

    public Converter getConverter() {
	return null;
    }

}
