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

	BolonhaStudentOptionalEnrollmentBean optionalEnrollmentBean = (BolonhaStudentOptionalEnrollmentBean) source;

	if (optionalEnrollmentBean.hasDegreeType()) {
	    List<Degree> result = Degree.readAllByDegreeType(optionalEnrollmentBean.getDegreeType());
	    Collections.sort(result, new BeanComparator("name"));
	    return result;
	}

	return Collections.EMPTY_LIST;

    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
