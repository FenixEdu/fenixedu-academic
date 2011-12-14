package net.sourceforge.fenixedu.presentationTier.renderers.providers.serviceRequests;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.InternalEnrolmentWrapper;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ApprovedEnrolmentsToCreateDocumentRequestProvider implements DataProvider {

    public Converter getConverter() {
	return new DomainObjectKeyArrayConverter();
    }

    public Object provide(Object source, Object currentValue) {
	final DocumentRequestCreateBean documentRequestCreateBean = (DocumentRequestCreateBean) source;

	final AdministrativeOffice administrativeOffice = AccessControl.getPerson().getEmployeeAdministrativeOffice();
	final List<Enrolment> result = documentRequestCreateBean.getStudent().getApprovedEnrolments(administrativeOffice);
	filter(result, documentRequestCreateBean);
	Collections.sort(result, Enrolment.COMPARATOR_BY_NAME_AND_ID);

	return result;
    }

    private void filter(final List<Enrolment> result, DocumentRequestCreateBean documentRequestCreateBean) {
	final CycleType cycleType = documentRequestCreateBean.getRequestedCycle();
	if (cycleType != null) {
	    final Iterator<Enrolment> elements = result.iterator();
	    while (elements.hasNext()) {
		final Enrolment enrolment = elements.next();
		if (!hasCycleType(enrolment, cycleType) && !isSourceInAnyCycleGroup(enrolment, cycleType)) {
		    elements.remove();
		}
	    }
	}
    }

    private boolean hasCycleType(final Enrolment enrolment, final CycleType cycleType) {
	CycleCurriculumGroup cycleCurriculumGroup = enrolment.getParentCycleCurriculumGroup();
	return cycleCurriculumGroup != null && cycleCurriculumGroup.getCycleType() == cycleType;
    }

    private boolean isSourceInAnyCycleGroup(final Enrolment enrolment, final CycleType cycleType) {
	for (final InternalEnrolmentWrapper wrapper : enrolment.getEnrolmentWrappers()) {
	    if (wrapper.getCredits().hasAnyDismissalInCycle(cycleType)) {
		return true;
	    }
	}
	return false;
    }

}
