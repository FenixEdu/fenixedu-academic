package net.sourceforge.fenixedu.presentationTier.renderers.providers.student;

import java.util.Collection;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.student.IRegistrationBean;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class RegistrationInternalCycleCurriculumGroupsProvider implements DataProvider {

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

    public Object provide(Object source, Object currentValue) {
	final IRegistrationBean iRegistrationBean = (IRegistrationBean) source;

	final Collection<CycleCurriculumGroup> result = new TreeSet<CycleCurriculumGroup>(CycleCurriculumGroup.COMPARATOR_BY_CYCLE_TYPE_AND_ID);
	result.addAll(iRegistrationBean.getRegistration().getLastStudentCurricularPlan().getInternalCycleCurriculumGrops());
	
	return result;
    }

}
