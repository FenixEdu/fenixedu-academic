package net.sourceforge.fenixedu.presentationTier.util;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;

import org.apache.struts.util.LabelValueBean;

public class LabelValueBeanUtils {

	public static LabelValueBean executionPeriodLabelValueBean(final ExecutionPeriod executionPeriod) {
		if (executionPeriod != null) {
			return new LabelValueBean(executionPeriod.getQualifiedName(), executionPeriod.getIdInternal().toString());
		}
		return null;
	}

}
