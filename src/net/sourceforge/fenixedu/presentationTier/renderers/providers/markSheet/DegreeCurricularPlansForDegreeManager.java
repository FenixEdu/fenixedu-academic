package net.sourceforge.fenixedu.presentationTier.renderers.providers.markSheet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementBaseBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeCurricularPlansForDegreeManager implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

	final MarkSheetManagementBaseBean markSheetManagementBean = (MarkSheetManagementBaseBean) source;
	final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
	if (markSheetManagementBean.getDegree() != null && markSheetManagementBean.getExecutionPeriod() != null) {
	    result.addAll(markSheetManagementBean.getDegree().getDegreeCurricularPlansSet());
	}
	Collections.sort(result, new BeanComparator("name"));
	return result;
    }

    @Override
    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }
}
