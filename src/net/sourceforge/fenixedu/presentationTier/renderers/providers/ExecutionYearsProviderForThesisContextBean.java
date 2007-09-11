package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.ThesisContextBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class ExecutionYearsProviderForThesisContextBean implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        ThesisContextBean bean = (ThesisContextBean) source;
        return bean.getExecutionYearPossibilities();
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
