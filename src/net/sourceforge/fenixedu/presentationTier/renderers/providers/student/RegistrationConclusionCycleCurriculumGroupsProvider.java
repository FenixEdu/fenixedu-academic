package net.sourceforge.fenixedu.presentationTier.renderers.providers.student;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class RegistrationConclusionCycleCurriculumGroupsProvider implements DataProvider {

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

    public Object provide(Object source, Object currentValue) {
	final RegistrationConclusionBean registrationConclusionBean = (RegistrationConclusionBean) source;

	return registrationConclusionBean.getRegistration().getLastStudentCurricularPlan().getInternalCycleCurriculumGrops();

    }

}
