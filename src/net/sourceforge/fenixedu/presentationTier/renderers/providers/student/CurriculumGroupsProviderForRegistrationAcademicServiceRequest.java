package net.sourceforge.fenixedu.presentationTier.renderers.providers.student;

import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationSelectExecutionYearBean;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class CurriculumGroupsProviderForRegistrationAcademicServiceRequest implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final RegistrationSelectExecutionYearBean bean = ((RegistrationSelectExecutionYearBean) source);
	final StudentCurricularPlan studentCurricularPlan = bean.getRegistration().getLastStudentCurricularPlan();
	final Set<CurriculumGroup> curriculumGroups = studentCurricularPlan.getRoot().getAllCurriculumGroups();
	curriculumGroups.removeAll(studentCurricularPlan.getRoot().getCycleCurriculumGroups());
	return curriculumGroups;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
