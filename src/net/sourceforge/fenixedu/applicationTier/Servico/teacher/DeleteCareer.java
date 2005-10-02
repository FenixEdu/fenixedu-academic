/*
 * Created on 11/Ago/2005 - 19:15:37
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.domain.teacher.Career;
import net.sourceforge.fenixedu.domain.teacher.ICareer;
import net.sourceforge.fenixedu.domain.teacher.IProfessionalCareer;
import net.sourceforge.fenixedu.domain.teacher.ITeachingCareer;
import net.sourceforge.fenixedu.domain.teacher.ProfessionalCareer;
import net.sourceforge.fenixedu.domain.teacher.TeachingCareer;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentCareer;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class DeleteCareer implements IService {

	public void run(Integer careerId) throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentCareer pc = sp.getIPersistentCareer();
		
		ICareer career = (ICareer) pc.readByOID(Career.class, careerId);
		
		if(career instanceof TeachingCareer) {
			ITeachingCareer teachingCareer = (ITeachingCareer) career;
			teachingCareer.delete();

		} else if(career instanceof ProfessionalCareer) {
			IProfessionalCareer professionalCareer = (IProfessionalCareer) career;
			professionalCareer.delete();
		}
		
		
	}
	
}
