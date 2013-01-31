package net.sourceforge.fenixedu.presentationTier.Action.phd.coordinator.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.phd.ManageEnrolmentsBean;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

import org.apache.commons.collections.comparators.ReverseComparator;

public class PhdManageEnrolmentsExecutionSemestersProvider extends AbstractDomainObjectProvider {

	@Override
	public Object provide(Object source, Object obj) {
		final ManageEnrolmentsBean bean = (ManageEnrolmentsBean) source;

		final List<ExecutionSemester> result = new ArrayList<ExecutionSemester>();

		ExecutionSemester each = bean.getProcess().getExecutionYear().getFirstExecutionPeriod();
		while (each != null) {
			result.add(each);
			each = each.getNextExecutionPeriod();
		}

		Collections.sort(result, new ReverseComparator(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR));

		return result;
	}
}
