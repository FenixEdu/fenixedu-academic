package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementBaseBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
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
            final Set<Degree> result = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
            Set<Degree> availableDegrees =
                    AcademicAuthorizationGroup.getDegreesForOperation(AccessControl.getPerson(),
                            AcademicOperationType.MANAGE_MARKSHEETS);
            Set<Degree> degrees = new HashSet<Degree>();
            for (Degree degree : availableDegrees) {
                if (!degree.isBolonhaDegree()) {
                    degrees.add(degree);
                }
            }
            result.addAll(degrees);
            return result;
        }

        return Collections.emptySet();
    }

}
