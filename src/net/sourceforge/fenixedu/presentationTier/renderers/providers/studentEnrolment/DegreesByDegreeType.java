package net.sourceforge.fenixedu.presentationTier.renderers.providers.studentEnrolment;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentOptionalEnrolmentBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreesByDegreeType implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	StudentOptionalEnrolmentBean optionalEnrolmentBean = (StudentOptionalEnrolmentBean) source;

	if (optionalEnrolmentBean.getDegreeType() != null) {
	    List<Degree> result = Degree.readAllByDegreeType(optionalEnrolmentBean.getDegreeType());
	    Collections.sort(result, Degree.COMPARATOR_BY_NAME);
	    return result;
	} else {
	    optionalEnrolmentBean.setDegreeCurricularPlan(null);
	    return Collections.emptyList();
	}

    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
