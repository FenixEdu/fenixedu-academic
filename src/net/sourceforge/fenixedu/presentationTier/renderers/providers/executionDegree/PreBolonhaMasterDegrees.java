package net.sourceforge.fenixedu.presentationTier.renderers.providers.executionDegree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanComparator;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class PreBolonhaMasterDegrees implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final List<Degree> result = new ArrayList<Degree>();
	Set<Degree> degreesSet = RootDomainObject.getInstance().getDegreesSet();
	for (Degree degree : degreesSet) {
	    if (degree.getTipoCurso().equals(DegreeType.MASTER_DEGREE)) {
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
