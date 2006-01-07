/*
 * Created on 11/Ago/2005 - 19:15:37
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.domain.teacher.Career;
import net.sourceforge.fenixedu.domain.teacher.Career;
import net.sourceforge.fenixedu.domain.teacher.ProfessionalCareer;
import net.sourceforge.fenixedu.domain.teacher.TeachingCareer;
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
		
		Career career = (Career) pc.readByOID(Career.class, careerId);
		
		if(career instanceof TeachingCareer) {
			TeachingCareer teachingCareer = (TeachingCareer) career;
			teachingCareer.delete();

		} else if(career instanceof ProfessionalCareer) {
			ProfessionalCareer professionalCareer = (ProfessionalCareer) career;
			professionalCareer.delete();
		}
		
		
	}
	
}
