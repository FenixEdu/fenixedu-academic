/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.providers.student;

import java.util.Collections;

import net.sourceforge.fenixedu.dataTransferObject.student.ChooseStudentCurricularPlanBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StudentCurricularPlansProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	ChooseStudentCurricularPlanBean bean = (ChooseStudentCurricularPlanBean) source;
	return bean.getRegistration() != null ? bean.getRegistration().getStudentCurricularPlans()
		: Collections.EMPTY_LIST;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
