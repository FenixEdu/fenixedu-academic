package net.sourceforge.fenixedu.presentationTier.renderers.providers.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationSelectExecutionYearBean;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CurriculumGroupsProviderForRegistrationAcademicServiceRequest implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final RegistrationSelectExecutionYearBean bean = ((RegistrationSelectExecutionYearBean) source);
	final StudentCurricularPlan studentCurricularPlan = bean.getRegistration().getLastStudentCurricularPlan();
	final List<CurriculumGroup> curriculumGroups = new ArrayList<CurriculumGroup>(studentCurricularPlan.getRoot().getAllCurriculumGroups());
	curriculumGroups.removeAll(studentCurricularPlan.getRoot().getCycleCurriculumGroups());
	Collections.sort(curriculumGroups, CurriculumGroup.COMPARATOR_BY_NAME_AND_ID);
	return curriculumGroups;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
