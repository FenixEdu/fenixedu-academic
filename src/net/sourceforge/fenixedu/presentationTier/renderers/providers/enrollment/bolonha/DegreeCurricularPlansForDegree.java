package net.sourceforge.fenixedu.presentationTier.renderers.providers.enrollment.bolonha;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.BolonhaStudentOptionalEnrollmentBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.BeanComparator;

public class DegreeCurricularPlansForDegree implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final BolonhaStudentOptionalEnrollmentBean optionalEnrollmentBean = (BolonhaStudentOptionalEnrollmentBean) source;

	final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
	if (optionalEnrollmentBean.hasDegree() && optionalEnrollmentBean.hasDegreeType()) {
	    if (optionalEnrollmentBean.getDegree().getTipoCurso() == optionalEnrollmentBean
		    .getDegreeType()) {
		result.addAll(optionalEnrollmentBean.getDegree().getDegreeCurricularPlansSet());
	    } else {
		optionalEnrollmentBean.setDegree(null);
		optionalEnrollmentBean.setDegreeType(null);
	    }
	}

	Collections.sort(result, new BeanComparator("name"));

	final DegreeCurricularPlan currentSelectedDegreeCurricularPlan = (DegreeCurricularPlan) currentValue;
	if (!result.contains(currentSelectedDegreeCurricularPlan)) {
	    optionalEnrollmentBean.setDegreeCurricularPlan(null);
	}

	return result;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
