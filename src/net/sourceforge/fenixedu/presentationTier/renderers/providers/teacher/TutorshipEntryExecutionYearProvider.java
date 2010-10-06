package net.sourceforge.fenixedu.presentationTier.renderers.providers.teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.StudentsPerformanceInfoBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class TutorshipEntryExecutionYearProvider implements DataProvider {

    public static class TutorshipEntryExecutionYearProviderForSingleStudent extends TutorshipEntryExecutionYearProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
	    StudentsPerformanceInfoBean bean = (StudentsPerformanceInfoBean) source;
	    return getExecutionYears(bean);
	}

	public static List<ExecutionYear> getExecutionYears(StudentsPerformanceInfoBean bean) {
	    Set<ExecutionYear> executionYears = new TreeSet<ExecutionYear>(ExecutionYear.REVERSE_COMPARATOR_BY_YEAR);
	    for (Tutorship tutor : bean.getTutorshipsFromStudent()) {
		executionYears.add(ExecutionYear.getExecutionYearByDate(tutor.getStudentCurricularPlan().getRegistration()
			.getStartDate()));
	    }
	    return new ArrayList<ExecutionYear>(executionYears);
	}
    }

    public Object provide(Object source, Object currentValue) {
	StudentsPerformanceInfoBean bean = (StudentsPerformanceInfoBean) source;
	return getExecutionYears(bean);
    }

    public static List<ExecutionYear> getExecutionYears(StudentsPerformanceInfoBean bean) {
	Set<ExecutionYear> executionYears = new TreeSet<ExecutionYear>(ExecutionYear.REVERSE_COMPARATOR_BY_YEAR);
	for (Tutorship tutor : bean.getTutorships()) {
	    if (bean.getDegree().equals(tutor.getStudentCurricularPlan().getRegistration().getDegree())) {
		executionYears.add(ExecutionYear.getExecutionYearByDate(tutor.getStudentCurricularPlan().getRegistration()
			.getStartDate()));
	    }
	}
	return new ArrayList<ExecutionYear>(executionYears);
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
