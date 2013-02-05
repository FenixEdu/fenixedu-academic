package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.student.StudentStatuteType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class VisibleStudentStatuteTypeProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        List<StudentStatuteType> result = new ArrayList<StudentStatuteType>(StudentStatuteType.VISIBLE_STATUTES);
        return result;
    }

    @Override
    public Converter getConverter() {
        return new EnumConverter();
    }

}
