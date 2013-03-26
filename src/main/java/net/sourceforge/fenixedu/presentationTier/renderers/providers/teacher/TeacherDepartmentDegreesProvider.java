package net.sourceforge.fenixedu.presentationTier.renderers.providers.teacher;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.StudentsPerformanceInfoBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Tutorship;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class TeacherDepartmentDegreesProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        return getDegrees((StudentsPerformanceInfoBean) source);
    }

    static public Set<Degree> getDegrees(StudentsPerformanceInfoBean bean) {
        Set<Degree> degrees = new TreeSet<Degree>(Collections.reverseOrder(Degree.COMPARATOR_BY_FIRST_ENROLMENTS_PERIOD_AND_ID));

        List<Tutorship> tutorships = bean.getTutorships();

        for (Tutorship tutorship : tutorships) {
            degrees.add(tutorship.getStudentCurricularPlan().getRegistration().getDegree());
        }
        return degrees;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
