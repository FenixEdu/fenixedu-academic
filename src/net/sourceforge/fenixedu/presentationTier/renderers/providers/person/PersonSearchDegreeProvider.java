package net.sourceforge.fenixedu.presentationTier.renderers.providers.person;

import java.util.ArrayList;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.person.FindPersonBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class PersonSearchDegreeProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        FindPersonBean bean = (FindPersonBean) source;
        final DegreeType degreeType = bean.getDegreeType();
        return new ArrayList<Degree>(Degree.readAllByDegreeType(degreeType));
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
