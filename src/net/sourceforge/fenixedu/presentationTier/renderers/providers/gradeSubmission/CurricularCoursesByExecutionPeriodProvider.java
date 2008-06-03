package net.sourceforge.fenixedu.presentationTier.renderers.providers.gradeSubmission;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.teacher.gradeSubmission.MarkSheetTeacherGradeSubmissionBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CurricularCoursesByExecutionPeriodProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {

        final MarkSheetTeacherGradeSubmissionBean submissionBean = (MarkSheetTeacherGradeSubmissionBean) source;
        final List<CurricularCourse> result = (submissionBean.getExecutionCourse() != null) ? submissionBean
                .getExecutionCourse().getCurricularCoursesWithDegreeType()
                : Collections.EMPTY_LIST;
        
        Collections.sort(result, new Comparator<CurricularCourse>() {
            public int compare(CurricularCourse o1, CurricularCourse o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return result;
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
