package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ProfessionType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class ActiveProfessionTypeProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        List<ProfessionType> result = new ArrayList<ProfessionType>();
        for (ProfessionType professionType : ProfessionType.values()) {
            if (professionType.isActive()) {
                result.add(professionType);
            }
        }
        return result;
    }

    @Override
    public Converter getConverter() {
        return new EnumConverter();
    }

}
