/*
 * Created on 11/Ago/2005 - 19:15:37
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacher.Career;
import net.sourceforge.fenixedu.domain.teacher.ProfessionalCareer;
import net.sourceforge.fenixedu.domain.teacher.TeachingCareer;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class DeleteCareer extends Service {

    public void run(Integer careerId) {
	Career career = rootDomainObject.readCareerByOID(careerId);

	if (career instanceof TeachingCareer) {
	    TeachingCareer teachingCareer = (TeachingCareer) career;
	    teachingCareer.delete();

	} else if (career instanceof ProfessionalCareer) {
	    ProfessionalCareer professionalCareer = (ProfessionalCareer) career;
	    professionalCareer.delete();
	}

    }

}
