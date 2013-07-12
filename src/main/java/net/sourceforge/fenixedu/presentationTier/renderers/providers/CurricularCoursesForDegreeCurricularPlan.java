package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.CurricularCourseMarksheetManagementBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementBaseBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.BiDirectionalConverter;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixframework.FenixFramework;

public class CurricularCoursesForDegreeCurricularPlan implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        final MarkSheetManagementBaseBean markSheetManagementBean = (MarkSheetManagementBaseBean) source;

        final List<CurricularCourseMarksheetManagementBean> result = new ArrayList<CurricularCourseMarksheetManagementBean>();

        if (markSheetManagementBean.hasDegree() && markSheetManagementBean.hasDegreeCurricularPlan()
                && markSheetManagementBean.hasExecutionPeriod()) {

            if (markSheetManagementBean.getDegree().getDegreeCurricularPlansSet()
                    .contains(markSheetManagementBean.getDegreeCurricularPlan())) {
                if (markSheetManagementBean.getDegree().isBolonhaDegree()) {
                    addCurricularCourses(
                            result,
                            markSheetManagementBean.getDegreeCurricularPlan().getDcpDegreeModules(CurricularCourse.class,
                                    markSheetManagementBean.getExecutionPeriod().getExecutionYear()),
                            markSheetManagementBean.getExecutionPeriod());
                } else {
                    addCurricularCourses(result, markSheetManagementBean.getDegreeCurricularPlan().getCurricularCourses(),
                            markSheetManagementBean.getExecutionPeriod());
                }
            } else {
                markSheetManagementBean.setDegreeCurricularPlan(null);
                markSheetManagementBean.setCurricularCourseBean(null);
            }
        }
        Collections.sort(result, CurricularCourseMarksheetManagementBean.COMPARATOR_BY_NAME);

        return result;
    }

    private void addCurricularCourses(final Collection<CurricularCourseMarksheetManagementBean> result,
            final Collection<? extends DegreeModule> dcpDegreeModules, final ExecutionSemester executionSemester) {

        for (final DegreeModule degreeModule : dcpDegreeModules) {
            result.add(new CurricularCourseMarksheetManagementBean((CurricularCourse) degreeModule, executionSemester));
        }

    }

    @Override
    public Converter getConverter() {
        return new BiDirectionalConverter() {

            @Override
            public Object convert(Class type, Object value) {
                final String str = (String) value;
                if (StringUtils.isEmpty(str)) {
                    return null;
                }
                final String[] values = str.split(":");

                final CurricularCourse course = FenixFramework.getDomainObject(values[0]);
                final ExecutionSemester semester = FenixFramework.getDomainObject(values[1]);

                return new CurricularCourseMarksheetManagementBean(course, semester);
            }

            @Override
            public String deserialize(final Object object) {
                return (object == null) ? "" : ((CurricularCourseMarksheetManagementBean) object).getKey();
            }
        };
    }
}
