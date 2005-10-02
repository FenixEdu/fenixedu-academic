/*
 * Created on 11/Ago/2005 - 17:00:02
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCareer;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoProfessionalCareer;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoTeachingCareer;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacher.ICategory;
import net.sourceforge.fenixedu.domain.teacher.IProfessionalCareer;
import net.sourceforge.fenixedu.domain.teacher.ITeachingCareer;
import net.sourceforge.fenixedu.domain.teacher.ProfessionalCareer;
import net.sourceforge.fenixedu.domain.teacher.TeachingCareer;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class EditCareer implements IService {

    public void run(Integer careerId, InfoCareer infoCareer) throws ExcepcaoPersistencia {
				
		if(infoCareer instanceof InfoTeachingCareer) {
			editCareer(careerId, (InfoTeachingCareer) infoCareer);

		} else if(infoCareer instanceof InfoProfessionalCareer) {
			editCareer(careerId, (InfoProfessionalCareer) infoCareer);
		}
		
    }

	private void editCareer(Integer careerId, InfoTeachingCareer infoTeachingCareer) throws ExcepcaoPersistencia {
		
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentObject po = sp.getIPersistentObject();
		
		ITeachingCareer teachingCareer = (ITeachingCareer) po.readByOID(TeachingCareer.class, careerId);
		
        ICategory category = (ICategory)po.readByOID(Category.class,infoTeachingCareer.getInfoCategory().getIdInternal());
		//If it doesn't exist in the database, a new one has to be created
		if(teachingCareer == null) {
			ITeacher teacher = (ITeacher) po.readByOID(Teacher.class, infoTeachingCareer.getInfoTeacher().getIdInternal());
			teachingCareer = DomainFactory.makeTeachingCareer(teacher, category, infoTeachingCareer);

		} else {
			teachingCareer.edit(infoTeachingCareer, category);
			
		}
		
		
	}

	private void editCareer(Integer careerId, InfoProfessionalCareer infoProfessionalCareer) throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentObject po = sp.getIPersistentObject();
		
		IProfessionalCareer professionalCareer = (IProfessionalCareer) po.readByOID(ProfessionalCareer.class, careerId);
		
		//If it doesn't exist in the database, a new one has to be created
		if(professionalCareer == null) {
			ITeacher teacher = (ITeacher) po.readByOID(Teacher.class, infoProfessionalCareer.getInfoTeacher().getIdInternal());
			professionalCareer = DomainFactory.makeProfessionalCareer(teacher, infoProfessionalCareer);
		} else {
			professionalCareer.edit(infoProfessionalCareer);
		}
		
	}

	

}
