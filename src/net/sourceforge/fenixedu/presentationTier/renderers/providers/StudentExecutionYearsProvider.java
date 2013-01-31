package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationSelectExecutionYearBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class StudentExecutionYearsProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		List<ExecutionYear> result =
				new ArrayList(((RegistrationSelectExecutionYearBean) source).getRegistration().getStudent()
						.getEnrolmentsExecutionYears());
		Collections.sort(result, new BeanComparator("year"));
		return result;
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
