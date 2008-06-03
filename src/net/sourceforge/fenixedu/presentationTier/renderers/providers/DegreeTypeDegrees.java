package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementBaseBean;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeTypeDegrees implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final MarkSheetManagementBaseBean markSheetManagementBean = (MarkSheetManagementBaseBean) source;
	if(markSheetManagementBean.getExecutionPeriod() != null) {
	    return AccessControl.getPerson().getEmployee().getAdministrativeOffice()
	    .getAdministratedDegreesForMarkSheets();
	}
	
	return Collections.emptySet();

    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
