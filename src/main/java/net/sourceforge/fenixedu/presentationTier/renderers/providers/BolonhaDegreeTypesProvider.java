package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class BolonhaDegreeTypesProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        final List<DegreeType> notEmptyBolonhaValues = new ArrayList<DegreeType>(DegreeType.NOT_EMPTY_BOLONHA_VALUES);
        Collections.sort(notEmptyBolonhaValues);
        return notEmptyBolonhaValues;
    }

    @Override
    public Converter getConverter() {
        return new EnumConverter();
    }

}
