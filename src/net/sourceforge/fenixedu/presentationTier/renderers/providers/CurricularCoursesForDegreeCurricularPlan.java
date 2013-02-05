package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.CurricularCourseMarksheetManagementBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementBaseBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.BiDirectionalConverter;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CurricularCoursesForDegreeCurricularPlan implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        final MarkSheetManagementBaseBean markSheetManagementBean = (MarkSheetManagementBaseBean) source;

        final List<CurricularCourseMarksheetManagementBean> result = new ArrayList<CurricularCourseMarksheetManagementBean>();

        if (markSheetManagementBean.hasDegree() && markSheetManagementBean.hasDegreeCurricularPlan()
                && markSheetManagementBean.hasExecutionPeriod()) {

            if (markSheetManagementBean.getDegree().hasDegreeCurricularPlans(markSheetManagementBean.getDegreeCurricularPlan())) {
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

    private void addCurricularCourses(final List<CurricularCourseMarksheetManagementBean> result,
            final List<? extends DegreeModule> dcpDegreeModules, final ExecutionSemester executionSemester) {

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

                final CurricularCourse course = (CurricularCourse) DomainObject.fromOID(Long.valueOf(values[0]).longValue());
                final ExecutionSemester semester = (ExecutionSemester) DomainObject.fromOID(Long.valueOf(values[1]).longValue());

                return new CurricularCourseMarksheetManagementBean(course, semester);
            }

            @Override
            public String deserialize(final Object object) {
                return (object == null) ? "" : ((CurricularCourseMarksheetManagementBean) object).getKey();
            }
        };
    }
}
