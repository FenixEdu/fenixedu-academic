package net.sourceforge.fenixedu.presentationTier.renderers.providers.gradeSubmission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.teacher.gradeSubmission.MarkSheetTeacherGradeSubmissionBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CurricularCoursesByExecutionPeriodProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {

		final MarkSheetTeacherGradeSubmissionBean submissionBean = (MarkSheetTeacherGradeSubmissionBean) source;
		final List<CurricularCourse> result =
				new ArrayList<CurricularCourse>((submissionBean.getExecutionCourse() != null) ? submissionBean
						.getExecutionCourse().getAssociatedCurricularCourses() : Collections.EMPTY_LIST);

		Collections.sort(result, new Comparator<CurricularCourse>() {
			@Override
			public int compare(CurricularCourse o1, CurricularCourse o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		return result;
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
