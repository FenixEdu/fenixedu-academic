package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationSelectExecutionYearBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class ExecutionYearsForAcademicServiceRequestProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final Set<ExecutionYear> result = ((RegistrationSelectExecutionYearBean) source).getRegistration().getSortedCurriculumLinesExecutionYears();
	return result.isEmpty() ? Collections.singletonList(ExecutionYear.readCurrentExecutionYear()) : result;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
