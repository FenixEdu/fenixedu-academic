package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class NotClosedPublicExecutionPeriodsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {        
        return ExecutionPeriod.readNotClosedPublicExecutionPeriods();        
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();               
    }
}
