package net.sourceforge.fenixedu.presentationTier.renderers.providers.alumni;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniMailSendToBean;
import net.sourceforge.fenixedu.domain.Degree;

import org.apache.commons.collections.comparators.ComparableComparator;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AlumniSearchDegreeProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {

		AlumniMailSendToBean bean = (AlumniMailSendToBean) source;
		final List<Degree> degrees = new ArrayList<Degree>(Degree.readAllByDegreeType(bean.getDegreeType()));
		Collections.sort(degrees, new ComparableComparator());
		return degrees;
	}

	@Override
	public Converter getConverter() {
		return null;
	}

}
