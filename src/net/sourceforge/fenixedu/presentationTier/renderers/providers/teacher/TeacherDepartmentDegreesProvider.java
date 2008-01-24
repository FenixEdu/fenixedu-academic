package net.sourceforge.fenixedu.presentationTier.renderers.providers.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.StudentsPerformanceInfoBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class TeacherDepartmentDegreesProvider implements DataProvider {

	public Object provide(Object source, Object currentValue) {
		List<Degree> degrees = new ArrayList<Degree>();
		
		StudentsPerformanceInfoBean bean = (StudentsPerformanceInfoBean) source;
    	List<Tutorship> tutorships = bean.getPerson().getTeacher().getActiveTutorships();
    	
    	for(Tutorship tutorship : tutorships) {
			if(!degrees.contains(tutorship.getStudentCurricularPlan().getRegistration().getDegree())) {
				degrees.add(tutorship.getStudentCurricularPlan().getRegistration().getDegree());
			}
    	}
		return degrees;
    }

    public Converter getConverter() {
    	return new DomainObjectKeyConverter();
    }

}
