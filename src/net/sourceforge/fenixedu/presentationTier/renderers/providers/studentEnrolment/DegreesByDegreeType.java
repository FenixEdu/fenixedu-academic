package net.sourceforge.fenixedu.presentationTier.renderers.providers.studentEnrolment;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentOptionalEnrolmentBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.BeanComparator;

public class DegreesByDegreeType implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	
	StudentOptionalEnrolmentBean optionalEnrolmentBean = (StudentOptionalEnrolmentBean) source;

        if(optionalEnrolmentBean.getDegreeType() != null) {
            List<Degree> result = Degree.readAllByDegreeType(optionalEnrolmentBean.getDegreeType());
            Collections.sort(result, new BeanComparator("name"));
            return result;
        } else {
            return Collections.emptyList();   
        }
        
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
