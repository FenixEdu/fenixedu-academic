package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationSelectExecutionYearBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionYearsFromRegistrationCurriculumLines implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	return ((RegistrationSelectExecutionYearBean) source).getRegistration().getSortedCurriculumLinesExecutionYears();
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
