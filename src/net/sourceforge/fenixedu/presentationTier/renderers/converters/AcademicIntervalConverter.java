package net.sourceforge.fenixedu.presentationTier.renderers.converters;

import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import pt.ist.fenixWebFramework.renderers.components.converters.BiDirectionalConverter;

public class AcademicIntervalConverter extends BiDirectionalConverter {

    @Override
    public Object convert(Class type, Object value) {
        return AcademicInterval.getAcademicIntervalFromResumedString((String) value);
    }

    @Override
    public String deserialize(Object object) {
        AcademicInterval academicInterval = (AcademicInterval) object;
        return academicInterval.getResumedRepresentationInStringFormat();
    }
}
