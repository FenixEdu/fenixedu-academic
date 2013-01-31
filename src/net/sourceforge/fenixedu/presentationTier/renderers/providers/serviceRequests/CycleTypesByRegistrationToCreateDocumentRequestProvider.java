package net.sourceforge.fenixedu.presentationTier.renderers.providers.serviceRequests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class CycleTypesByRegistrationToCreateDocumentRequestProvider implements DataProvider {

	@Override
	public Converter getConverter() {
		return new EnumConverter();
	}

	@Override
	public Object provide(final Object source, final Object currentValue) {
		final DocumentRequestCreateBean documentRequestCreateBean = (DocumentRequestCreateBean) source;
		final StudentCurricularPlan scp = documentRequestCreateBean.getRegistration().getLastStudentCurricularPlan();

		final List<CycleType> result = new ArrayList<CycleType>();
		for (final CycleCurriculumGroup group : scp.getInternalCycleCurriculumGrops()) {
			result.add(group.getCycleType());
		}
		return result;
	}

}
