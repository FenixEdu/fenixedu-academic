package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementBaseBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeTypesDegreeForOldMarkSheets implements DataProvider {

    @Override
    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
	final MarkSheetManagementBaseBean markSheetManagementBean = (MarkSheetManagementBaseBean) source;
	if (markSheetManagementBean.getExecutionPeriod() != null) {
	    Set<Degree> degrees = new HashSet<Degree>();
	    for (Degree degree : AccessControl.getPerson().getEmployee().getAdministrativeOffice()
		    .getAdministratedDegreesForMarkSheets()) {
		if (!degree.isBolonhaDegree()) {
		    degrees.add(degree);
		}
	    }
	    return degrees;
	}

	return Collections.emptySet();
    }

}
