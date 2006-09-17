package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.BeanComparator;

public class DegreeTypeDegrees implements DataProvider {

	@SuppressWarnings("unchecked")
    public Object provide(Object source, Object currentValue) {

        final List<Degree> result = new ArrayList<Degree>();
        for (Degree degree : RootDomainObject.getInstance().getDegreesSet()) {
            if (degree.getTipoCurso() == DegreeType.BOLONHA_DEGREE) {
                result.add(degree);
            }
        }
        Collections.sort(result, new BeanComparator("name"));
        return result;
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
