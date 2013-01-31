package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.ContextSelectionBean;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CurricularYearForExecutionDegreeProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {

		ContextSelectionBean contextSelectionBean = (ContextSelectionBean) source;
		List<CurricularYear> result = new ArrayList<CurricularYear>();
		ExecutionDegree executionDegree = contextSelectionBean.getExecutionDegree();

		if (executionDegree != null) {
			Integer index = executionDegree.getDegreeCurricularPlan().getDegreeDuration();

			result.addAll(RootDomainObject.getInstance().getCurricularYears());
			Collections.sort(result, CurricularYear.CURRICULAR_YEAR_COMPARATORY_BY_YEAR);
			return index == null ? result : result.subList(0, index);
		}

		return result;
	}

	@Override
	public Converter getConverter() {
		return null;
	}

}
