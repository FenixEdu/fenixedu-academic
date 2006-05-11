package net.sourceforge.fenixedu.presentationTier.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;

import org.apache.struts.util.LabelValueBean;

public class LabelValueBeanUtils {

	public static LabelValueBean executionPeriodLabelValueBean(final ExecutionPeriod executionPeriod) {
		if (executionPeriod != null) {
			return new LabelValueBean(executionPeriod.getQualifiedName(), executionPeriod.getIdInternal().toString());
		}
		return null;
	}

	public static List<LabelValueBean> executionPeriodLabelValueBeans(final Collection<ExecutionPeriod> executionPeriods, final boolean reverse) {
		final int size = executionPeriods == null ? 0 : executionPeriods.size();
		final List<LabelValueBean> labelValueBeans = new ArrayList<LabelValueBean>(size);
		for (final ExecutionPeriod executionPeriod : executionPeriods) {
			labelValueBeans.add(executionPeriodLabelValueBean(executionPeriod));
		}
		if (reverse) {
			Collections.reverse(labelValueBeans);
		}
		return labelValueBeans;
	}

	public static List<LabelValueBean> executionPeriodLabelValueBeans(final Collection<ExecutionPeriod> executionPeriods) {
		return executionPeriodLabelValueBeans(executionPeriods, false);
	}

}
