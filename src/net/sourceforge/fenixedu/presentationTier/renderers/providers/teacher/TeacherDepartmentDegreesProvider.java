package net.sourceforge.fenixedu.presentationTier.renderers.providers.teacher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.StudentsPerformanceInfoBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class TeacherDepartmentDegreesProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	Set<Degree> degrees = new HashSet<Degree>();

	StudentsPerformanceInfoBean bean = (StudentsPerformanceInfoBean) source;
	List<Tutorship> tutorships = bean.getTutorships();

	for (Tutorship tutorship : tutorships) {
	    degrees.add(tutorship.getStudentCurricularPlan().getRegistration().getDegree());
	}
	return new ArrayList<Degree>(degrees);
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
