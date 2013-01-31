package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.ContextSelectionBean;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionDegreeForAcademicSemesterSelectionBeanProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {

		ContextSelectionBean contextSelectionBean = (ContextSelectionBean) source;
		List<ExecutionDegree> executionDegrees =
				ExecutionDegree.filterByAcademicInterval(contextSelectionBean.getAcademicInterval());

		Collections.sort(executionDegrees, ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);

		return executionDegrees;
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
