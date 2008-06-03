package net.sourceforge.fenixedu.presentationTier.renderers.providers.markSheet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementBaseBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeTypeDegreesManager implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final MarkSheetManagementBaseBean markSheetManagementBean = (MarkSheetManagementBaseBean) source;
	if(markSheetManagementBean.getExecutionPeriod() != null) {
	    List<Degree> res = new ArrayList<Degree>();
	    res.addAll(RootDomainObject.getInstance().getDegreesSet());
	    Collections.sort(res, Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
	    return res;
	}
	
	return Collections.emptySet();

    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
