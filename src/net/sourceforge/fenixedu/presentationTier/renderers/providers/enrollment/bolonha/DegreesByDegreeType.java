package net.sourceforge.fenixedu.presentationTier.renderers.providers.enrollment.bolonha;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.BolonhaStudentOptionalEnrollmentBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.BeanComparator;

public class DegreesByDegreeType implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	final BolonhaStudentOptionalEnrollmentBean optionalEnrollmentBean = (BolonhaStudentOptionalEnrollmentBean) source;
	List<Degree> result = null;
	if (optionalEnrollmentBean.hasDegreeType()) {
	    result = Degree.readAllByDegreeType(optionalEnrollmentBean.getDegreeType());
	    Collections.sort(result, new BeanComparator("name"));
	} else {
	    result = Collections.EMPTY_LIST;
	}

	final Degree currentSelectedDegree = (Degree) currentValue;
	if (!result.contains(currentSelectedDegree)) {
	    optionalEnrollmentBean.setDegree(null);
	}

	return result;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
