package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementBaseBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeCurricularPlansForDegree implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	final MarkSheetManagementBaseBean markSheetManagementBean = (MarkSheetManagementBaseBean) source;
	final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();

	if (markSheetManagementBean.getDegree() != null && markSheetManagementBean.getExecutionPeriod() != null) {

	    final Campus employeeCampus = getEmployeeCampus();

	    if (employeeCampus != null) {
		ExecutionSemester executionSemester = ExecutionSemester.readBySemesterAndExecutionYear(1, "2006/2007");

		if (markSheetManagementBean.getExecutionPeriod().isBeforeOrEquals(executionSemester)) {

		    if (!employeeCampus.getName().equalsIgnoreCase("Taguspark")) {
			for (DegreeCurricularPlan degreeCurricularPlan : markSheetManagementBean.getDegree()
				.getDegreeCurricularPlansSet()) {
			    if (degreeCurricularPlan.getExecutionDegreeByYear(markSheetManagementBean.getExecutionPeriod()
				    .getExecutionYear()) != null
				    || canSubmitImprovementMarksheets(markSheetManagementBean, degreeCurricularPlan)) {
				result.add(degreeCurricularPlan);
			    }
			}
		    }

		} else {
		    for (final DegreeCurricularPlan dcp : markSheetManagementBean.getDegree().getDegreeCurricularPlansSet()) {

			if (isDEAAvailableToSubmitMarksheetForMasterDegree(markSheetManagementBean, dcp)
				|| hasExecutionDegreeForYearAndCampus(markSheetManagementBean, employeeCampus, dcp)
				|| canSubmitImprovementMarksheets(markSheetManagementBean, dcp)) {

			    result.add(dcp);
			}
		    }
		}
	    }
	}
	Collections.sort(result, DegreeCurricularPlan.COMPARATOR_BY_NAME);
	return result;
    }

    /*
     * MasterDegree office only exists in one campus, so must always appear if
     * has execution degree
     * 
     * One solution: - AdministrativeOffice could have all managed campus, and
     * we could search for any execution degree in any employee campus
     */
    private boolean isDEAAvailableToSubmitMarksheetForMasterDegree(final MarkSheetManagementBaseBean bean,
	    final DegreeCurricularPlan dcp) {
	return dcp.getDegree().isDEA() && isMasterDegreeEmployee() && dcp.hasExecutionDegreeFor(bean.getExecutionYear());
    }

    private boolean canSubmitImprovementMarksheets(final MarkSheetManagementBaseBean markSheetManagementBean,
	    final DegreeCurricularPlan degreeCurricularPlan) {
	return degreeCurricularPlan.canSubmitImprovementMarkSheets(markSheetManagementBean.getExecutionPeriod()
		.getExecutionYear());
    }

    private boolean hasExecutionDegreeForYearAndCampus(final MarkSheetManagementBaseBean markSheetManagementBean,
	    final Campus employeeCampus, final DegreeCurricularPlan degreeCurricularPlan) {
	return degreeCurricularPlan.getExecutionDegreeByYearAndCampus(markSheetManagementBean.getExecutionPeriod()
		.getExecutionYear(), employeeCampus) != null;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

    public Campus getEmployeeCampus() {
	return AccessControl.getPerson().getEmployee().getCurrentCampus();
    }

    private boolean isMasterDegreeEmployee() {
	final AdministrativeOffice office = AccessControl.getPerson().getEmployee().getAdministrativeOffice();
	return office != null && office.getAdministrativeOfficeType() == AdministrativeOfficeType.MASTER_DEGREE;
    }
}
