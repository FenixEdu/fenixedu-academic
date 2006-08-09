package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementBaseBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.BeanComparator;

public class CurricularCoursesForDegreeCurricularPlan implements DataProvider {

    public Object provide(Object source, Object currentValue) {

        final MarkSheetManagementBaseBean markSheetManagementBean = (MarkSheetManagementBaseBean) source;
        final List<DegreeModule> result = new ArrayList<DegreeModule>();
        if (markSheetManagementBean.getDegree() != null && markSheetManagementBean.getDegreeCurricularPlan() != null) {
            
            if (markSheetManagementBean.getDegree().hasDegreeCurricularPlans(markSheetManagementBean.getDegreeCurricularPlan())) {
                if (markSheetManagementBean.getDegree().isBolonhaDegree()) {
                    result.addAll(markSheetManagementBean.getDegreeCurricularPlan().getDcpDegreeModules(CurricularCourse.class, null));
                } else {
                    result.addAll(markSheetManagementBean.getDegreeCurricularPlan().getCurricularCourses());
                }                
            } else {
                markSheetManagementBean.setDegreeCurricularPlan(null);
                markSheetManagementBean.setCurricularCourse(null);
            }
        }
        Collections.sort(result, new BeanComparator("name"));
        return result;
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
