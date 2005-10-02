/*
 * Created on 2/Out/2005 - 15:16:59
 * 
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CareerType;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.ICareer;
import net.sourceforge.fenixedu.domain.teacher.ProfessionalCareer;
import net.sourceforge.fenixedu.domain.teacher.TeachingCareer;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentCareer;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class CareerVO extends VersionedObjectsBase implements IPersistentCareer {

	public List readAllByTeacherIdAndCareerType(Integer teacherId,
			CareerType careerType) throws ExcepcaoPersistencia {
		
		ITeacher teacher = (ITeacher) readByOID(Teacher.class, teacherId);
		List<ICareer> careersList= new ArrayList<ICareer>();
		
		if(careerType.equals(CareerType.PROFESSIONAL)) {
			for(ICareer teachersCareer : teacher.getAssociatedCareers()) {
				if(teachersCareer.getOjbConcreteClass().equals(ProfessionalCareer.class.getName())) {
					careersList.add(teachersCareer);
				}
			}
		}
		else if(careerType.equals(CareerType.TEACHING)) {
			for(ICareer teachersCareer : teacher.getAssociatedCareers()) {
				if(teachersCareer.getOjbConcreteClass().equals(TeachingCareer.class.getName())) {
					careersList.add(teachersCareer);
				}
			}
		}
		
		return careersList;
	}

}
