/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationSelectExecutionYearBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - Ângela Almeida (argelina@ist.utl.pt)
 * 
 */
public class ExecutionYearsForRegistrationProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        return ((RegistrationSelectExecutionYearBean) source).getRegistration().getSortedEnrolmentsExecutionYears();
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
