package net.sourceforge.fenixedu.presentationTier.Action.phd.coordinator.providers;

import java.util.Collection;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.phd.ManageEnrolmentsBean;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

import org.apache.commons.collections.comparators.ReverseComparator;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CurricularCourseDegreeExecutionSemesterProvider extends AbstractDomainObjectProvider {

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
        final ManageEnrolmentsBean bean = (ManageEnrolmentsBean) source;

        final Collection<ExecutionSemester> result = new TreeSet<ExecutionSemester>(new ReverseComparator());

        for (final ExecutionYear executionYear : bean.getCurricularCourse().getDegreeCurricularPlan().getExecutionYears()) {
            result.addAll(executionYear.getExecutionPeriods());
        }

        return result;
    }
}