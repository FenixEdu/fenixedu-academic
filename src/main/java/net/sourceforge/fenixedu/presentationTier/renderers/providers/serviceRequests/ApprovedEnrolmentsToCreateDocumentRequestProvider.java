package net.sourceforge.fenixedu.presentationTier.renderers.providers.serviceRequests;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.InternalEnrolmentWrapper;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ApprovedEnrolmentsToCreateDocumentRequestProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
        final DocumentRequestCreateBean documentRequestCreateBean = (DocumentRequestCreateBean) source;

        Set<Degree> degrees =
                AcademicAuthorizationGroup.getDegreesForOperation(AccessControl.getPerson(),
                        AcademicOperationType.SERVICE_REQUESTS);
        SortedSet<Enrolment> aprovedEnrolments = new TreeSet<Enrolment>(Enrolment.COMPARATOR_BY_NAME_AND_ID);
        for (Degree degree : degrees) {
            for (final Registration registration : documentRequestCreateBean.getStudent().getRegistrationsFor(degree)) {
                aprovedEnrolments.addAll(registration.getApprovedEnrolments());
            }
        }
        documentRequestCreateBean.setEnrolments(new ArrayList<Enrolment>(aprovedEnrolments));
        filter(aprovedEnrolments, documentRequestCreateBean);

        return aprovedEnrolments;
    }

    private void filter(final SortedSet<Enrolment> aprovedEnrolments, DocumentRequestCreateBean documentRequestCreateBean) {
        final CycleType cycleType = documentRequestCreateBean.getRequestedCycle();
        if (cycleType != null) {
            final Iterator<Enrolment> elements = aprovedEnrolments.iterator();
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
