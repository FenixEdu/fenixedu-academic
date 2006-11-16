/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - Ângela Almeida (argelina@ist.utl.pt)
 * 
 */
public class ExecutionYearsForRegistrationProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	return ((DocumentRequestCreateBean) source).getRegistration().getEnrolmentsExecutionYears();
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
