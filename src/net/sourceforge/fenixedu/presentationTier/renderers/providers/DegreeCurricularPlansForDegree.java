package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementBaseBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeCurricularPlansForDegree implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

	final MarkSheetManagementBaseBean markSheetManagementBean = (MarkSheetManagementBaseBean) source;
	final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();

	if (markSheetManagementBean.getDegree() != null && markSheetManagementBean.getExecutionPeriod() != null) {

	    Set<Degree> availableDegrees = AcademicAuthorizationGroup.getDegreesForOperation(AccessControl.getPerson(),
		    AcademicOperationType.MANAGE_MARKSHEETS);

	    for (final DegreeCurricularPlan dcp : markSheetManagementBean.getDegree().getDegreeCurricularPlansSet()) {
		if (availableDegrees.contains(dcp.getDegree())) {
		    if (hasExecutionDegreeForYear(markSheetManagementBean, dcp)
			    || canSubmitImprovementMarksheets(markSheetManagementBean, dcp)) {
			result.add(dcp);
		    }
		}
	    }
	}
	Collections.sort(result, DegreeCurricularPlan.COMPARATOR_BY_NAME);
	return result;
    }

    private boolean canSubmitImprovementMarksheets(final MarkSheetManagementBaseBean markSheetManagementBean,
	    final DegreeCurricularPlan degreeCurricularPlan) {
	return degreeCurricularPlan.canSubmitImprovementMarkSheets(markSheetManagementBean.getExecutionPeriod()
		.getExecutionYear());
    }

    private boolean hasExecutionDegreeForYear(final MarkSheetManagementBaseBean markSheetManagementBean,
	    final DegreeCurricularPlan degreeCurricularPlan) {
	return degreeCurricularPlan.hasAnyExecutionDegreeFor(markSheetManagementBean.getExecutionYear());
    }

    @Override
    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }
}
