package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementBaseBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeCurricularPlansForDegree implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	final MarkSheetManagementBaseBean markSheetManagementBean = (MarkSheetManagementBaseBean) source;
	final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
	if (markSheetManagementBean.getDegree() != null && markSheetManagementBean.getExecutionPeriod() != null) {
	    Campus employeeCampus = getEmployeeCampus();
	    if (employeeCampus != null) {
		ExecutionSemester executionSemester = ExecutionSemester.readBySemesterAndExecutionYear(1, "2006/2007");

		if (markSheetManagementBean.getExecutionPeriod().isBeforeOrEquals(executionSemester)) {

		    if (!employeeCampus.getName().equalsIgnoreCase("Taguspark")) {
			for (DegreeCurricularPlan degreeCurricularPlan : markSheetManagementBean.getDegree()
				.getDegreeCurricularPlansSet()) {
			    if (degreeCurricularPlan.getExecutionDegreeByYear(markSheetManagementBean.getExecutionPeriod()
				    .getExecutionYear()) != null
				    || degreeCurricularPlan.canSubmitImprovementMarkSheets(markSheetManagementBean
					    .getExecutionPeriod().getExecutionYear())) {
				result.add(degreeCurricularPlan);
			    }
			}
		    }

		} else {
		    for (DegreeCurricularPlan degreeCurricularPlan : markSheetManagementBean.getDegree()
			    .getDegreeCurricularPlansSet()) {
			if (degreeCurricularPlan.getExecutionDegreeByYearAndCampus(markSheetManagementBean.getExecutionPeriod()
				.getExecutionYear(), employeeCampus) != null
				|| degreeCurricularPlan.canSubmitImprovementMarkSheets(markSheetManagementBean
					.getExecutionPeriod().getExecutionYear())) {
			    result.add(degreeCurricularPlan);
			}
		    }
		}
	    }
	}
	Collections.sort(result, DegreeCurricularPlan.COMPARATOR_BY_NAME);
	return result;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

    public Campus getEmployeeCampus() {
	return AccessControl.getPerson().getEmployee().getCurrentCampus();
    }

}
