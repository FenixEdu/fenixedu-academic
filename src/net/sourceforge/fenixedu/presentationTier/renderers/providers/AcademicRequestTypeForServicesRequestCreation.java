package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.converters.EnumConverter;

public class AcademicRequestTypeForServicesRequestCreation implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        final Collection<AcademicServiceRequestType> result = new ArrayList<AcademicServiceRequestType>();
        result.add(AcademicServiceRequestType.REINGRESSION);
        result.add(AcademicServiceRequestType.EQUIVALENCE_PLAN);
        result.add(AcademicServiceRequestType.REVISION_EQUIVALENCE_PLAN);
        result.add(AcademicServiceRequestType.COURSE_GROUP_CHANGE_REQUEST);
        result.add(AcademicServiceRequestType.FREE_SOLICITATION_ACADEMIC_REQUEST);
        result.add(AcademicServiceRequestType.EXTRA_EXAM_REQUEST);
        return result;
    }

    public Converter getConverter() {
        return new EnumConverter();
    }

}
