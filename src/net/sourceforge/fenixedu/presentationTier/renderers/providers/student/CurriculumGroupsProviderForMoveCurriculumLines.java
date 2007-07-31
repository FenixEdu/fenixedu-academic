package net.sourceforge.fenixedu.presentationTier.renderers.providers.student;

import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.curriculumLine.CurriculumLineLocationBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class CurriculumGroupsProviderForMoveCurriculumLines implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final CurriculumLineLocationBean locationBean = (CurriculumLineLocationBean) source;
	final CurriculumGroup rootCurriculumGroup = locationBean.getCurriculumLine().getStudentCurricularPlan().getRoot();

	return rootCurriculumGroup.getAllCurriculumGroups();

    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
