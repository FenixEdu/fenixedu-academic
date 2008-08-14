package net.sourceforge.fenixedu.presentationTier.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;

import org.apache.struts.util.LabelValueBean;

public class LabelValueBeanUtils {

    public static LabelValueBean executionPeriodLabelValueBean(final ExecutionSemester executionSemester) {
	if (executionSemester != null) {
	    return new LabelValueBean(executionSemester.getQualifiedName(), executionSemester.getIdInternal().toString());
	}
	return null;
    }

    public static List<LabelValueBean> executionPeriodLabelValueBeans(final Collection<ExecutionSemester> executionSemesters,
	    final boolean reverse) {
	final int size = executionSemesters == null ? 0 : executionSemesters.size();
	final List<LabelValueBean> labelValueBeans = new ArrayList<LabelValueBean>(size);
	for (final ExecutionSemester executionSemester : executionSemesters) {
	    labelValueBeans.add(executionPeriodLabelValueBean(executionSemester));
	}
	if (reverse) {
	    Collections.reverse(labelValueBeans);
	}
	return labelValueBeans;
    }

    public static List<LabelValueBean> executionPeriodLabelValueBeans(final Collection<ExecutionSemester> executionSemesters) {
	return executionPeriodLabelValueBeans(executionSemesters, false);
    }

}
