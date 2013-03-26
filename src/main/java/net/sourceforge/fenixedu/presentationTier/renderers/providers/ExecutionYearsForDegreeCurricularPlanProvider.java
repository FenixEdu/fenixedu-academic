package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.interfaces.HasDegreeCurricularPlan;

import org.apache.commons.collections.comparators.ReverseComparator;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionYearsForDegreeCurricularPlanProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        HasDegreeCurricularPlan bean = (HasDegreeCurricularPlan) source;
        return getExecutionYears(bean);
    }

    public static List<ExecutionYear> getExecutionYears(HasDegreeCurricularPlan bean) {
        List<ExecutionYear> executionYears = new ArrayList<ExecutionYear>();
        for (ExecutionYear year : RootDomainObject.getInstance().getExecutionYears()) {
            if (year.isInclusivelyBetween(bean.getDegreeCurricularPlan().getInauguralExecutionYear(), bean
                    .getDegreeCurricularPlan().getLastExecutionYear())) {
                executionYears.add(year);
            }
        }
        Collections.sort(executionYears, new ReverseComparator());
        return executionYears;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
