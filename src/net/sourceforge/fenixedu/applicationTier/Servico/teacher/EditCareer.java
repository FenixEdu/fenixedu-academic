/*
 * Created on 11/Ago/2005 - 17:00:02
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCareer;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoProfessionalCareer;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoTeachingCareer;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacher.ProfessionalCareer;
import net.sourceforge.fenixedu.domain.teacher.TeachingCareer;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class EditCareer extends Service {

    public void run(Integer careerId, InfoCareer infoCareer) throws ExcepcaoPersistencia {
				
		if(infoCareer instanceof InfoTeachingCareer) {
			editCareer(careerId, (InfoTeachingCareer) infoCareer);

		} else if(infoCareer instanceof InfoProfessionalCareer) {
			editCareer(careerId, (InfoProfessionalCareer) infoCareer);
		}
		
    }

	private void editCareer(Integer careerId, InfoTeachingCareer infoTeachingCareer) throws ExcepcaoPersistencia {
		IPersistentObject po = persistentSupport.getIPersistentObject();
		
		TeachingCareer teachingCareer = (TeachingCareer) po.readByOID(TeachingCareer.class, careerId);
		
        Category category = (Category)po.readByOID(Category.class,infoTeachingCareer.getInfoCategory().getIdInternal());
		//If it doesn't exist in the database, a new one has to be created
		if(teachingCareer == null) {
			Teacher teacher = (Teacher) po.readByOID(Teacher.class, infoTeachingCareer.getInfoTeacher().getIdInternal());
			teachingCareer = DomainFactory.makeTeachingCareer(teacher, category, infoTeachingCareer);

		} else {
			teachingCareer.edit(infoTeachingCareer, category);
			
		}
		
		
	}

	private void editCareer(Integer careerId, InfoProfessionalCareer infoProfessionalCareer) throws ExcepcaoPersistencia {
		IPersistentObject po = persistentSupport.getIPersistentObject();
		
		ProfessionalCareer professionalCareer = (ProfessionalCareer) po.readByOID(ProfessionalCareer.class, careerId);
		
		//If it doesn't exist in the database, a new one has to be created
		if(professionalCareer == null) {
			Teacher teacher = (Teacher) po.readByOID(Teacher.class, infoProfessionalCareer.getInfoTeacher().getIdInternal());
			professionalCareer = DomainFactory.makeProfessionalCareer(teacher, infoProfessionalCareer);
		} else {
			professionalCareer.edit(infoProfessionalCareer);
		}
		
	}

	

}
