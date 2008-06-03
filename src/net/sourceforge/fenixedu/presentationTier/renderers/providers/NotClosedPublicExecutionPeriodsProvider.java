package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class NotClosedPublicExecutionPeriodsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {        
        return ExecutionSemester.readNotClosedPublicExecutionPeriods();        
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();               
    }
}
