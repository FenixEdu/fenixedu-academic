package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;

import org.apache.commons.beanutils.MethodUtils;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionPeriodsForExecutionYear implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        ExecutionYear executionYear;
        try {
            executionYear = (ExecutionYear) MethodUtils.invokeMethod(source, "getExecutionYear", null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        List<ExecutionSemester> periods = new ArrayList<ExecutionSemester>();
        if (executionYear != null) {
            periods.addAll(executionYear.getExecutionPeriods());
        }

        return periods;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
