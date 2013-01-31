package net.sourceforge.fenixedu.presentationTier.renderers.providers.lists;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.ExecutionDegreeListBean;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.AcademicIntervalConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AcademicIntervalProviderForListsContextBean implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		final SortedSet<AcademicInterval> academicIntervals =
				new TreeSet<AcademicInterval>(AcademicInterval.REVERSE_COMPARATOR_BY_BEGIN_DATE);

		final ExecutionDegreeListBean executionDegreeBean = (ExecutionDegreeListBean) source;
		if (executionDegreeBean.getDegreeCurricularPlan() != null) {

			for (ExecutionDegree exeDegree : executionDegreeBean.getDegreeCurricularPlan().getExecutionDegrees()) {
				academicIntervals.add(exeDegree.getAcademicInterval());
			}
		}

		return academicIntervals;
	}

	@Override
	public Converter getConverter() {
		return new AcademicIntervalConverter();
	}
}
